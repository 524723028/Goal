package com.goal.Calender;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.goal.MyDBHelper;
import com.goal.R;
import com.goal.util.CalendarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * @version: V1.0
 */
public class CalendarGridViewAdapter extends BaseAdapter {

	private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
	private Calendar calSelected = Calendar.getInstance(); // 选择的日历
	private String mDate;
	private MyDBHelper dbHelper;

	public void setSelectedDate(Calendar cal) {
		calSelected = cal;
	}

	private Calendar calToday = Calendar.getInstance(); // 今日
	private int iMonthViewCurrentMonth = 0; // 当前视图月

	// 根据改变的日期更新日历
	// 填充日历控件用
	private void UpdateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月

		// 星期一是2 星期天是1 填充剩余天数
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);

		calStartDate.add(Calendar.DAY_OF_MONTH, -1);// 周日第一位

	}

	ArrayList<Map<String, Object>> titles;

	private ArrayList<Map<String,Object>> getDates() {

		UpdateStartDateForMonth();
		MyDBHelper dbHelper;

		ArrayList<Map<String,Object>> alArrayList = new ArrayList<>();

		boolean begin=false;
		boolean end=false;

		for (int i = 1; i <= 42; i++) {

			int dayofmonth=calStartDate.get(Calendar.DAY_OF_MONTH);
			int leng=getdaysInMonth(calStartDate.get(Calendar.MONTH)+1,calStartDate.get(Calendar.YEAR));
			if(dayofmonth==1&&!end){
				begin=true;
				if(i<=7){
					for (int j = 1; j < i; j++) {
						Map<String,Object> map=new HashMap<>();
						map.put("Date","");
						map.put("is_display","false");
						alArrayList.add(map);
					}

				}


			}
			if(dayofmonth>=1&&begin&&!end){

				Map<String,Object> map=new HashMap<>();
				map.put("Date",calStartDate.get(Calendar.YEAR)+"-"+(calStartDate.get(Calendar.MONTH)+1)+"-"+calStartDate.get(Calendar.DAY_OF_MONTH));
				map.put("is_display","true");
				alArrayList.add(map);
				System.out.println("\nadd:i"+i+"|"+calStartDate.toString()+"|"+calStartDate.getTime()+"|"+ Calendar.DAY_OF_MONTH+"|"+calStartDate.getTime().getDay()+"[dayofmonth]"+dayofmonth);
				if(dayofmonth==leng){
					end=true;
					calStartDate.add(Calendar.DAY_OF_MONTH, 1);



				}
			}
			if(begin&&end){
				for (int j = i+1; j <= 42; j++) {
					Map<String,Object> map=new HashMap<>();
					map.put("Date","");
					map.put("is_display","false");
					alArrayList.add(map);
					System.out.println("\n长度为："+alArrayList.size());
					return alArrayList;
					//Date d=new Date();
					//alArrayList.add(d.getTime());
				}
			}

			calStartDate.add(Calendar.DAY_OF_MONTH, 1);
		}

		return alArrayList;
	}

	public int getdaysInMonth(int month,int year){
		int temp_month=parseInt(String.valueOf(month),10)+1;
		Date temp=new Date(year+"/"+temp_month+"/0");
		//System.out.println("\n想要获知该年该月的总天数："+temp.getDate());
		return temp.getDate();
	}

	private Activity activity;
	Resources resources;

	// construct
	public CalendarGridViewAdapter(Activity a, Calendar cal) {
		calStartDate = cal;
		activity = a;
		resources = activity.getResources();
		titles = getDates();
		dbHelper=new MyDBHelper(activity.getBaseContext());
	}

	public CalendarGridViewAdapter(Activity a) {
		activity = a;
		resources = activity.getResources();
	}

	@Override
	public int getCount() {
		return titles.size();
	}

	@Override
	public Object getItem(int position) {
		return titles.get(position).get("Date").toString();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        int mYear=calStartDate.get(Calendar.YEAR);
        int mMonth=calStartDate.get(Calendar.MONTH);

		LinearLayout iv = new LinearLayout(activity);
		iv.setId(position + 5000);
		iv.setBackgroundColor((resources.getColor(R.color.Touming)));
		LinearLayout imageLayout = new LinearLayout(activity);
		imageLayout.setOrientation(LinearLayout.HORIZONTAL);
		iv.setGravity(Gravity.CENTER);
		iv.setOrientation(LinearLayout.VERTICAL);

		iv.setBackgroundColor(resources.getColor(R.color.Touming));
		if(titles.get(position).get("is_display").toString().equals("false")){
			TextView txtToDay = new TextView(activity);// 日本老黄历
			txtToDay.setGravity(Gravity.CENTER_HORIZONTAL);
			// 日期开始
			TextView txtDay = new TextView(activity);// 日期
			txtDay.setGravity(Gravity.CENTER_HORIZONTAL);
			LayoutParams lp = new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			iv.addView(txtDay, lp);

			LayoutParams lp1 = new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			iv.addView(txtToDay, lp1);
		}else{



			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

			Date myDate = null;//new Date(String.valueOf(getItem(position))) ;
			try {
				myDate = sdf.parse(String.valueOf(getItem(position)));

			} catch (ParseException e) {
				System.out.println("\n在214行捕捉到异常");
				e.printStackTrace();
			}
			Calendar calCalendar = Calendar.getInstance();
			calCalendar.setTime(myDate);

			final int iMonth = calCalendar.get(Calendar.MONTH);
			final int iDay = calCalendar.get(Calendar.DAY_OF_WEEK);

			TextView txtToDay = new TextView(activity);// 日本老黄历
			txtToDay.setGravity(Gravity.CENTER_HORIZONTAL);
			txtToDay.setTextSize(9);
			CalendarUtil calendarUtil = new CalendarUtil(calCalendar);
			if (equalsDate(calToday.getTime(), myDate)) {
				// 当前日期
				iv.setBackgroundColor(resources.getColor(R.color.white));//iv.setBackgroundColor(resources.getColor(R.color.event_center));
				txtToDay.setText(calendarUtil.toString());
			}

				// 设置背景颜色
				if (equalsDate(calSelected.getTime(), myDate)) {
					// 选择的
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						iv.setBackgroundResource(R.drawable.dot);
					} else {
						iv.setBackgroundColor(resources.getColor(R.color.selection));
					}
				} else {
					if (equalsDate(calToday.getTime(), myDate)) {
						// 当前日期
						iv.setBackgroundColor(resources.getColor(R.color.Touming));
					}
				}

				// 设置背景颜色结束

				// 日期开始
				TextView txtDay = new TextView(activity);// 日期
				txtDay.setGravity(Gravity.CENTER_HORIZONTAL);

				// 判断是否是当前月
				if (iMonth == iMonthViewCurrentMonth) {
					int day = myDate.getDate(); // 日期


					//mDate = mYear+"-"+dbHelper.Time_Format(String.valueOf(mMonth))+"-"+dbHelper.Time_Format(String.valueOf(day));
					//System.out.println("\n现在是:"+mDate +has_entry(mDate));


					txtDay.setText(String.valueOf(day));
					txtDay.setGravity(Gravity.CENTER);
					txtDay.setId(position + 500);
					iv.setTag(myDate);
					int day0=day;

					if(has_entry(mYear,mMonth,day0) ){
						txtDay.setTextColor(activity.getResources().getColorStateList(R.color.title_text_6));
					}else{
						//System.out.println("\nposition:"+position+"不是偶数");
						txtDay.setTextColor(resources.getColor(R.color.Text));
					}


					/*
					int day0=day;
					if(has_entry(year,month,day0)){			//判断该天是否有财务记录
						txtDay.setTextColor(activity.getResources().getColorStateList(R.color.title_text_6));
					}else{
						txtDay.setTextColor(resources.getColor(R.color.Text));
					}
	*/				//获知屏幕的宽度和高度
					DisplayMetrics displayMetrics = new DisplayMetrics();
					activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
					int width = displayMetrics.widthPixels;


					LayoutParams lp = new LayoutParams(
							LayoutParams.FILL_PARENT, width / 7);
					iv.addView(txtDay, lp);

					LayoutParams lp1 = new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				}


		}
		return iv;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	private Boolean equalsDate(Date date1, Date date2) {

		if (date1.getYear() == date2.getYear()
				&& date1.getMonth() == date2.getMonth()
				&& date1.getDate() == date2.getDate()) {
			return true;
		} else {
			return false;
		}

	}

	//处理数据
	public boolean has_entry(int year,int month,int day){
		boolean temp=false;
		MyDBHelper myDBHelper=new MyDBHelper(activity);
		SQLiteDatabase db=myDBHelper.getReadableDatabase();
		String str="select * from "+myDBHelper.TABLE_GOAL_NAME2+" where "
				+myDBHelper.CURRENT_YEAR +" = '"+year+"' and "
				+myDBHelper.CURRENT_MONTH + "= '"+dbHelper.Time_Format(String.valueOf(month))+"' and "
				+myDBHelper.CURRENT_DAY + "= '"+dbHelper.Time_Format(String.valueOf(day))+"'";
		Cursor c=null;
		try{
			c=db.rawQuery(str,null);
		}catch (Exception e){
			System.out.println("\n异常:"+e.toString());
		}
		//System.out.println("\n"+"匹配个数"+c.getCount());
		if(c!=null){
			if(c.getCount()>0){
				temp=true;
			}
		}
		c.close();
		db.close();
		return temp;
	}


}
