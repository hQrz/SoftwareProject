package com.zsh.ricky.test.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zsh.ricky.test.MainActivity;
import com.zsh.ricky.test.R;
import com.zsh.ricky.test.model.GameHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Ricky on 2017/10/22.
 */

public class HistoryAdapter extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private List<GameHistory> data;
    private Activity activity;

    public HistoryAdapter(List<GameHistory> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (context == null) {
            context = parent.getContext();
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_item, null);
            viewHolder = new ViewHolder();
            viewHolder.hPlayerTv = (TextView) convertView.findViewById(R.id.battlePlayer);
            viewHolder.hWinnerTv = (TextView) convertView.findViewById(R.id.battleWinner);
            viewHolder.hDateTv = (TextView) convertView.findViewById(R.id.battleDate);
            viewHolder.hTimeTv = (TextView) convertView.findViewById(R.id.battleTime);
            viewHolder.hBtn = (Button) convertView.findViewById(R.id.historyDeleteBt);
            convertView.setTag(viewHolder);
        }
        //获取viewHolder实例
        viewHolder = (ViewHolder) convertView.getTag();
        //设置数据
        GameHistory gh = data.get(position);
        String player = gh.getPlayerA() + " vs " + gh.getPlayerB();
        viewHolder.hPlayerTv.setText(player);
        viewHolder.hWinnerTv.setText(gh.getWinner());
        viewHolder.hDateTv.setText(gh.getDate());
        viewHolder.hTimeTv.setText(gh.getTime());

        viewHolder.hBtn.setText("删除");
        viewHolder.hBtn.setOnClickListener(this);
        viewHolder.hBtn.setTag(R.id.btn, position);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        Log.d("tag", "deleteButton: " + "view = " + v);
        int b = (int) v.getTag(R.id.btn);
        Toast.makeText(context, "我是按钮" + b, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据按钮位置，删除历史信息
     * @param position 按钮位置
     */
    private void deleteHistory(final int position) {
        int historyNum = data.get(position).getHistoryNum();

        Map<String, String> map = new HashMap<>();
        map.put("historyNum", String.valueOf(historyNum));

        OkHttpHelper helper = new OkHttpHelper();
        Call call = helper.postRequest(UrlResources.DELETE_HISTORY, map);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "服务器连接出错", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObj = new JSONObject(response.body().string());

                    boolean result = jsonObj.getBoolean("result");
                    if (result) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                data.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    } else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static private class ViewHolder {
        TextView hPlayerTv;      //当前对战文本
        TextView hWinnerTv;       //当前胜者文本
        TextView hDateTv;        //当前对战日期文本
        TextView hTimeTv;       //当前对战时间文本
        Button hBtn;       //当前按钮
    }
}
