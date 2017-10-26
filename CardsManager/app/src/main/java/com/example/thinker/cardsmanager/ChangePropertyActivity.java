package com.example.thinker.cardsmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.*;
import android.widget.Toast;

import com.example.thinker.cardsmanager.util.DBAdapter;
/**
 * Created by Thinker on 2017/10/25.
 */

public class ChangePropertyActivity extends AppCompatActivity{
    public void initWeightTableList(){

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_property);
        //初始化控件，显示卡牌列表
        initWeightTableList();


    }
}
