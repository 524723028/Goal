package com.goal;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

// import com.example.administrator.menu_test.Utils.Category_Utils;


/**
 * Created by Administrator on 2016/12/3.
 */
public class MyDBHelper extends SQLiteOpenHelper {
    Context context;

    //数据库的名字
    static String DB_name="MY.db";

    /**
     * 数据库里每条信息都应带有的字段:U_name和_id
     * 其中U_name为用户名，_id为记录的唯一标识，不自增
     * */
    public final String U_name="U_name";
    public final String ID="_id";



    //表0
    public final String TABLE0_PWD="PWD";
    public final String PWD="pwassord";
    public final String IS_OPEN="is_open";
    public final String PWD_ID="_id";
    public  final String IMG_ID="img_id";
    public final String PWD_U_NAME="U_name";

    final String create_tablw_pwd="create table "+TABLE0_PWD+"("+ID+" int not null,"+U_name+" text,"
            +PWD+" text,"+IS_OPEN+" text,"+IMG_ID+" text"+")";

    //表1.存储用户的用户名以及密码,用户名一旦确定，便再也不能修改
    public final String TABLE_USER="user";
    public final String U_PASSWORD="U_password";
    public final String U_ID="_id";    //pk
    public final String U_OnLine="is_online";

    final String create_table1_user="create table "+TABLE_USER+"("+U_name+" text not null, "+U_OnLine+" text," +U_PASSWORD+" text not null,"
            +ID+" integer  not null)";
    /***
     * 记事本模块的表格
     * **/

    //表2记事的种类
    public final String TABLE_FOLDER="folder";//表名
    public final String FOLDER_ID="_id";   //pk
    public final String FODER_UID="U_id";  //pk,fk
    public final String F_NAME="F_name";   //种类名称

    final String create_table2_folder="create table "+TABLE_FOLDER+"("+ID+" int not null,"+U_name+" text,"+F_NAME+" text,"
            +FODER_UID+" int)";



    //表3 照片
    public final String TABLE_PICTURE="Picture";//表名
    public final String PICTURE_ID="_id";      //pk
    public final String PICTURE_DATA="P_address";
    public final String ENTITY_ID = "e_id";

    final String create_table3_Picture="create table "+TABLE_PICTURE+"("+ID+" int not null,"+U_name+" text,"+PICTURE_DATA
            +" blob not null,"+ENTITY_ID+" int)";

    //表4 附件
    public final String TABLE_ATTACHMENT="Attachment"; //表名
    // final String ATTACH_ID="_id";               //pk
    public final String ATTACH_ADDRESS="Attach_address";

    final String create_table4_Attachment="create table "+TABLE_ATTACHMENT+"("+ID+" int not null,"+U_name+" text,"+ATTACH_ADDRESS
            +" text not null)";

    //表5记录
    public final String TABLE_ENTITY="entity"; //表名
    public final String Entity_UID="U_id"; //  pk,fk
    public final String Entity_FID="F_id"; //fk
    public final String N_ID="_id";        //pk
    public final String TITLE="Title";     //可为空
    public final String C_TIME="C_Time";   //记录的最后一次编辑保存的时间
    public final String A_ID="A_id";       //可为空，每条记录最多设置一个提醒施加，但提醒时间可以后期继续修改
    public final String CONTENT="Content"; //记事的主体文本内容，可以为空
    public final String P_IDS="P_ids";     //使用连字符"-"将记事相关的图片的ID连接起来
    public final String ATTACH_IDS="Attach_ids";//使用连字符"-"将记事相关的附件的ID连接起来


    public final String IS_FOLDER = "is_folder";
    public final String PARENT_FOLDER = "parent_folder";
    public final String BACKGROUND_COLOR = "background_color";
    public final String UPDATE_DATE = "update_date";
    public final String UPDATE_TIME = "update_time";


    final String create_table5_Entity="create table "+TABLE_ENTITY+"("+ID+" int not null,"+U_name+" text,"
            +Entity_UID+" int,"+Entity_FID+" int,"+TITLE+" text,"+C_TIME+" text,"+A_ID+" text,"+CONTENT+" text,"
            +P_IDS+" text,"+ATTACH_IDS+" text," +BACKGROUND_COLOR+" text,"
            +"foreign key("+Entity_FID+") references "+TABLE_FOLDER
            +"("+ID+"),"+"foreign key("+Entity_UID+") references "+TABLE_USER+"("+ID+"))";




