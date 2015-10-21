package com.imojiapp.messaging;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ImojiSearchFragment extends Fragment {

    private static final String LOG_TAG = ImojiSearchFragment.class.getSimpleName();
    public static final String QUERY_BUNDLE_ARG_KEY = "QUERY_BUNDLE_ARG_KEY";

    EditText mSearchEt;

    GridView mImojiGrid;

    ProgressBar mProgress;
    private InputMethodManager mImm;
    private CancellableCallback mCallback;


    public static ImojiSearchFragment newInstance(String query) {
        ImojiSearchFragment f = new ImojiSearchFragment();

        Bundle args = new Bundle();
        if (query != null) {
            args.putString(QUERY_BUNDLE_ARG_KEY, query);
        }
        f.setArguments(args);


        return f;
    }

    public static ImojiSearchFragment newInstance() {
        return newInstance(null);
    }


    private String mQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(QUERY_BUNDLE_ARG_KEY)) {
            mQuery = getArguments().getString(QUERY_BUNDLE_ARG_KEY);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_imoji_search, container, false);

        mSearchEt = (EditText) v.findViewById(R.id.et_search);
        mImojiGrid = (GridView) v.findViewById(R.id.gv_imoji_grid);
        mProgress = (ProgressBar) v.findViewById(R.id.pb_progress);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String query = v.getText().toString();
                    mProgress.setVisibility(View.VISIBLE);
                    mImm.hideSoftInputFromWindow(mSearchEt.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                    doSearch(query);

                    return true;
                }
                return false;
            }
        });

        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mImojiGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Imoji imoji = (Imoji) parent.getItemAtPosition(position);
//                Utils.launchImojiPopupWindow(getActivity(), imoji);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (savedInstanceState == null && mQuery != null) {
            mSearchEt.setText(mQuery);
            doSearch(mQuery);
        }
    }

    private void doSearch(String query) {
        if (mCallback != null) {
            mCallback.cancel();
        }
        mCallback = new CancellableCallback(this);
        ImojiApi.with(getActivity()).search(query, mCallback);
    }

    private static class CancellableCallback implements Callback<List<Imoji>, String> {


        private WeakReference<ImojiSearchFragment> mSearchFragmentWeakReference;
        private boolean mIsCancelled;

        public CancellableCallback(ImojiSearchFragment fragment) {
            mSearchFragmentWeakReference = new WeakReference<ImojiSearchFragment>(fragment);
        }

        public void cancel() {
            mIsCancelled = true;
        }

        @Override
        public void onSuccess(List<Imoji> result) {
            ImojiSearchFragment fragment = mSearchFragmentWeakReference.get();
            if (!mIsCancelled && fragment != null && fragment.isResumed()) {


                ImojiAdapter adapter = new ImojiAdapter(fragment.getActivity(), R.layout.imoji_item_layout, result == null ? new ArrayList<Imoji>() : result);
                fragment.mImojiGrid.setAdapter(adapter);
                fragment.mProgress.setVisibility(View.GONE);
            }

        }

        @Override
        public void onFailure(String error) {
            if (mSearchFragmentWeakReference.get() != null) {
                ImojiSearchFragment fragment = mSearchFragmentWeakReference.get();
                fragment.mProgress.setVisibility(View.GONE);

            }
            Log.d(LOG_TAG, "failed with error: " + error);
        }
    }

}
