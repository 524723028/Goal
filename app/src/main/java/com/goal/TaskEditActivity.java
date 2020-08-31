package com.goal;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/11/3.
 */
public class TaskEditActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ProgressBar progressBar;
    private int cur_progress;
    private int sum_progress;
    private TextView tv_progress;

    private EditText et_content;
    private TextView tv_Date1Text,tv_GoalTotalValueText,tv_GoalValueText,tv_GoalTimesText,tv_RepeatTypeText;

    private int mYear1, mMonth1,mDay1;
    private Calendar mCalendar;
    private String mG_id,mUname;
    private String mContent;
    private String mDate1;
    private String mGoalTotalValue;
    private String mGoalValue;
    private String mGoalTimes;
    private String mRestTimes;
    private String mFinishTimes;
    private String mRepeatType;
    private String mLastResetTime;

    MyDBHelper dbHelper;
    SQLiteDatabase db;

    boolean is_old=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        dbHelper = new MyDBHelper(TaskEditActivity.this);

        //或许可以移到InitView里面
        mCalendar = Calendar.getInstance();
        mYear1 = mCalendar.get(Calendar.YEAR);
        mMonth1 = mCalendar.get(Calendar.MONTH);
        mDay1 = mCalendar.get(Calendar.DAY_OF_MONTH);
        InitView();

        final Intent intent = getIntent();
        if(null!=intent.getStringExtra("goal_id")) {
            is_old = true;
            GetOldData();
        }else{
            //这里设置默认值
            mGoalTotalValue = Integer.toString(200);
            mGoalValue = Integer.toString(30);
            mGoalTimes = Integer.toString(1);
            mFinishTimes = Integer.toString(0);
            mRepeatType = "每日任务";
            mDate1 = mYear1 + "-" + dbHelper.Time_Format(String.valueOf(mMonth1 + 1)) + "-" + dbHelper.Time_Format(String.valueOf(mDay1));
            tv_Date1Text.setText(mDate1);
            tv_GoalTotalValueText.setText(mGoalTotalValue);
            tv_GoalValueText.setText(mGoalValue);
            tv_GoalTimesText.setText(mGoalTimes);
            tv_RepeatTypeText.setText(mRepeatType);
        }

        //设置标题栏
        setSupportActionBar(mToolbar);
        if(is_old){
            getSupportActionBar().setTitle(R.string.title_activity_edit_goal);
        }else {
            getSupportActionBar().setTitle(R.string.title_activity_add_goal);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        sum_progress = (Integer.parseInt(tv_GoalTotalValueText.getText().toString())) * 60;
        float f= (float) (1.0*cur_progress/sum_progress*100);
        progressBar.setProgress((int) f);
        tv_progress.setText(cur_progress+"/"+sum_progress);

    }

    public void GetOldData() {
        final Intent intent = getIntent();
        mG_id = intent.getStringExtra("goal_id");
        mUname = intent.getStringExtra("U_name");
        mRestTimes = intent.getStringExtra("rest_times");
        mFinishTimes = intent.getStringExtra("finish_times");
        cur_progress = intent.getIntExtra("cur_progress",0);
        mLastResetTime = intent.getStringExtra("last_reset_time");
        et_content.setText(intent.getStringExtra("goal_content"));
        tv_Date1Text.setText(intent.getStringExtra("date1"));
        tv_GoalTotalValueText.setText(intent.getStringExtra("goal_total_value"));
        tv_GoalValueText.setText(intent.getStringExtra("goal_value"));
        tv_GoalTimesText.setText(intent.getStringExtra("goal_times"));
        tv_RepeatTypeText.setText(intent.getStringExtra("repeat"));
    }

    public void SetDefaultValue() {
        //这里设置默认值

    }


    public void InitView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        tv_progress=(TextView)findViewById(R.id.progress_text);
        et_content = (EditText)findViewById(R.id.et_content);
        tv_Date1Text = (TextView) findViewById(R.id.set_date1);
        tv_GoalTotalValueText = (TextView) findViewById(R.id.set_goal_total_value);
        tv_GoalValueText = (TextView) findViewById(R.id.set_goal_value);
        tv_GoalTimesText = (TextView) findViewById(R.id.set_times_value);
        tv_RepeatTypeText = (TextView) findViewById(R.id.set_repeat_type);




    }

    public void setDate1(View v){
        new DatePickerDialog(TaskEditActivity.this,
                new DatePickerDialog.OnDateSetListener() {//监听器
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {//监听选中的年月日
                        TaskEditActivity.this.mYear1 = year;//把最初int的year改成选中的年份
                        TaskEditActivity.this.mMonth1 = monthOfYear;
                        TaskEditActivity.this.mDay1 = dayOfMonth;
                        tv_Date1Text.setText(mYear1 + "-" + (mMonth1 + 1) + "-" + mDay1);
                    }
                }, mYear1, mMonth1, mDay1).show();//初始年月日
    }

    public void goalTotalValue(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("设定任务总时长（小时）");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().length() == 0) {
                            final Intent intent = getIntent();
                            //if (null != itent.)
                            Toast.makeText(TaskEditActivity.this,"数值无效",Toast.LENGTH_SHORT).show();
                        }

                        else {
                            mGoalTotalValue = input.getText().toString().trim();
                            tv_GoalTotalValueText.setText(mGoalTotalValue);
                            //刷新
                            sum_progress = (Integer.parseInt(tv_GoalTotalValueText.getText().toString())) * 60;
                            float f= (float) (1.0*cur_progress/sum_progress*100);
                            progressBar.setProgress((int) f);
                            tv_progress.setText(cur_progress+"/"+sum_progress);

                        }
                    }
                });
        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    public void goalValue(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("设定每次任务时长（分钟）");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            Toast.makeText(TaskEditActivity.this,"数值无效",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            mGoalValue = input.getText().toString().trim();
                            tv_GoalValueText.setText(mGoalValue);
                        }
                    }
                });
        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    public void goalTimes(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("设定任务数量");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0||input.getText().toString().length()>3) {
                            final Intent intentt = getIntent();
                            Toast.makeText(TaskEditActivity.this,"数值无效",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mGoalTimes = input.getText().toString().trim();
                            tv_GoalTimesText.setText(mGoalTimes);
                        }
                    }
                });
        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    public void selectRepeatType(View v){
        final String[] items = new String[4];

        items[0] = "当天任务";
        items[1] = "每日任务";
        items[2] = "每周任务";
        items[3] = "工作日任务";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择周期");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mRepeatType = items[item];
                tv_RepeatTypeText.setText(mRepeatType);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void saveGoal() {
        db = dbHelper.getWritableDatabase();
        final String[] goal_id = {mG_id};

        mContent = et_content.getText().toString();
        mDate1 = mYear1 + "-" + dbHelper.Time_Format(String.valueOf(mMonth1 + 1)) + "-" +dbHelper.Time_Format(String.valueOf(mDay1)) ;
        mGoalTotalValue = tv_GoalTotalValueText.getText().toString();
        mGoalValue = tv_GoalValueText.getText().toString();
        mGoalTimes = tv_GoalTimesText.getText().toString();
        mRepeatType = tv_RepeatTypeText.getText().toString();

        if(Integer.parseInt(mGoalTimes)>Integer.parseInt(mFinishTimes)) {
            mRestTimes = String.valueOf((Integer.valueOf(mGoalTimes)) - (Integer.valueOf(mFinishTimes)));
        }else {
            mRestTimes ="0";
        }

        ContentValues values = new ContentValues();
        values.put("goal_content",mContent);
        values.put("date1",mDate1);
        values.put("goal_total_value",mGoalTotalValue);
        values.put("goal_value",mGoalValue);
        values.put("goal_times",mGoalTimes);
        values.put("rest_times",mRestTimes);
        values.put("finish_times",mFinishTimes);
        values.put("repeat",mRepeatType);
        values.put("finish_progress",cur_progress);

        if(is_old) {
            values.put("last_reset_time" , mLastResetTime);
            db.update("goal",values,"["+dbHelper.ID+"]=?"+" and ["+dbHelper.U_name+"]=?",
                    new String[]{mG_id,mUname});
        }else {
            //这个地方的mdate1还需要重新连接吗？
            mDate1 = mYear1 + "-" + dbHelper.Time_Format(String.valueOf(mMonth1 + 1)) + "-" +dbHelper.Time_Format(String.valueOf(mDay1)) ;
            values.put("last_reset_time",mDate1);
            values.put(dbHelper.U_name,dbHelper.who_is_online(db));
            values.put(dbHelper.ID,dbHelper.Get_New_ID(db,"goal"));
            db.insert(dbHelper.TABLE_GOAL_NAME,null,values);

        }
        db.close();;
        finish();;

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save_goal:
                if (et_content.getText().toString().equals("")||et_content.getText().length()>8) {
                    Toast.makeText(TaskEditActivity.this, "任务名称的长度应为1-8", Toast.LENGTH_SHORT).show();
                    et_content.setError("任务名称的长度应为1-8");
                    et_content.setText("");
                }else {
                    saveGoal();
                }

                return true;


            case R.id.discard_goal:

                db=dbHelper.getWritableDatabase();
                if(db.delete("goal","["+dbHelper.ID+"]=?"+" and ["+dbHelper.U_name+"]=?",
                        new String[]{mG_id,mUname})>0){
                    if(db.delete(dbHelper.TABLE_GOAL_NAME2,"  ["+dbHelper.U_name+"]=?"+" and ["+dbHelper.GET_GOAL_ID+"]=?",
                            new String[]{mUname,mG_id})>0){
                    }
                    Toast.makeText(getApplicationContext(), "删除成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "删除失败",Toast.LENGTH_SHORT).show();
                }

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
