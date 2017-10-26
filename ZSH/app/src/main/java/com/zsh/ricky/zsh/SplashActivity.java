package com.zsh.ricky.zsh;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import com.zsh.ricky.zsh.util.OkHttpHelper;
import com.zsh.ricky.zsh.util.UrlResources;


public class SplashActivity extends AppCompatActivity {

    private LinearLayout splash_layout = null;
    private TextView version_view = null;

    public void updateCards(){//get卡牌数，生成需下载的卡牌id字串，‘;’分割，分别post信息与图片
        OkHttpHelper helper=new OkHttpHelper();
        Call get_cards_count=helper.getRequest(UrlResources.CARDS_COUNT);
        get_cards_count.enqueue(new Callback() {
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
                String cards_count=response.body().toString();

            }
        });
        Map<String,String> map=new HashMap<>();
        String str=new String();

        Call apply_download_list=helper.postRequest("",map);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        splash_layout = (LinearLayout) this.findViewById(R.id.SplashLayout);
        version_view = (TextView) this.findViewById(R.id.version);
        version_view.setText(getAppVersion());

        //检查网络状态是否可用
        if (isNetworkConnected())
        {
            //更新卡牌
            updateCards();

            //splash做一个动画后进入登录界面
            AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
            aa.setDuration(2000);
            splash_layout.setAnimation(aa);
            splash_layout.startAnimation(aa);
            //通过handler延迟2秒
            new Handler().postDelayed(new LoadLoginTask(), 2000);
        }
        else
        {
            showNetworkDialog();
        }
    }

    private class LoadLoginTask implements Runnable
    {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 获取当前app的版本号
     * @return 获取成功返回版本号，否则返回空
     */
    private String getAppVersion()
    {
        String version;

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = "Version " + info.versionName;

        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            version = "Version";
        }

        return version;
    }

    /**
     * 判断当前网络是否可用
     * @return 可用则返回true,否则返回false
     */
    private boolean isNetworkConnected()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        //WifiManager  wifimanager =  (WifiManager) getSystemService(WIFI_SERVICE);
        //wifimanager.isWifiEnabled();
        //wifimanager.getWifiState();

        return (info != null && info.isConnected());
    }

    /**
     * 当网络检测出问题时出现该对话框
     */
    private void showNetworkDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置网络");
        builder.setMessage("网络错误请检查网络状态");

        //设置手机中网络连接
        builder.setPositiveButton("设置网络", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                //类名一定包含包名
                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");

                startActivity(intent);
                finish();
            }
        });
        //结束游戏
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }
}
