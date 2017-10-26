package com.zsh.ricky.zsh;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zsh.ricky.zsh.util.OkHttpHelper;

import org.json.JSONArray;

import okhttp3.Call;

public class AdminActivity extends AppCompatActivity {

    private RadioGroup rgMenu;
    private RadioButton rtCard,rtHisory;
    private Button btAdd,btBack;
    private ListView lvInfo;

    private JSONArray historyArray;   //存储从服务器获取的历史信息
    private JSONArray cardArray;      //存储从服务器获取的卡牌信息

    private static final int CARD_LIST = 0;
    private static final int HISTORY_LIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //设置没有titleBar
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setupItem();

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(backIntent);
                finish();
            }
        });
    }

    private void setupItem() {
        rgMenu = (RadioGroup) this.findViewById(R.id.admin_menuGroup);
        rtCard = (RadioButton) this.findViewById(R.id.admin_cardMenu);
        rtHisory = (RadioButton) this.findViewById(R.id.admin_historyMenu);
        btAdd = (Button) this.findViewById(R.id.admin_btAdd);
        btBack = (Button) this.findViewById(R.id.admin_btBack);
        lvInfo = (ListView) this.findViewById(R.id.infoList);
    }

    private class MenuChangeListener implements RadioGroup.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            if (rtCard.isChecked()) {
                System.out.println("card is checked");
            }
            else if (rtHisory.isChecked()) {

                OkHttpHelper helper = new OkHttpHelper();

            }
        }
    }
}