    //表6闹钟
    public final String TABLE_ALARM="Alarm";   //表名
    public final String Alarm_ID="_id";        //pk,闹钟的唯一标识
    public final String Alarm_TIME="A_Time";   //设置的闹钟时间
    public final String ALarm_E_ID="Alarm_Entity_id";  //闹钟需要提醒的记事
    final String create_table6_Alarm="create table "+TABLE_ALARM+"("+ID+" int not null,"+U_name+" text,"+Alarm_TIME+" text not null,"
            +ALarm_E_ID+" int ,"+"foreign key("+ALarm_E_ID+")references "+TABLE_ENTITY+"("+ID+"))";


    /***
     * 理财模块的表格
     * */

    //表7 短信拦截 MSM
    public final String TABLE_MSM="MSM";           //表名
    public final String MSM_ID="_id";              //pk
    public final String MSM_UID="U_id";            //pk,fk
    public final String MSM_CONTENT="Msm_content"; //短信的主体内容
    public final String MSM_MONEY="money";
    public final String MSM_YEAR_MONTH="year_month";
    public final String MSM_DAY="day";
    public final String MSM_HOUR_MINUTE="hour_minute";
    public final String MSM_TIME="Msm_time";       //收到短信的时间
    public final String MSM_CATEGORY="Msm_category";     //用来区分它是属于收入(值为0)还是支出(值为1)
    public final String MSM_PHONENUMBER="Msm_phone_number";

    /* final String create_table7_MSM="create table "+TABLE_MSM+"("+MSM_ID+" integer  primary key autoincrement not null, "
             +MSM_UID+" int,"+MSM_CONTENT+" text,"+MSM_TIME+" text,"+MSM_CATEGORY+"int ,"+"foreign key("+MSM_UID+")" +
             "references "+TABLE_USER+"("+U_ID+"))";*/
    final String create_table7_MSM="create table "+TABLE_MSM+"("+ID+" int not null,"+U_name+" text,"
            +MSM_CONTENT+" text,"+MSM_TIME+" text,"+MSM_MONEY+" text,"+MSM_CATEGORY+" text,"+MSM_PHONENUMBER+" text,"+MSM_YEAR_MONTH+" text,"+MSM_DAY+" text,"+MSM_HOUR_MINUTE+" text"+")";

    //表8 支出的额度限制
    public final String TABLE_MONEY="Money";       //表名
    public final String YEAR="year";
    public final String MONTH="month";
    public final String MONEY_UID="U_id";      //pk,fk
    public final String MONEY_ID="_id";        //PK
    public final String MONEY="money";         //额度

    final String create_table8_Money="create table "+TABLE_MONEY+"("+ID+" int not null,"+U_name+" text,"
            +MONEY_UID+" int, "+YEAR+" text,"+MONTH+" text,"+MONEY+" int,"+"foreign key("+MONEY_UID+")" +
            "references "+TABLE_USER+"("+ID+"))";

    //表9 银行卡
    public final String TABLE_CARD="card";     //表名
    public final String CARD_UID="U_id";       //pk.fk
    public final String CARD_ID="_id";         //pk
    public final String NUMBER="number";       //卡号
    public final String BANK_NAME="bank_name";   //银行卡的所属

    final String create_table9_card="create table "+TABLE_CARD+"("+ID+" int not null,"+U_name+" text,"
            +CARD_UID+" int, "+NUMBER+" text,"+MONTH+" text,"+BANK_NAME+" text,"+"foreign key("+CARD_UID+")" +
            "references "+TABLE_USER+"("+ID+"))";


    //表10 每一笔账的种类
    public final String TABLE_CATEGORY="category";     //表名
    public final String CATEGORY_ID="_id";             //PK
    public final String CATEGORY_UID="U_id";           //pk.fk
    public final String CATEGORY_judge="judge";        //为0时，表示收入，为1时，表示输出
    public final String CATEGORY_NAME="category_name"; //种类名称

