package fresh_RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myrecyclerview.R;
import com.example.administrator.myrecyclerview.fresh.githubRefresh.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/27.
 */
public class RefreshListActivity extends Activity implements View.OnClickListener, PullLoadMoreRecyclerView.PullLoadMoreListener {
    private PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
    private List<String> list;
    private MyAdapter adapter;

   /* 调用下拉刷新和加载更多

    mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore() {

        }
    });
    刷新结束

    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
    其他方法

            显示下拉刷新

    mPullLoadMoreRecyclerView.setRefreshing(true);
    不需要下拉刷新

    mPullLoadMoreRecyclerView.setPullRefreshEnable(false);
    不需要上拉刷新

    mPullLoadMoreRecyclerView.setPushRefreshEnable(false);
    设置上拉刷新文字

    mPullLoadMoreRecyclerView.setFooterViewText("loading");
    设置上拉刷新文字颜色

    mPullLoadMoreRecyclerView.setFooterViewTextColor(R.color.white);
    设置加载更多背景色

    mPullLoadMoreRecyclerView.setFooterViewBackgroundColor(R.color.colorBackground);
    设置下拉刷新颜色

    mPullLoadMoreRecyclerView.setColorSchemeResources(android.R.color.holo_red_dark,android.R.color.holo_blue_dark);
    快速Top

    mPullLoadMoreRecyclerView.scrollToTop();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        init();

    }

    private void init() {
        pullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.swipeRefreshLayout);
        //设置布局模式
        list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new StringBuilder("object").append(String.valueOf(i)).toString());
        }

        adapter = new MyAdapter();
        pullLoadMoreRecyclerView.setLinearLayout();
        pullLoadMoreRecyclerView.setAdapter(adapter);
        //设置监听
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
    }

    //下拉刷新
    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //2s后设置刷新完成
                pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
            }
        }, 2000);
    }

    //上拉刷新
    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //2s后设置刷新完成
                for (int i = 0; i < 10; i++) {
                    list.add("更新" + String.valueOf(i));
                }
                adapter.notifyDataSetChanged();
                pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHodler> {

        @Override
        public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            /**需要将inflate的view传到viewhodler中去**/
            View view = LayoutInflater.from(RefreshListActivity.this)
                    .inflate(R.layout.item_recyclerview, parent, false);
            view.setOnClickListener(RefreshListActivity.this);

            MyViewHodler myViewHodler = new MyViewHodler(view);


            return myViewHodler;
        }

        //将adapter和viewhodler进行绑定
        @Override
        public void onBindViewHolder(MyViewHodler holder, int position) {

            holder.textView.setText(list.get(position));
            holder.itemView.setTag(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        /**
         * 需要自行设置监听事件
         **/

        //重写封装好的viewhodler
        public class MyViewHodler extends RecyclerView.ViewHolder {

            public TextView textView;

            public MyViewHodler(View itemView) {
                super(itemView);
                //传入实例化的itemview
                textView = (TextView) itemView.findViewById(R.id.tv_wifi);

            }
        }


    }
}


