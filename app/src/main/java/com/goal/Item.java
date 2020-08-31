package com.goal;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/4/26.
 */

public class Item {
    public String item_u_name;
    public Bitmap bt_image;
    public String item_id;
    public String item_content;
    public String item_date1;
    public String item_date2;
    public String item_goal_total_value;
    public String item_goal_value;
    public String item_goal_times;
    public String item_finish_times;
    public String item_rest_times;


    public int item_sum_progress;
    public int item_finish_progress;
    public String item_repeat;
    public String item_last_reset_time;




    public boolean flag;        //任务是否已经完成
    public int Times;           //任务的执行次数

    public void Set_bt_image(Bitmap bitmap){
        this.bt_image=bitmap;           //改变
    }

  /*  public  void Set_Times(String new_times){
        this.item_goal_times=new_times;
    }
    */

    public  void Set_Rest_Times(String new_times){
        this.item_rest_times=new_times;
    }
    public  void Set_Finish_Times(String new_times){
        this.item_finish_times=new_times;
    }
    public  void Set_cur_progress(int new_times){
        this.item_finish_progress=new_times;
    }
}