    final String create_table10_category="create table "+TABLE_CATEGORY+"("+ID+" int not null,"+U_name+" text,"
            +CATEGORY_UID+" int, "+CATEGORY_judge+" int,"+CATEGORY_NAME+" text ,"+"foreign key("+CATEGORY_UID+")" +
            "references "+TABLE_USER+"("+ID+"))";

    //表11  收入
    public final String TABLE_INCOME="income";         //表名
    public final String INCOME_ID="_id";               //pk
    public final String INCOME_UID="U_id";             //pk,fk
    public final String INCOME_category_ID="category_id";//fk
    public final String INCOME_info="infomation";      //与收入相关的备注信息
    public final String INCOME_Year_Month="year_month";
    public final String INCOME_Day="day";
    public final String INCOME_HOUR_MINUTE="hour_minute";
    public final String INCOME_MONEY="income_money";
    public final String INPUT_ADDRESS="address";      //消费地址，保留开发

    final String create_table11_income="create table "+TABLE_INCOME+"("+ID+" int not null,"+U_name+" text,"
            +INCOME_UID+" int, "+INCOME_category_ID+" int,"+INCOME_info+" text,"+INCOME_Day+" text,"
            +INCOME_Year_Month+" text,"+INCOME_HOUR_MINUTE+" text,"+INPUT_ADDRESS+" text,"+INCOME_MONEY+" text,"+"foreign key ("+INCOME_category_ID+")"+"REFERENCES "+TABLE_CATEGORY
            +"("+ID+"),foreign key("+INCOME_UID+")" +
            "references "+TABLE_USER+"("+ID+"))";

    //表12 支出
    public final String TABLE_OUTPUT="output";         //表名
    public final String OUTPUT_ID="_id";               //pk
    public final String OUTPUT_UID="U_id";             //pk,fk
    public final String OUTPUT_category_ID="category_id";
    public final String OUTPUT_info="information";
    public final String OUTPUT_Year_Month="year_month";
    public final String OUTPUT_Day="day";
    public final String OUTPUT_HOUR_MINUTE="hour_minute";
    public final String OUTPUT_MONEY="output_money";
    public final String OUTPUT_ADDRESS="address";      //消费地址，保留开发

    final String create_table12_output="create table "+TABLE_OUTPUT+"("+ID+" int not null,"+U_name+" text,"
            +OUTPUT_UID+" int, "+OUTPUT_category_ID+" int,"+OUTPUT_info+" text,"+OUTPUT_Day+" text,"
            +OUTPUT_Year_Month+" text,"+OUTPUT_HOUR_MINUTE+" text,"+OUTPUT_ADDRESS+" text,"+OUTPUT_MONEY+" text,"+"foreign key ("+OUTPUT_category_ID+")"+"REFERENCES "+TABLE_CATEGORY
            +"("+ID+"),foreign key("+OUTPUT_UID+")" +
            "references "+TABLE_USER+"("+ID+"))";

    //表13课程
    public final String TABLE_CLASSES="user_classes";
    public final String Class_Name="class_name";//课程的名字
    public final String Class_Id="_classid";//课程ID
    public final String CLASSES_UID="U_id";            //保留
    public final String Class_Info="class_info";//与课程相关的信息，比如单周或双周

    final String create_table13_classes="create table "+TABLE_CLASSES+"("+Class_Name+" text,"+CLASSES_UID+" int,"+ID+" int not null,"+U_name+" text,"+Class_Info+" text,"+"foreign key("+CLASSES_UID+")" +
            "references "+TABLE_USER+"("+ID+")" +
            ")";

    //表14 课程表
    public final String TABLE_CLASS="user_class";      //表名
    public final String U_C_ID="_u_c_id";
    public final String CLASS_ID="_classid";           //课程id
    public final String CLASS_UID="U_id";            //保留
    public final String CLASS_FINISH="is_finish";      //是否已经结课
    public final String CLASS_SORT_WEEK="class_week";       //周几：1-7
    public final String CLASS_SORT_NUMBER="class_number";       //第几节课：1-12
    public final String CLASS_ADDRESS="class_address";  //上课地点
    public final String CLASS_TIME="class_time";       //上课时间

    final String create_table14_classes="create table "+TABLE_CLASS+"("+ID+" int not null,"+U_name+" text,"+CLASS_ID+" integer,"
            +CLASS_FINISH+" text,"+CLASS_SORT_WEEK+" text,"+CLASS_SORT_NUMBER+" text,"+CLASS_ADDRESS+" text,"+CLASS_TIME+"  text,"
            +"foreign key ("+CLASS_ID+")"+"references "+TABLE_CLASSES+"("+ID+")"+")";



