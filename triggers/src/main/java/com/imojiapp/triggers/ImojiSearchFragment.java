package com.imojiapp.triggers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.imojiapp.imoji.sdk.Api;
import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.triggers.view.RecyclerItemClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImojiSearchFragment extends Fragment {

    public static final String FRAGMENT_TAG = ImojiSearchFragment.class.getSimpleName();
    private static final String LOG_TAG = ImojiSearchFragment.class.getSimpleName();
    public static final String QUERY_BUNDLE_ARG_KEY = "QUERY_BUNDLE_ARG_KEY";

    RecyclerView mImojiGrid;
    ImojiRecyclerAdapter mImojiRecyclerAdapter;
    ProgressBar mProgress;



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

        mImojiGrid = (RecyclerView) v.findViewById(R.id.gv_imoji_grid);
        mProgress = (ProgressBar) v.findViewById(R.id.pb_progress);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mImojiGrid.setLayoutManager(linearLayoutManager);
        mImojiRecyclerAdapter = new ImojiRecyclerAdapter(getActivity());
        mImojiGrid.setAdapter(mImojiRecyclerAdapter);
        mImojiGrid.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Imoji imoji = mImojiRecyclerAdapter.getItemAt(position);
                ((MessageInterface)getActivity()).addImoji(imoji);
            }
        }));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
