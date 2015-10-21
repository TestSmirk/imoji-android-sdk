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

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdk.ImojiCategory;
import com.imojiapp.triggers.view.RecyclerItemClickListener;

import java.util.List;

public class ImojiCategoryFragment extends Fragment {
    public static final String CLASSIFICATION_BUNDLE_ARG_KEY = "CLASSIFICATION_BUNDLE_ARG_KEY";
    private static final String LOG_TAG = ImojiCategoryFragment.class.getSimpleName();
    RecyclerView mCategoryGrid;

    ImojiCategoryRecyclerAdapter mCategoryAdapter;
    private String mClassification;

    public static ImojiCategoryFragment newInstance(String classification) {
        ImojiCategoryFragment f = new ImojiCategoryFragment();

        Bundle args = new Bundle();
        args.putString(CLASSIFICATION_BUNDLE_ARG_KEY, classification);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassification = getArguments().getString(CLASSIFICATION_BUNDLE_ARG_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_imoji_category, container, false);
        mCategoryGrid = (RecyclerView) v.findViewById(R.id.rv_imoji_list);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mCategoryGrid.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImojiCategory imojiCategory = mCategoryAdapter.getItemAt(position);

                ImojiSearchFragment fragment = ImojiSearchFragment.newInstance(imojiCategory.getSearchText());
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commitAllowingStateLoss();

            }
        }));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mCategoryGrid.setLayoutManager(manager);
        mCategoryAdapter = new ImojiCategoryRecyclerAdapter(getActivity());
        mCategoryGrid.setAdapter(mCategoryAdapter);
        loadImojiCategories(mClassification);
    }

    private void loadImojiCategories(String classification) {
        ImojiApi.with(getActivity()).getImojiCategories(classification, new Callback<List<ImojiCategory>, String>() {
            @Override
            public void onSuccess(List<ImojiCategory> result) {
                if (isResumed()) {
                    mCategoryAdapter.setImojiCategories(result);
                }

            }

            @Override
            public void onFailure(String result) {
                Log.w(LOG_TAG, "" + result);

            }
        });
    }

}
