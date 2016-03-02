package io.imoji.keywordsearch;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.imoji.sdk.ApiTask;
import com.imoji.sdk.ImojiSDK;
import com.imoji.sdk.Session;
import com.imoji.sdk.objects.Imoji;
import com.imoji.sdk.response.ImojisResponse;

import java.util.List;
import java.util.concurrent.Future;

public class ImojiSearchFragment extends Fragment {

    private static final String LOG_TAG = ImojiSearchFragment.class.getSimpleName();
    public static final String QUERY_BUNDLE_ARG_KEY = "QUERY_BUNDLE_ARG_KEY";

    EditText mSearchEt;

    GridView mImojiGrid;

    ProgressBar mProgress;
    private InputMethodManager mImm;
    private AsyncTask<Future<ImojisResponse>, Void, ImojisResponse> mCallback;


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
            mCallback.cancel(true);
        }
        Session session = ImojiSDK.getInstance().createSession(getContext());

        mCallback = session.searchImojis(query).executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
            @Override
            protected void onPostExecute(ImojisResponse imojisResponse) {
                List<Imoji> imojis = imojisResponse.getImojis();
                ImojiAdapter adapter = new ImojiAdapter(getActivity(), R.layout.imoji_item_layout, imojis);
                mImojiGrid.setAdapter(adapter);
                mProgress.setVisibility(View.GONE);
            }
        });

    }
}
