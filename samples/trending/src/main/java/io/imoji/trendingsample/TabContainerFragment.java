package io.imoji.trendingsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import io.imoji.sdk.objects.Category;

import io.imoji.imojitrendingsample.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabContainerFragment extends Fragment {

    public static final String FRAGMENT_TAG = TabContainerFragment.class.getSimpleName();
    public static final String SELECTED_TAB_BUNDLE_ARG_KEY = "SELECTED_TAB_BUNDLE_ARG_KEY";
    private static final String LOG_TAG = TabContainerFragment.class.getSimpleName();

    public static TabContainerFragment newInstance() {
        TabContainerFragment f = new TabContainerFragment();

        Bundle args = new Bundle();
        f.setArguments(args);

        return f;
    }

    RadioGroup mButtonGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_container, container, false);
        mButtonGroup = (RadioGroup) v.findViewById(R.id.rg_bottom_bar);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isResumed()) {
                    Fragment f;
                    switch (checkedId) {
                        case R.id.rb_reactions:
                            f = ImojiCategoryFragment.newInstance(Category.Classification.Generic.name());
                            getFragmentManager().beginTransaction().replace(R.id.tab_container, f).commit();
                            break;
                        case R.id.rb_trending:
                            f = ImojiCategoryFragment.newInstance(Category.Classification.Trending.name());
                            getFragmentManager().beginTransaction().replace(R.id.tab_container, f).commit();
                            break;
                        case R.id.rb_search:
                            f = ImojiSearchFragment.newInstance();
                            getFragmentManager().beginTransaction().replace(R.id.tab_container, f).commit();
                            break;
                    }

                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mButtonGroup.check(R.id.rb_trending);
    }

}
