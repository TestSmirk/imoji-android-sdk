package com.imojiapp.messaging;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by sajjadtabib on 10/20/15.
 */
public class KeyboardFragment extends DialogFragment {



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
            getChildFragmentManager().beginTransaction().add(R.id.container, tf).commit();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = new Dialog(getActivity());
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        d.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        final WindowManager.LayoutParams p = new WindowManager.LayoutParams();
        p.flags &= ~(
                WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM |
                        WindowManager.LayoutParams.FLAG_SPLIT_TOUCH);
//        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
//        p.flags|= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        d.getWindow().addFlags(p.flags);
//        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        d.getWindow().addFlags(WindowManager.LayoutParams.);
        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR);
        return d;
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