    //收支种类表
    public final String TABLE_IMAGE_CATEGORY="mage_category";
    public final String IMAGE_CATEGORY_UID="u_id";
    public final String IMAGE_CATEGORY_ID="image_id";
    public final String IMAGE_POSITION="image_position";
    public final String IMAGE_COLOR_POSITION="image_color_pposition";
    public final String IMAGE_CATEGORY_NAME="name";
    public final String IMAGE_CATEGORY_INOUT="is_in";      //1为收入，0表支出

    final String create_category_table="create table "+TABLE_IMAGE_CATEGORY+"("+ID+" int not null,"+U_name+" text,"
            +IMAGE_POSITION+" integory,"+IMAGE_CATEGORY_NAME+" text,"+IMAGE_CATEGORY_INOUT+" text,"+IMAGE_COLOR_POSITION+" integory,"+IMAGE_CATEGORY_UID+" integory,"+
            "foreign key ("+IMAGE_CATEGORY_UID+")"+"references "+TABLE_USER+"("+ID+")"+")";


    //表收支
    public final String TABLE_FINANCE="Finance";
    public final String F_CATEGORY_ID="category";        //0为收入，1为支出
    public final String F_MONEY="money";               //金额
    public final String F_INFO="info";                 //备注
    public final String F_TIME_YEAR="year";
    public final String F_TIME_MONTH="month";
    public final String F_TIME_DAY="day";
    public final String F_TIME_HOUR_MINUTE="hour_minute";
    public final String F_ID="_id";
    public final String F_U_ID="u_id";

    public final String create_table_finance="create table "+TABLE_FINANCE+"("+ID+" int not null,"+U_name+" text,"+F_U_ID+" integer,"+F_CATEGORY_ID+" text," +
            F_MONEY+" text,"+F_INFO+" text,"+F_TIME_YEAR+"  text,"+F_TIME_MONTH+" text,"+F_TIME_DAY+" text,"
            +F_TIME_HOUR_MINUTE+" text,"+"foreign key ("+F_U_ID+")"+"references "+TABLE_USER+"("+ID+")"+")";


    public  final String TABLE_BUDGET="budget"; //预算表
    public final String B_U_ID="u_id";
    public final String B_ID="_id";
    public final String B_MONEY="money";

    final String create_table_budget="create table "+TABLE_BUDGET+"("+ID+" int not null,"+U_name+" text,"+B_U_ID+" integer,"+B_MONEY+" text,"+"foreign key ("+B_U_ID+")"+"references "+TABLE_USER+"("+ID+")"+")";



    //任务管理表
    public final String TABLE_GOAL_NAME="goal";
   // public final String GOAL_ID="_id";

    //public final String GOAL_UID="u_id";
    public final String GOAL_CONTENT="goal_content";
    public final String DATE1="date1";
    public final String GOAL_TOTAL_VALUE="goal_total_value";
    public final String GOAL_VALUE="goal_value";
    public final String REPEAT="repeat";
    public final String GOAL_TIMES="goal_times";
    public final String FINISH_TIMES="finish_times";
    public final String REST_TIMES="rest_times";
    public final String FINISH_PROGRESS="finish_progress";
    public final String LAST_RESET_TIME = "last_reset_time";

    public final String TABLE_GOAL_NAME2 = "goal2";
    //public final String GOAL2_ID = "_id";
    public final String GET_GOAL_ID = "G_id";
    public final String GOAL2_CONTENT = "goal2_content";
    public final String CURRENT_YEAR = "current_year";
    public final String CURRENT_MONTH = "current_month";
    public final String CURRENT_DAY = "current_day";
    public final String CURRENT_PROGRESS = "current_progress";

    public final String create_table_goal="create table "+TABLE_GOAL_NAME+"("+ID+" int not null,"+U_name+" text,"
            +GOAL_CONTENT+" text,"
            +DATE1+" text,"
            +GOAL_TOTAL_VALUE+" text,"
            +GOAL_VALUE+" text,"
            +REPEAT+" text,"
            +GOAL_TIMES+" text,"
            +FINISH_TIMES+" text,"
            +REST_TIMES+" text,"
            +FINISH_PROGRESS+" integer,"
            +LAST_RESET_TIME+" text"+")";

