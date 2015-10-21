package com.imojiapp.messaging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdk.ImojiCategory;

import java.util.List;

public class ImojiCategoryFragment extends Fragment {
    public static final String CLASSIFICATION_BUNDLE_ARG_KEY = "CLASSIFICATION_BUNDLE_ARG_KEY";
    private static final String LOG_TAG = ImojiCategoryFragment.class.getSimpleName();
    GridView mCategoryGrid;

    ImojiCategoryAdapter mCategoryAdapter;
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
        mCategoryGrid = (GridView) v.findViewById(R.id.gv_imoji_grid);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        loadImojiCategories(mClassification);

        mCategoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isResumed()) {
                    ImojiCategory category = (ImojiCategory) parent.getItemAtPosition(position);
                    ImojiSearchFragment f = ImojiSearchFragment.newInstance(category.getSearchText());
                    getFragmentManager().beginTransaction().replace(R.id.tab_container, f).commit();
                }
            }
        });
    }


    private void loadImojiCategories(String classification) {
        ImojiApi.with(getActivity()).getImojiCategories(classification, new Callback<List<ImojiCategory>, String>() {
            @Override
            public void onSuccess(List<ImojiCategory> result) {
                if (isResumed()) {
                    mCategoryAdapter = new ImojiCategoryAdapter(getActivity(), R.layout.category_item_layout, result);
                    mCategoryGrid.setAdapter(mCategoryAdapter);
                }

            }

            @Override
            public void onFailure(String result) {
                Log.w(LOG_TAG, "" + result);

            }
        });
    }

}
