package com.imojiapp.messaging;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Api;
import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImojiSearchFragment extends Fragment {

    public static final String FRAGMENT_TAG = ImojiSearchFragment.class.getSimpleName();
    private static final String LOG_TAG = ImojiSearchFragment.class.getSimpleName();
    public static final String QUERY_BUNDLE_ARG_KEY = "QUERY_BUNDLE_ARG_KEY";
    public static final String SHOW_INPUT_BAR_BUNDLE_ARG_KEY = "SHOW_INPUT_BAR_BUNDLE_ARG_KEY";

    RecyclerView mImojiGrid;
    ImojiRecyclerAdapter mImojiRecyclerAdapter;
    ProgressBar mProgress;
    EditText mSearchEt;
    InputMethodManager mImm;


    public static ImojiSearchFragment newInstance(String query) {

        return newInstance(query, true);
    }

    public static ImojiSearchFragment newInstance(String query, boolean showInputBar) {
        ImojiSearchFragment f = new ImojiSearchFragment();

        Bundle args = new Bundle();
        if (query != null) {
            args.putString(QUERY_BUNDLE_ARG_KEY, query);
        }
        args.putBoolean(SHOW_INPUT_BAR_BUNDLE_ARG_KEY, showInputBar);
        f.setArguments(args);


        return f;
    }

    public static ImojiSearchFragment newInstance() {
        return newInstance(null);
    }


    private String mQuery;
    private boolean mShowInputBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(QUERY_BUNDLE_ARG_KEY)) {
            mQuery = getArguments().getString(QUERY_BUNDLE_ARG_KEY);
        }

        mShowInputBar = getArguments().getBoolean(SHOW_INPUT_BAR_BUNDLE_ARG_KEY);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_imoji_search, container, false);

        mImojiGrid = (RecyclerView) v.findViewById(R.id.gv_imoji_grid);
        mProgress = (ProgressBar) v.findViewById(R.id.pb_progress);
        mSearchEt = (EditText) v.findViewById(R.id.et_search);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        mImojiGrid.setLayoutManager(layoutManager);
        mImojiRecyclerAdapter = new ImojiRecyclerAdapter(getActivity());
        mImojiGrid.setAdapter(mImojiRecyclerAdapter);
        mImojiGrid.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Imoji imoji = mImojiRecyclerAdapter.getItemAt(position);
                ((MessageInterface) getActivity()).addImoji(imoji);
            }
        }));

        mSearchEt.setVisibility(mShowInputBar ? View.VISIBLE : View.GONE);
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (isResumed()) {


                    if ((actionId & EditorInfo.IME_ACTION_DONE) > 0 || (actionId & EditorInfo.IME_ACTION_SEARCH) > 0) {
                        String query = v.getText().toString();
                        mProgress.setVisibility(View.VISIBLE);
                        IBinder token = getActivity().getCurrentFocus().getWindowToken();
                        mImm.hideSoftInputFromWindow(token, 0);
                        doSearch(query, false);

                        return true;
                    }
                }
                return false;
            }
        });

        if (mShowInputBar) {
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (savedInstanceState == null && mQuery != null) {
            doSearch(mQuery, false);
        }
    }

    public void doSearch(String query, boolean sentence) {
        Map<String, String> params = new HashMap<>();
        if (sentence) {
            params.put(Api.SearchParams.SENTENCE, query);
        } else {
            params.put(Api.SearchParams.QUERY, query);
        }
        params.put(Api.SearchParams.OFFSET, String.valueOf(0));
        params.put(Api.SearchParams.NUM_RESULTS, String.valueOf(60));

        ImojiApi.with(getActivity()).search(params, new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                if (isResumed()) {
                    if (result.size() > 0) {
                        mImojiRecyclerAdapter.setList(result);
                    }
                    mProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String error) {
                mProgress.setVisibility(View.GONE);
                Log.d(LOG_TAG, "failed with error: " + error);
            }
        });
    }

}