    public final String create_table_goal2="create table "+TABLE_GOAL_NAME2+"("+ID+" int not null,"+U_name+" text,"
            +GET_GOAL_ID+" text,"
            +GOAL2_CONTENT+" text,"
            +CURRENT_YEAR+" text,"
            +CURRENT_MONTH+" text,"
            +CURRENT_DAY+" text,"
            +CURRENT_PROGRESS+" text"+")";

    public MyDBHelper(Context context) {
        super(context, MyDBHelper.DB_name, null, 3);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库中的表格
        sqLiteDatabase.execSQL(create_tablw_pwd);
        sqLiteDatabase.execSQL(create_table1_user);
        sqLiteDatabase.execSQL(create_table2_folder);
        sqLiteDatabase.execSQL(create_table3_Picture);
        sqLiteDatabase.execSQL(create_table4_Attachment);
        sqLiteDatabase.execSQL(create_table5_Entity);
        sqLiteDatabase.execSQL(create_table6_Alarm);
        sqLiteDatabase.execSQL(create_table7_MSM);
        sqLiteDatabase.execSQL(create_table8_Money);
        sqLiteDatabase.execSQL(create_table9_card);
        sqLiteDatabase.execSQL(create_table10_category);
        sqLiteDatabase.execSQL(create_table11_income);
        sqLiteDatabase.execSQL(create_table12_output);
        sqLiteDatabase.execSQL(create_table13_classes);
        sqLiteDatabase.execSQL(create_table14_classes);

        sqLiteDatabase.execSQL(create_category_table);
        sqLiteDatabase.execSQL(create_table_finance);
        sqLiteDatabase.execSQL(create_table_budget);
        //init_image_category(sqLiteDatabase,"ALice");     //初始化种类表

        init_cpwd(sqLiteDatabase);
        init_category(sqLiteDatabase);
        init_tables(sqLiteDatabase,"ALice");   //初始化课表
        Init_User(sqLiteDatabase);
        sqLiteDatabase.execSQL(create_table_goal);
        sqLiteDatabase.execSQL(create_table_goal2);
        System.out.println("数据库创建成功了");
        //set_test_datas(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        System.out.println("数据库升级成功了");
    }

