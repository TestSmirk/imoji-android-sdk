package io.imoji.photointegration;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imoji.sdk.ApiTask;
import com.imoji.sdk.ImojiSDK;
import com.imoji.sdk.response.ImojisResponse;

import java.util.List;

public class ImojiSearchFragment extends Fragment {

    private static final String LOG_TAG = ImojiSearchFragment.class.getSimpleName();
    public static final String QUERY_BUNDLE_ARG_KEY = "QUERY_BUNDLE_ARG_KEY";

    EditText mSearchEt;

    GridView mImojiGrid;

    ProgressBar mProgress;
    private InputMethodManager mImm;


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
                    mImm.hideSoftInputFromWindow(mSearchEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    doSearch(query);

                    return true;
                }
                return false;
            }
        });

        mImojiGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isResumed()) {
                    ((PhotoEditorActivity) getActivity()).addImojiToEditor((BitmapDrawable) ((ImageView) view).getDrawable());
                    getFragmentManager().beginTransaction().remove(ImojiSearchFragment.this).commit();
                }
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
        ImojiSDK.getInstance()
                .createSession(getContext())
                .searchImojis(query)
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        if (isResumed()) {
                            ImojiAdapter adapter = new ImojiAdapter(getActivity(), R.layout.imoji_item_layout, imojisResponse.getImojis());
                            mImojiGrid.setAdapter(adapter);
                            mProgress.setVisibility(View.GONE);

                        }
                    }
                });
    }
}
