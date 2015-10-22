package com.imojiapp.messaging;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by sajjadtabib on 10/20/15.
 */
public class KeyboardFragment extends Fragment {



    public static final String FRAGMENT_TAG = KeyboardFragment.class.getSimpleName();
    private static final String LOG_TAG = KeyboardFragment.class.getSimpleName();

    public static KeyboardFragment newInstance() {
        KeyboardFragment f = new KeyboardFragment();

        Bundle args = new Bundle();
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_keyboard, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            TabContainerFragment tf = TabContainerFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.tab_holder, tf).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
