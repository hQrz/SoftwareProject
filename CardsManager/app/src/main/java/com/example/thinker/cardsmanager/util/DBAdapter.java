package com.example.thinker.cardsmanager.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.*;
import android.content.Context;
import android.database.Cursor;
/**
 * Created by Thinker on 2017/10/25.
 */
public class DBAdapter extends SQLiteOpenHelper {
    public static  final String TABLE_NAME="Cards";
    public static final String SQL_CREATE_CARDS = "create table Cards(" +
            //primary key 将id列设为主键    autoincrement表示id列是自增长的
            "CardID integer primary key autoincrement," +
            "CardName varchar(30) not null," +
            "CardPhotoName varchar(30) not null," +
            "CardHP integer not null,"+
            "CardAttack integer not null,"+
            "CardType integer not null)";

    private Context mContext;

    //构造方法：第一个参数Context，第二个参数数据库名，第三个参数cursor允许我们在查询数据的时候返回一个自定义的光标位置，一般传入的都是null，第四个参数表示目前库的版本号（用于对库进行升级）
    public DBAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        //必须通过super 调用父类的构造函数
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //调用SQLiteDatabase中的execSQL（）执行建表语句。
        db.execSQL(SQL_CREATE_CARDS);
        //创建成功
        Toast.makeText(mContext, "生成数据库成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}
