package com.imojiapp.imoji.sdksample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdk.ImojiCategory;
import com.imojiapp.imoji.sdk.OutlineOptions;
import com.imojiapp.imoji.sdksample.adapters.ImojiAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {
private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.gv_imoji_grid)
    GridView mImojiGrid;

    @InjectView(R.id.et_search)
    EditText mSearchEt;

    @InjectView(R.id.iv_full_imoji)
    ImageView mFullImoji;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mImojiGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Imoji imoji = (Imoji) parent.getItemAtPosition(position);
                OutlineOptions options = new OutlineOptions();
                options.color = Color.parseColor("#F88920");
                ImojiApi.with(MainActivity.this).loadFull(imoji, options).into(mFullImoji);


            }
        });

        mImojiGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Imoji imoji = (Imoji) parent.getItemAtPosition(position);

                ImojiApi.with(MainActivity.this).addImojiToUserCollection(imoji.getImojiId(), new Callback<String, String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(LOG_TAG, "Add Imoji Success with result: " + result);

                    }

                    @Override
                    public void onFailure(String result) {
                        Log.d(LOG_TAG, "Add Imoji Failed with result: " + result);
                    }
                });


                return false;
            }

        });

        ImojiApi.with(this).getFeatured(new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                ImojiAdapter adapter = new ImojiAdapter(MainActivity.this, R.layout.imoji_item_layout, result);
                mImojiGrid.setAdapter(adapter);
            }

            @Override
            public void onFailure(String error) {
                Log.d(LOG_TAG, "failure: " + error);
            }
        });

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String query = v.getText().toString();
                    ImojiApi.with(MainActivity.this).search(query, new Callback<List<Imoji>, String>() {
                        @Override
                        public void onSuccess(List<Imoji> result) {
                            ImojiAdapter adapter = new ImojiAdapter(MainActivity.this, R.layout.imoji_item_layout, result);
                            mImojiGrid.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(String error) {
                            Log.d(LOG_TAG, "failed with error: " + error);
                        }
                    });
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_create_imoji) {
            ImojiApi.with(this).createImoji();
            return true;
        }else if (item.getItemId() == R.id.action_external_oauth) {
            ImojiApi.with(this).initiateUserOauth(new Callback<String, String>() {
                @Override
                public void onSuccess(String result) {
                    Log.d(LOG_TAG, "success: " + result);
                }

                @Override
                public void onFailure(String err) {
                    Log.d(LOG_TAG, "error: " + err);
                }
            });
            return true;
        }else if (item.getItemId() == R.id.action_get_user_imojis) {
            ImojiApi.with(this).getUserImojis(new Callback<List<Imoji>, String>() {
                @Override
                public void onSuccess(List<Imoji> result) {
                    Log.d(LOG_TAG, "got user imojis: " + result.size());
                }

                @Override
                public void onFailure(String error) {
                    Log.d(LOG_TAG, "failed to get user imojis: error");
                }
            });
        }else if (item.getItemId() == R.id.action_get_categories) {
            ImojiApi.with(this).getImojiCategories(ImojiCategory.Classification.TRENDING, new Callback<List<ImojiCategory>, String>() {
                @Override
                public void onSuccess(List<ImojiCategory> result) {
                    for (ImojiCategory c : result) {
                        Log.d(LOG_TAG, "" + c.getTitle() + " " + c.getClassification());
                    }
                }

                @Override
                public void onFailure(String result) {

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }



}
