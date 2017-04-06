package zdm.com.vlayout;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.LayoutManagerHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LayoutChunkResult;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelperEx;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    private static final boolean BANNER_LAYOUT = true;

    private static final boolean LINEAR_LAYOUT = true;

    private static final boolean ONEN_LAYOUT = true;

    private static final boolean GRID_LAYOUT = true;

    private static final boolean STICKY_LAYOUT = true;

    private static final boolean HORIZONTAL_SCROLL_LAYOUT = true;

    private static final boolean SCROLL_FIX_LAYOUT = true;

    @InjectView(R.id.recycle)
    RecyclerView mRecycle;
    private VirtualLayoutManager layoutManager;
    private Runnable trigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        //生成VirtualLayoutManager对象
        layoutManager = new VirtualLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);

        //设置分割线
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((VirtualLayoutManager.LayoutParams) view.getLayoutParams()).getViewPosition();
                outRect.set(4, 4, 4, 4);
            }
        };
        mRecycle.addItemDecoration(itemDecoration);


        //  创建recyclerView的View池（类似线程池）
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecycle.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        //加载数据
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        mRecycle.setAdapter(delegateAdapter);

        //创建DelegateAdapter的Adapter集合 便于设置adapter
        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        //设置对应的adapter
        if (BANNER_LAYOUT) {//banner图
            adapters.add(new SubAdapter(this, new LinearLayoutHelper(), 1) {

                //处理轮播图
                @Override
                public void onViewRecycled(RecyclerView.ViewHolder holder) {
                    if (holder.itemView instanceof ViewPager) {
                        ((ViewPager) holder.itemView).setAdapter(null);
                    }
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                    if (holder.itemView instanceof ViewPager) {
                        ViewPager viewPager = (ViewPager) holder.itemView;

                        viewPager.setLayoutParams(new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));

                        // from position to get adapter
                        viewPager.setAdapter(new PagerAdapter(this, viewPool));
                    }
                }

                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    if (viewType == 1) {
                        return new ViewHolder(LayoutInflater.from(MainActivity.this)
                                .inflate(R.layout.view_pager, parent, false));
                    }
                    return super.onCreateViewHolder(parent, viewType);
                }

                @Override
                public int getItemViewType(int position) {
                    return 1;
                }
            });

            if (GRID_LAYOUT) {
                //传入每行的个数
                GridLayoutHelper layoutHelper = new GridLayoutHelper(4);
                //间距  setHGap 水平间距  setVGap 垂直间距
                layoutHelper.setHGap(3);
                layoutHelper.setMargin(10, 10, 10, 10);
                //圆角
                layoutHelper.setAspectRatio(3f);
                //总数量
                adapters.add(new SubAdapter(this, layoutHelper, 8));
            }

            if (ONEN_LAYOUT) {
                OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
//                helper.setBgColor(0xff876384);
                helper.setMargin(10, 10, 10, 10);
//                helper.setPadding(10, 10, 10, 10);
                adapters.add(new SubAdapter(this, helper, 3) {
                    @Override
                    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                        super.onBindViewHolder(holder, position);
                        VirtualLayoutManager.LayoutParams layoutParams = new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
                        holder.itemView.setLayoutParams(layoutParams);
                    }

                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return super.onCreateViewHolder(parent, viewType);
                    }
                });
            }

            if (ONEN_LAYOUT) {
                OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
                helper.setBgColor(0xff876384);
                helper.setMargin(0, 10, 0, 10);
                adapters.add(new SubAdapter(this, helper, 4));
            }

            if (ONEN_LAYOUT) {
                OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
                helper.setBgColor(0xff876384);
                helper.setMargin(0, 10, 0, 10);
                adapters.add(new SubAdapter(this, helper, 5));
            }

            if (ONEN_LAYOUT) {
                OnePlusNLayoutHelperEx helper = new OnePlusNLayoutHelperEx();
                helper.setBgColor(0xff876384);
                helper.setMargin(0, 10, 0, 10);
                adapters.add(new SubAdapter(this, helper, 5));
            }


            if (ONEN_LAYOUT) {
                OnePlusNLayoutHelperEx helper = new OnePlusNLayoutHelperEx();
                helper.setBgColor(0xff876384);
                helper.setMargin(0, 10, 0, 10);
                helper.setColWeights(new float[]{40f, 45f, 15f, 60f, 0f, 30f, 30f});
                adapters.add(new SubAdapter(this, helper, 7));
            }

            if (LINEAR_LAYOUT)
                adapters.add(new SubAdapter(this, new LinearLayoutHelper(), 100));

            delegateAdapter.setAdapters(adapters);

            //轮询当前是什么布局-->暂没发现有啥用出
            final Handler mainHandler = new Handler(Looper.getMainLooper());

            trigger = new Runnable() {
                @Override
                public void run() {
                    // recyclerView.scrollToPosition(22);
                    // recyclerView.getAdapter().notifyDataSetChanged();
                    mRecycle.requestLayout();
                    // mainHandler.postDelayed(trigger, 1000);
                }
            };

            mainHandler.postDelayed(trigger, 1000);
        }
    }
}