    public void init_cpwd(SQLiteDatabase sqLiteDatabase){

        ContentValues contentValues=new ContentValues();
        contentValues.put(PWD,"123456");
        contentValues.put(IS_OPEN,"1");//0时，免密码登录
        contentValues.put(IMG_ID,"1");
        contentValues.put(U_name,"ALice");
        contentValues.put(ID,1);
        sqLiteDatabase.insert(TABLE0_PWD,null,contentValues);
    }
    public void Init_User(SQLiteDatabase sqLiteDatabase){
/***
 *
 final String TABLE_USER="user";
 final String U_PASSWORD="U_password";
 final String U_ID="_id";    //pk
 final String U_OnLine="is_online";
 * */
        ContentValues contentValues=new ContentValues();
        contentValues.put(U_PASSWORD,"123456");
        contentValues.put(U_OnLine,"true");//0时，免密码登录
        contentValues.put(U_name,"ALice");
        contentValues.put(ID,1);
        sqLiteDatabase.insert(TABLE_USER,null,contentValues);
    }

/*
    public void set_test_datas(SQLiteDatabase db){
        db.execSQL(" delete  from "+TABLE_GOAL_NAME);
        db.execSQL(" delete  from "+TABLE_GOAL_NAME2);

        ContentValues values=new ContentValues();
        values.put(ID,Get_New_ID(db,TABLE_GOAL_NAME));
        values.put(U_name,who_is_online(db));
        values.put(GOAL_CONTENT,"测试显示1");
        values.put(DATE1,"2017-05-08");
        values.put(GOAL_TOTAL_VALUE,"2");
        values.put(GOAL_VALUE,"30");
        values.put(REPEAT,"当天任务");
        values.put(GOAL_TIMES,"5");
        values.put(FINISH_TIMES,"3");
        values.put(REST_TIMES,"2");
        values.put(FINISH_PROGRESS,90);
        values.put(LAST_RESET_TIME,"2017-05-08");
        db.insert(TABLE_GOAL_NAME,null,values);

        ContentValues values1=new ContentValues();
        values1.put(ID,Get_New_ID(db,TABLE_GOAL_NAME));
        values1.put(U_name,who_is_online(db));
        values1.put(GOAL_CONTENT,"测试显示2");
        values1.put(DATE1,"2017-05-08");
        values1.put(GOAL_TOTAL_VALUE,"5");
        values1.put(GOAL_VALUE,"30");
        values1.put(REPEAT,"当天任务");
        values1.put(GOAL_TIMES,"10");
        values1.put(FINISH_TIMES,"4");
        values1.put(REST_TIMES,"6");
        values1.put(FINISH_PROGRESS,120);
        values1.put(LAST_RESET_TIME,"2017-05-08");
        db.insert(TABLE_GOAL_NAME,null,values1);

        ContentValues values2=new ContentValues();
        values2.put(ID,Get_New_ID(db,TABLE_GOAL_NAME));
        values2.put(U_name,who_is_online(db));
        values2.put(GOAL_CONTENT,"测试显示3");
        values2.put(DATE1,"2017-05-12");
        values2.put(GOAL_TOTAL_VALUE,"5");
        values2.put(GOAL_VALUE,"30");
        values2.put(REPEAT,"当天任务");
        values2.put(GOAL_TIMES,"10");
        values2.put(FINISH_TIMES,"4");
        values2.put(REST_TIMES,"6");
        values2.put(FINISH_PROGRESS,120);
        values2.put(LAST_RESET_TIME,"2017-05-12");
        db.insert(TABLE_GOAL_NAME,null,values2);

        ContentValues values3=new ContentValues();
        values3.put(ID,Get_New_ID(db,TABLE_GOAL_NAME));
        values3.put(U_name,who_is_online(db));
        values3.put(GET_GOAL_ID,"1");
        values3.put(GOAL2_CONTENT,"测试显示1");
        values3.put(CURRENT_YEAR,"2017");
        values3.put(CURRENT_MONTH,"05");
        values3.put(CURRENT_DAY,"08");
        values3.put(CURRENT_PROGRESS,"90");
        db.insert(TABLE_GOAL_NAME2,null,values3);

        ContentValues values4=new ContentValues();
        values4.put(ID,Get_New_ID(db,TABLE_GOAL_NAME));
        values4.put(U_name,who_is_online(db));
        values4.put(GET_GOAL_ID,"2");
        values4.put(GOAL2_CONTENT,"测试显示2");
        values4.put(CURRENT_YEAR,"2017");
        values4.put(CURRENT_MONTH,"05");
        values4.put(CURRENT_DAY,"08");
        values4.put(CURRENT_PROGRESS,"120");
        db.insert(TABLE_GOAL_NAME2,null,values4);

        ContentValues values5=new ContentValues();
        values5.put(ID,Get_New_ID(db,TABLE_GOAL_NAME));
        values5.put(U_name,who_is_online(db));
        values5.put(GET_GOAL_ID,"3");
        values5.put(GOAL2_CONTENT,"测试显示3");
        values5.put(CURRENT_YEAR,"2017");
        values5.put(CURRENT_MONTH,"05");
        values5.put(CURRENT_DAY,"12");
        values5.put(CURRENT_PROGRESS,"120");
        db.insert(TABLE_GOAL_NAME2,null,values5);

    }

*/
    //用来使日期前面补0的函数
    public String Time_Format(String s){
        String temp="";
        if(Integer.parseInt(s)<10){
            temp="0"+ String.valueOf(Integer.parseInt(s));
        }else{
            temp=s;
        }
        return temp;
    }

