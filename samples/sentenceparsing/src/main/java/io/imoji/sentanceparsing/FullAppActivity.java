package io.imoji.sentanceparsing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.imoji.searchsample.R;

/**
 * Created by sajjadtabib on 8/25/15.
 */
public class FullAppActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_app);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.container, TabContainerFragment.newInstance()).commit();
    }
}
