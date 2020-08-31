package com.goal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.goal.Calender.CalendarView_Activity;
import com.goal.Explotion.ExplosionField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/2.
 */
public class TaskDisplayActivity extends AppCompatActivity implements Callback {
    private MyDBHelper dbHelper;
    private SQLiteDatabase db;

    private Toolbar mToolbar;
    private ImageButton add_imageButton;

    private ExplosionField mExplosionField;

    private Cursor cursor;
    int index;

    Test_Adapter test_adapter;
    ListView listView;
    List<Item> itemList;
    List<Map<String,Object>>  reset_ID_RestTimes_list;

    private Calendar mCalendar;
    private int cur_progress=0;
    private int mYear, mMonth,mDay;
    private String mContent;
    private String mDate;
    private String mDate1;
    private String mGoalTotalValue;
    private String mGoalValue;
    private String mGoalTimes;
    private String mFinishTimes;
    private String mRestTimes;
    private String mLastResetTime;
    private int mDaysOfMonth;
    private int mDayOfWeek;
    private String mRepeatType;
    private String mFirstDayOfWeek,mLastDayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_display_activity);

        initViews();

        dbHelper = new MyDBHelper(TaskDisplayActivity.this);

        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        mDaysOfMonth=mCalendar.getMaximum(Calendar.DAY_OF_MONTH);
        mDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("\n今天是星期"+mDayOfWeek);
        System.out.println("\n这个月有"+mDaysOfMonth+"天");
        mDate = dbHelper.Time_Format(String.valueOf(mYear)) + "-" + dbHelper.Time_Format(String.valueOf(mMonth + 1)) + "-" + dbHelper.Time_Format(String.valueOf(mDay));

        itemList = new ArrayList<>();
        try {
            get_data();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dbHelper.getWritableDatabase();
        test_adapter = new Test_Adapter(TaskDisplayActivity.this, itemList, this);
        listView.setAdapter(test_adapter);
        System.out.println(itemList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//listview的监控
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mContent = itemList.get(position).item_content;
                mDate1 = itemList.get(position).item_date1;
                mGoalTotalValue = itemList.get(position).item_goal_total_value;
                mGoalValue = itemList.get(position).item_goal_value;
                mGoalTimes = itemList.get(position).item_goal_times;
                mRestTimes = itemList.get(position).item_rest_times;
                mFinishTimes = itemList.get(position).item_finish_times;
                mRepeatType = itemList.get(position).item_repeat;

                cur_progress = itemList.get(position).item_finish_progress;
                mLastResetTime = itemList.get(position).item_last_reset_time;


                Intent intent = new Intent(getApplicationContext(),TaskEditActivity.class);

                intent.putExtra("goal_id", itemList.get(position).item_id);//传出id
                intent.putExtra("U_name", itemList.get(position).item_u_name);
                intent.putExtra("goal_content", mContent);//传出goal_content
                intent.putExtra("date1", mDate1);
                intent.putExtra("goal_total_value", mGoalTotalValue);
                intent.putExtra("goal_value", mGoalValue);
                intent.putExtra("goal_times", mGoalTimes);
                intent.putExtra("rest_times", mRestTimes);
                intent.putExtra("finish_times",mFinishTimes);
                intent.putExtra("repeat", mRepeatType);
                intent.putExtra("cur_progress",cur_progress);
                intent.putExtra("last_reset_time",mLastResetTime);

                test_adapter.notifyDataSetChanged();
                startActivity(intent);
                //finish();
            }
        });
        registerForContextMenu(listView);

        //可能用于长按删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // TODO Auto-generated method stub
                index = arg2;//长按选中第index个
                return false;
            }
        });
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {//长按菜单
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("选项");
                menu.add(0, 0, 0, "删除目标");
                menu.add(0, 1, 1, "取消");
            }
        });


    }

    //初始化组件
    private void initViews() {
        listView = (ListView)findViewById(R.id.list_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        add_imageButton = (ImageButton) findViewById(R.id.add_imageButton);
        mExplosionField = ExplosionField.attach4Window(this);

        //设置标题栏
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        add_imageButton.getBackground().setAlpha(0);
        add_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGoal();
            }
        });

    }

    public void get_data() throws ParseException {
        reset_ID_RestTimes_list=new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        itemList = new ArrayList<>();
        if(itemList.size()>0) {
            itemList.clear();
        }

        cursor =db.rawQuery("select *from "+dbHelper.TABLE_GOAL_NAME+" where U_name='"+dbHelper.who_is_online(db)+"'",null);

        if(cursor.moveToLast()) do {
            Item item = new Item();

            item.item_id = cursor.getString(cursor.getColumnIndex("_id"));
            item.item_u_name = cursor.getString(cursor.getColumnIndex("U_name"));
            item.item_content = cursor.getString(cursor.getColumnIndex("goal_content"));
            item.item_date1 = cursor.getString(cursor.getColumnIndex("date1"));
            item.item_goal_value = cursor.getString(cursor.getColumnIndex("goal_value"));
            item.item_goal_total_value = cursor.getString(cursor.getColumnIndex("goal_total_value"));

            item.item_goal_times = cursor.getString(cursor.getColumnIndex("goal_times"));
            item.item_finish_times = cursor.getString(cursor.getColumnIndex("finish_times"));
            item.item_rest_times = cursor.getString(cursor.getColumnIndex("rest_times"));
            item.item_repeat = cursor.getString(cursor.getColumnIndex("repeat"));

            item.item_sum_progress = Integer.parseInt(item.item_goal_total_value)*60;
            item.item_finish_progress = cursor.getInt(cursor.getColumnIndex("finish_progress"));
            item.item_last_reset_time = cursor.getString(cursor.getColumnIndex("last_reset_time"));

            //加判断
            String last_reset_time = item.item_last_reset_time;

            if (item.item_repeat.equals("当天任务")) {
                if (last_reset_time.compareTo(mDate.toString())<0) {
                    //delete();
                }else {
                    itemList.add(item);
                }

            }else if(item.item_repeat.equals("每日任务")) {
                System.out.println("\n上一次清零的时间"+last_reset_time+"与今日"+mDate.toString()+"相比较的结果："+last_reset_time.compareTo(mDate.toString()));
                if (last_reset_time.compareTo(mDate.toString())<0) {
                    item.item_finish_times="0";
                    item.item_rest_times=item.item_goal_times;
                    item.item_last_reset_time=mDate;
                    Map<String,Object> map=new HashMap<>();
                    map.put(dbHelper.ID,item.item_id);
                    map.put(dbHelper.REST_TIMES,item.item_rest_times);
                    reset_ID_RestTimes_list.add(map);
                }
                itemList.add(item);



            }else if (item.item_repeat.equals("每周任务")){
                test_data();
                if(last_reset_time.compareTo(mFirstDayOfWeek.toString())< 0){
                    item.item_finish_times="0";
                    item.item_rest_times=item.item_goal_times;
                    item.item_last_reset_time=mDate;
                    Map<String,Object> map=new HashMap<>();
                    map.put(dbHelper.ID,item.item_id);
                    map.put(dbHelper.REST_TIMES,item.item_rest_times);
                    reset_ID_RestTimes_list.add(map);
                }
                itemList.add(item);


            }else if(item.item_repeat.equals("工作日任务")){
                if(mDayOfWeek - 1 == 6 || mDayOfWeek - 1 == 7) {
                    System.out.println("\n今天是星期"+(mDayOfWeek - 1));
                }else {
                    System.out.println("\n上一次清零的时间"+last_reset_time+"与今日"+mDate.toString()+"相比较的结果："+last_reset_time.compareTo(mDate.toString()));
                    if (last_reset_time.compareTo(mDate.toString())<0) {

                        item.item_finish_times="0";
                        item.item_rest_times=item.item_goal_times;
                        item.item_last_reset_time=mDate;
                        Map<String,Object> map=new HashMap<>();
                        map.put(dbHelper.ID,item.item_id);
                        map.put(dbHelper.REST_TIMES,item.item_rest_times);
                        reset_ID_RestTimes_list.add(map);
                    }
                    itemList.add(item);
                }


            }

        } while (cursor.moveToPrevious());
        cursor.close();
        db.close();
        Refresh_Last_Reset_Time(reset_ID_RestTimes_list);

    }

    public void test_data() throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mDate));
        int d = 0;
        if(cal.get(Calendar.DAY_OF_WEEK)==1)
        {
            d = -6;
        }else{
            d = 2-cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);		//所在周开始日期

        mFirstDayOfWeek = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);		//所在周结束日期
        mLastDayOfWeek = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        System.out.println("\n4月1号所在的周的第一天为："+ mFirstDayOfWeek);
        System.out.println("\n4月1号所在的周的最后一天为："+ mLastDayOfWeek);
    }



    //新建
    public void newGoal() {
        Intent intent = new Intent();
        intent.setClass(TaskDisplayActivity.this, TaskEditActivity.class);
        startActivity(intent);
    }

    public void Refresh_Last_Reset_Time(List<Map<String,Object>> reset_ID_RestTimes_list){
        db=dbHelper.getWritableDatabase();
        for (int i = 0; i <reset_ID_RestTimes_list.size() ; i++) {
            String single_finish_time = "0";
            String last_reset_time = mDate;
            ContentValues values = new ContentValues();
            values.put("finish_times",single_finish_time);
            values.put("last_reset_time",last_reset_time);
            values.put(dbHelper.REST_TIMES,reset_ID_RestTimes_list.get(i).get(dbHelper.REST_TIMES).toString());
            System.out.println("\n刷新数据库中ID为:"+reset_ID_RestTimes_list.get(i).toString()+"的数据");
            db.update(dbHelper.TABLE_GOAL_NAME,values,"["+dbHelper.ID+"]=?"
                            +" and ["+dbHelper.U_name+"]=?",
                    new String[]{reset_ID_RestTimes_list.get(i).get(dbHelper.ID).toString(),dbHelper.who_is_online(db)});

        }
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_goal, menu);
        return true;
    }

    @Override
    public void onBackPressed() {//安卓后退键
        // TODO Auto-generated method stub
        finish();
        super.onBackPressed();
    }
    // 长按菜单响应函数
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                delete();
                break;

            case 1:
                Toast.makeText(TaskDisplayActivity.this,
                        "cancel",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void delete(){
        dbHelper = new MyDBHelper(TaskDisplayActivity.this);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        // String[] args = { itemList.get(index).item_id};

        // Cursor cursor =db.rawQuery("select *from "+dbHelper.TABLE_GOAL_NAME+" where U_name='"+dbHelper.who_is_online(db)+"'",null);
        //Cursor cursor=db.query("goal",null,"_id = ?",args,null,null,null);
        Cursor cursor=db.query("goal",null,"["+dbHelper.ID+"]=?"
                        +" and ["+dbHelper.U_name+"]=?",
                new String[]{itemList.get(index).item_id,dbHelper.who_is_online(db)} ,
                null,null,null);
        cursor.moveToNext();

        //db.delete("goal","_id = ?",args);//删除goal的内容
        db.delete("goal","["+dbHelper.ID+"]=?"
                        +" and ["+dbHelper.U_name+"]=?",
                new String[]{itemList.get(index).item_id,dbHelper.who_is_online(db)});

        itemList.remove(index);//在listview上删除
        test_adapter.notifyDataSetChanged();//刷新

        Toast.makeText(TaskDisplayActivity.this,
                "delete note successfully",
                Toast.LENGTH_SHORT).show();
        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.calender:
                Intent intentt = new Intent();
                intentt.setClass(TaskDisplayActivity.this, CalendarView_Activity.class);
                startActivity(intentt);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        try {
            get_data();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        test_adapter=new Test_Adapter(TaskDisplayActivity.this,itemList,this);
        listView.setAdapter(test_adapter);
        db=dbHelper.getWritableDatabase();
        super.onResume();
    }

    @Override
    public void explotion(int position, View view){
        mRestTimes = itemList.get(position).item_rest_times;
        if(Integer.valueOf(mRestTimes)>0) {
            mExplosionField.explode(view);
        }
    }

    @Override
    public void Enter_next(int position, boolean want_minus_times_of_item) {
        db=dbHelper.getWritableDatabase();

        if(want_minus_times_of_item){
            mRestTimes =itemList.get(position).item_rest_times;
            mFinishTimes = itemList.get(position).item_finish_times;
            mGoalTimes = itemList.get(position).item_goal_times;
            mGoalTotalValue = itemList.get(position).item_goal_total_value;
            mGoalValue = itemList.get(position).item_goal_value;
            cur_progress = itemList.get(position).item_finish_progress;
            mContent = itemList.get(position).item_content;
            if(Integer.valueOf(mRestTimes)>0){
                mFinishTimes = String.valueOf(Integer.valueOf(mFinishTimes)+1);
                mRestTimes = String.valueOf((Integer.valueOf(mGoalTimes)) -(Integer.valueOf( mFinishTimes)));
                itemList.get(position).Set_Finish_Times(mFinishTimes);
                itemList.get(position).Set_Rest_Times( mRestTimes);

                if(cur_progress<(Integer.parseInt(mGoalTotalValue)*60)) {
                    cur_progress = cur_progress + Integer.parseInt(mGoalValue);
                    itemList.get(position).Set_cur_progress(cur_progress);
                }else{
                    cur_progress =Integer.parseInt(mGoalValue);
                    itemList.get(position).Set_cur_progress(cur_progress);
                }


                test_adapter.notifyDataSetChanged();
                ContentValues values = new ContentValues();
                values.put("finish_times",mFinishTimes);
                values.put("rest_times",mRestTimes);
                values.put("finish_progress",cur_progress);
                db.update("goal",values,"["+dbHelper.ID+"]=?"
                                +" and ["+dbHelper.U_name+"]=?",
                        new String[]{itemList.get(position).item_id,dbHelper.who_is_online(db)});


                String mCurrentProgress = String.valueOf(Integer.parseInt(mFinishTimes)*Integer.parseInt(mGoalValue));
                String year = mDate.split("-")[0];
                String month = mDate.split("-")[1];
                String day = mDate.split("-")[2];

                ContentValues values1 = new ContentValues();
                values1.put("G_id",itemList.get(position).item_id);
                values1.put("U_name",itemList.get(position).item_u_name);
                values1.put("goal2_content",mContent);
                values1.put("current_year",year);
                values1.put("current_month",month);
                values1.put("current_day",day);
                values1.put("current_progress",mCurrentProgress);
                // String str="update "+dbHelper.TABLE_GOAL_NAME2+" set "+dbHelper.GET_GOAL_ID+"="+itemList.get(position).item_id+" and "+dbHelper.CURRENT_DATE+"="+mDate
                //       +" and "+dbHelper.CURRENT_PROGRESS+"="+mCurrentProgress+ " where "+dbHelper.GOAL2_ID+"='"+itemList.get(position).item_id+"' and "+dbHelper.CURRENT_DATE+"='"+mDate+"'";

                if(0==db.update(dbHelper.TABLE_GOAL_NAME2,values1,"["+dbHelper.GET_GOAL_ID+"]=?"
                                +" and ["+dbHelper.U_name+"]=?"
                                +" and ["+dbHelper.CURRENT_YEAR+"]=?"
                                +" and ["+dbHelper.CURRENT_MONTH+"]=?"
                                +" and ["+dbHelper.CURRENT_DAY+"]=?",
                        new String[]{itemList.get(position).item_id,dbHelper.who_is_online(db),year,month,day})){
                    values1.put(dbHelper.ID,dbHelper.Get_New_ID(db,dbHelper.TABLE_GOAL_NAME2));
                    db.insert(dbHelper.TABLE_GOAL_NAME2,null,values1);
                }


                // db.close();
                System.out.println("\n现在的剩余次数"+mRestTimes);

            }
        }
    }


}
