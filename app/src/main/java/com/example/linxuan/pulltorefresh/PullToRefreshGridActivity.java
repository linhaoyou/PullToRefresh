package com.example.linxuan.pulltorefresh;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.LinkedList;

/**
 * Created by linxuan on 2017/10/13.
 */

public class PullToRefreshGridActivity extends AppCompatActivity {
    private static final String TAG = "PullToRefreshGridActivi";
    private LinkedList<String> mListItems;
    private PullToRefreshGridView mPullToRefreshGridView;
    private ArrayAdapter<String> mAdapter;

    private int mItemCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptr_grid);
        mPullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
        mPullToRefreshGridView.setMode(PullToRefreshBase.Mode.BOTH);
        // 初始化数据和数据源
        initDatas();

        mAdapter = new ArrayAdapter<String>(this,R.layout.grid_item,
                R.id.id_grid_item_text, mListItems);
        mPullToRefreshGridView.setAdapter(mAdapter);

        mPullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                Log.e(TAG, "onPullDownToRefresh: ");
                String label = DateUtils.formatDateTime(
                        getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                //Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);

                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                Log.e(TAG, "onPullUpToRefresh: ");
                new GetDataTask().execute();
            }
        });
    }

    private void initDatas() {
        mListItems = new LinkedList<>();

        for (int i = 0; i < mItemCount; i++) {
            mListItems.add(i + "");
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mListItems.add(""+mItemCount++);
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullToRefreshGridView.onRefreshComplete();
        }
    }
}
