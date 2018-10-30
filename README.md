# CountDownTextView RecyclerView中使用

```java
/**
 * Created by JiaShuai on 2018/6/22.
 * RecycleView 中使用情参考该类
 * 仅供参考，懒得写实例了
 */

public class ServicePlanAdapter extends RVBaseAdapter<C4107_ServicePlanListBean.Entity.PlanListAppItem> {

    private long mCurrentTime;
    private Timer timer;


    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTextView> countDownMap;


    public ServicePlanAdapter(Context context) {
        countDownMap = new SparseArray<>();
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mCurrentTime = mCurrentTime + 1000;
                }
            }, 0, 1000);
        }
    }


    public void setNewData(List<C4107_ServicePlanListBean.Entity.PlanListAppItem> datas, long mCurrentTime) {
        this.mCurrentTime = mCurrentTime;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlanUnauthorizedHolder(inflater.inflate(R.layout.item_plan_unauthorized, parent, false));
    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }
        if (timer != null) {
            timer.cancel();
        }
        for (int i = 0, length = countDownMap.size(); i < length; i++) {
            CountDownTextView cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        getItem(position).currentTime = mCurrentTime;
        PlanUnauthorizedHolder unauthorizedHolder = (PlanUnauthorizedHolder) holder;
        long endTime = getItem(position).authorizationEndTime;
        unauthorizedHolder.ttv_countdown.setCall(() -> {
            Handler handler = new Handler();
            Runnable r = () -> {
                mDatas.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount() - position);
            };
            handler.post(r);

        });
        unauthorizedHolder.ttv_countdown.cancel();
        unauthorizedHolder.ttv_countdown.start(endTime - mCurrentTime);
        countDownMap.put(unauthorizedHolder.ttv_countdown.hashCode(), unauthorizedHolder.ttv_countdown);


    }
}

```