    public void init_category(SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues=new ContentValues();
        contentValues.put(CATEGORY_NAME,"其他");
        contentValues.put(CATEGORY_judge,"0");
        contentValues.put(CATEGORY_UID,"1");
        contentValues.put(U_name,"ALice");
        contentValues.put(ID,1);
        sqLiteDatabase.insert(TABLE_CATEGORY,null,contentValues);
        contentValues=new ContentValues();
        contentValues.put(CATEGORY_NAME,"其他");
        contentValues.put(CATEGORY_judge,"1");
        contentValues.put(CATEGORY_UID,"1");
        contentValues.put(U_name,"ALice");
        contentValues.put(ID,2);
        sqLiteDatabase.insert(TABLE_CATEGORY,null,contentValues);
        contentValues=new ContentValues();
        contentValues.put(CATEGORY_NAME,"银行卡");
        contentValues.put(CATEGORY_judge,"0");
        contentValues.put(CATEGORY_UID,"1");
        contentValues.put(U_name,"ALice");
        contentValues.put(ID,3);
        sqLiteDatabase.insert(TABLE_CATEGORY,null,contentValues);
        contentValues=new ContentValues();
        contentValues.put(CATEGORY_NAME,"银行卡");
        contentValues.put(CATEGORY_judge,"1");
        contentValues.put(CATEGORY_UID,"1");
        contentValues.put(U_name,"ALice");
        contentValues.put(ID,4);
        sqLiteDatabase.insert(TABLE_CATEGORY,null,contentValues);

    }
    public void init_tables(SQLiteDatabase db, String User_name){

        db.execSQL("delete from "+TABLE_CLASS);
        for (int i = 0; i <7 ; i++) {
            for (int j = 0; j < 12; j++) {
                ContentValues values=new ContentValues();
                values.put(CLASS_ID, (byte[]) null);
                values.put(CLASS_FINISH,"");
                // values.put(myDBHelper.CLASS_UID,"0");
                values.put(CLASS_SORT_WEEK,i);
                values.put(CLASS_SORT_NUMBER,j);
                values.put(CLASS_ADDRESS,"");
                values.put(CLASS_TIME,"");
                values.put(ID,i*12+j+1);
                values.put(U_name,User_name);
                db.insert(TABLE_CLASS,null,values);
                values.clear();
            }

        }

    }

/*
    public void init_image_category(SQLiteDatabase sqLiteDatabase,String  User_name){
        Category_Utils category_utils=new Category_Utils();
        for (int i = 0; i <category_utils.Category_OUT_ID.length ; i++) {
            ContentValues contentvalue=new ContentValues();
            contentvalue.put(IMAGE_CATEGORY_UID,1);
            contentvalue.put(IMAGE_POSITION,i);
            contentvalue.put(IMAGE_COLOR_POSITION,0);
            contentvalue.put(IMAGE_CATEGORY_NAME,"");
            contentvalue.put(IMAGE_CATEGORY_INOUT,"0");
            contentvalue.put(ID,i+1);
            contentvalue.put(U_name,User_name);
            sqLiteDatabase.insert(TABLE_IMAGE_CATEGORY,null,contentvalue);
        }
        for (int i = 0; i <category_utils.Category_IN_ID.length ; i++) {
            ContentValues contentvalue=new ContentValues();
            contentvalue.put(IMAGE_CATEGORY_UID,1);
            contentvalue.put(IMAGE_POSITION,i);
            contentvalue.put(IMAGE_COLOR_POSITION,0);
            contentvalue.put(IMAGE_CATEGORY_NAME,"");
            contentvalue.put(IMAGE_CATEGORY_INOUT,"1");
            contentvalue.put(ID,category_utils.Category_OUT_ID.length+i+1);
            contentvalue.put(U_name,User_name);
            sqLiteDatabase.insert(TABLE_IMAGE_CATEGORY,null,contentvalue);
        }


    }

    public boolean Is_in(String IMAGE_CATEGORY_ID,SQLiteDatabase db){
        boolean b=true;
        String str="select * from "+TABLE_IMAGE_CATEGORY+" where "+IMAGE_CATEGORY_ID+"="+IMAGE_CATEGORY_ID;
        Cursor c=db.rawQuery(str,null);
        if(c!=null){
            while(c.moveToNext()){
                String is_in=c.getString(c.getColumnIndex(IMAGE_CATEGORY_INOUT));
                if(is_in.equals("0")){
                    b=false;
                }
            }
        }


        return b;

    }

*/

    /**
     * 用于保存新数据时，设置ID的值，放置ID重复出现
     * 返回值为整型
     * **/

    public int Get_New_ID(SQLiteDatabase db, String table_name){
        int result=1;
        Cursor cursor=db.rawQuery("Select max(_id) as _id from "+table_name,null);
        if(cursor!=null){
            cursor.moveToNext();
            result=cursor.getInt(cursor.getColumnIndex(ID))+1;
        }
        return result;
    }

