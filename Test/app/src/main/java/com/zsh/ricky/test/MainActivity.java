package com.zsh.ricky.test;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zsh.ricky.test.model.GameHistory;
import com.zsh.ricky.test.util.HistoryAdapter;
import com.zsh.ricky.test.util.OkHttpHelper;
import com.zsh.ricky.test.util.UrlResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ListView lvInfo;
    private Button btBack, btAdd;
    private RadioGroup rgMenu;
    private RadioButton rbCard, rbHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupItem();
        setupList();

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbHistory.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setupItem() {
        lvInfo = (ListView) this.findViewById(R.id.infoList);
        btBack = (Button) this.findViewById(R.id.admin_btBack);
        btAdd = (Button) this.findViewById(R.id.admin_btAdd);
        rgMenu = (RadioGroup) this.findViewById(R.id.admin_menuGroup);
        rbCard = (RadioButton) this.findViewById(R.id.admin_cardMenu);
        rbHistory = (RadioButton) this.findViewById(R.id.admin_historyMenu);
    }

    private void setupList() {
        OkHttpHelper helper = new OkHttpHelper();
        Call call = helper.getRequest(UrlResources.HISTORY);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "与服务器连接失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<GameHistory> data = convertHistoryData(response.body().string());
                            lvInfo.setAdapter(new HistoryAdapter(data, MainActivity.this));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    /**
     * 将服务器返回的字符串转换成List<GameHistory>信息
     * @param str 服务器返回的信息
     * @return 返回转换后的数据,转换失败返回null
     */
    private List<GameHistory> convertHistoryData(String str) {

        List<GameHistory> data = null;

        try {
            JSONArray jsonArray = new JSONArray(str);
            data = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                GameHistory gh = new GameHistory();

                gh.setPlayerA(jsonObj.getString("playerA"));
                gh.setPlayerB(jsonObj.getString("playerB"));
                gh.setWinner(jsonObj.getString("winner"));
                gh.setHistoryNum(jsonObj.getInt("historyNum"));
                gh.setDate(jsonObj.getString("date"));
                gh.setTime(jsonObj.getString("time"));

                data.add(gh);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
