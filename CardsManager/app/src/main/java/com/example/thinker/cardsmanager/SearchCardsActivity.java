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

public class SearchCardsActivity extends AppCompatActivity{
    private DBAdapter dbAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cards);
        //获取卡牌数据
        dbAdapter=new DBAdapter(this,"CardsGame.db",null,1);
        SQLiteDatabase db= dbAdapter.getWritableDatabase();
        Cursor dataset=db.query("Cards",null,null,null,null,null,null);
        //显示到控件上
        //......
    }
}
