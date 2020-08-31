package com.goal;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/3.
 */
public class Test_Adapter extends BaseAdapter implements Callback {

    private int cur_progress;
    private int sum_progress;

    List<Item> itemList;
    Context context;
    Callback callback;

    public Test_Adapter (Context context, List<Item>itemList, Callback callback) {
        this.context=context;
        this.itemList=itemList;
        this.callback=callback;

    }

    /**
     * 内部类，可根据实际的Item的布局进行修改
     */

    class ViewHolder{
        TextView item_content;
        TextView item_date1;
        TextView item_goal_value;
        ProgressBar item_progress_bar;
        TextView item_rest_times;
    }
    @Override
    public void Enter_next(int position, boolean want_minus_times_of_item) {

    }

    @Override
    public void explotion(int position, View view){

    }
    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        view = LayoutInflater.from(context).inflate(R.layout.item, null);
        viewHolder=new ViewHolder();

        //使用findViewById分别找到每个子控件

        viewHolder.item_content=(TextView)view.findViewById(R.id.item_content);
        viewHolder.item_date1=(TextView)view.findViewById(R.id.item_date1);
        viewHolder.item_goal_value=(TextView)view.findViewById(R.id.item_goal_value);
        viewHolder.item_rest_times=(TextView)view.findViewById(R.id.item_times);
        viewHolder.item_progress_bar = (ProgressBar)view.findViewById(R.id.item_progressbar);


        viewHolder.item_content.setText(itemList.get(i).item_content);
        viewHolder.item_date1.setText(itemList.get(i).item_date1+"   "+itemList.get(i).item_last_reset_time);
        viewHolder.item_goal_value.setText(itemList.get(i).item_goal_value);
        //这里需要修改，显示在圆圈里面的时间
        viewHolder.item_rest_times.setText(itemList.get(i).item_rest_times);

        cur_progress = itemList.get(i).item_finish_progress;
        sum_progress = itemList.get(i).item_sum_progress;
        float f= (float) (1.0*cur_progress/sum_progress*100);
        viewHolder.item_progress_bar.setProgress((int)f);



        String mRestTimes=itemList.get(i).item_rest_times;
        if(Integer.valueOf(mRestTimes)==0){
            viewHolder.item_rest_times.setText("完成");
            //  BitmapDrawable bitmapDrawable0=(BitmapDrawable)context.getResources().getDrawable(R.drawable.test_succeed);
            // viewHolder.imageView.setImageBitmap(bitmapDrawable0.getBitmap());
        }else{
            viewHolder.item_rest_times.setText(String.valueOf(itemList.get(i).item_rest_times));                 //一定要用String.valueof这个函数，不然会报错
        }
        viewHolder.item_rest_times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.explotion(i,view);
                callback.Enter_next(i,true);
            }
        });

        return view;

    }

}