    /**
     * 获取在线的用户名
     * */

    public String who_is_online(SQLiteDatabase db){
        String result="";
        Cursor cursor=db.rawQuery("select U_name from user where is_online='true'",null);
        if(cursor!=null){
            cursor.moveToNext();
            result=cursor.getString(cursor.getColumnIndex("U_name"));
        }
        return result;
    }
/*
    public Bitmap getBmByID(String F_ID,SQLiteDatabase db){
        Bitmap temp;
        temp=GetBmBY_ID(0,0 ,true);
        String str="select "+F_ID+" from "+TABLE_FINANCE;
        Cursor c=db.rawQuery(str,null);
        String IMAGE_ID="0";
        if(c!=null){
            while(c.moveToNext()){
                IMAGE_ID=c.getString(c.getColumnIndex(F_CATEGORY_ID));

            }
        }
        boolean flag=Is_in(IMAGE_ID,db);
        String str0="select * from "+TABLE_IMAGE_CATEGORY+" where "+IMAGE_CATEGORY_ID+"="+IMAGE_ID;
        Cursor c0=db.rawQuery(str0,null);
        if(c0!=null){
            while(c0.moveToNext()){
                int image_position=Integer.parseInt(c.getString(c.getColumnIndex(IMAGE_POSITION)));
                int color_posituion=Integer.parseInt(c.getString(c.getColumnIndex(IMAGE_COLOR_POSITION)));
                temp=GetBmBY_ID(image_position,color_posituion ,flag);
            }
        }


        return  temp;
    }

    public Bitmap getBmBy_ImageID(String ID, SQLiteDatabase db,boolean b){
        Bitmap temp = null;
        String str0="select * from "+TABLE_IMAGE_CATEGORY+" where "+IMAGE_CATEGORY_ID+"="+ID;
        Cursor c0=db.rawQuery(str0,null);
        if(c0!=null){
            while(c0.moveToNext()){
                int image_position=Integer.parseInt(c0.getString(c0.getColumnIndex(IMAGE_POSITION)));
                int color_posituion=Integer.parseInt(c0.getString(c0.getColumnIndex(IMAGE_COLOR_POSITION)));
                temp=GetBmBY_ID(image_position,color_posituion ,b);
            }
        }
        return temp;
    }

    public Bitmap GetBmBY_ID(int image_id,int color_id,boolean is_in){
        Bitmap temp;
        BitmapDrawable bitmapDrawable;
        if(is_in){
            bitmapDrawable=(BitmapDrawable)context.getResources().getDrawable(new Category_Utils().Category_IN_ID[image_id]);
        }else{

            bitmapDrawable=(BitmapDrawable)context.getResources().getDrawable(new Category_Utils().Category_OUT_ID[image_id]);
        }

        temp=getAlphaBitmap(bitmapDrawable.getBitmap(), Color.parseColor(new Category_Utils().Category_color_ID[color_id]));
        return temp;
    }
 */
    public Bitmap getAlphaBitmap(Bitmap mBitmap, int newColor){
        Bitmap mAlphaBitmap= Bitmap.createBitmap(mBitmap.getWidth(),mBitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas=new Canvas(mAlphaBitmap);
        Paint mPaint=new Paint();
        mPaint.setColor(newColor);//mPaint.setColor(Color.WHITE);
        //从原位图中提取只安博涵alpha的位图
        Bitmap alphaBITMAP=mBitmap.extractAlpha();
        //在画布上绘制alpha位图
        mCanvas.drawBitmap(alphaBITMAP,0,0,mPaint);
        return  mAlphaBitmap;
    }


    /**
     * 通过种类的ID获取种类的名称
     * **/

    public String get_category_nameByID(int ID, SQLiteDatabase db){
        String str="";
        String string="select * from "+TABLE_IMAGE_CATEGORY+" where "+IMAGE_CATEGORY_ID+"="+ID;
        Cursor c=db.rawQuery(string,null);
        if(c!=null){
            c.moveToFirst();
            str=c.getString(c.getColumnIndex(IMAGE_CATEGORY_NAME));
        }


        return  str;
    }
}
