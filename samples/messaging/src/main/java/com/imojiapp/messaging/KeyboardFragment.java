package com.imojiapp.messaging;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by sajjadtabib on 10/20/15.
 */
public class KeyboardFragment extends Fragment {

    public static final String FRAGMENT_TAG = KeyboardFragment.class.getSimpleName();
    private static final String LOG_TAG = KeyboardFragment.class.getSimpleName();

    EditText mInputBar;
    Button mSendBt;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mInputBar = (EditText) view.findViewById(R.id.et_input_bar);
        mInputBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId & EditorInfo.IME_ACTION_DONE) > 0 || (actionId & EditorInfo.IME_ACTION_SEND) > 0) {
                    if (isResumed()) {
                        if (v.getText().length() > 0) {
                            ((MessageInterface) getActivity()).addText(v.getText().toString());
                            v.getEditableText().clear();
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        mSendBt = (Button) view.findViewById(R.id.bt_send);
        mSendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isResumed()) {
                    if (mInputBar.getText().length() > 0) {
                        ((MessageInterface) getActivity()).addText(mInputBar.getText().toString());
                        mInputBar.getText().clear();
                    }
                }
            }
        });
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
