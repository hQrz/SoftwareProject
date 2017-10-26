package com.zsh.ricky.zsh.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.zsh.ricky.zsh.AdminActivity;

/**
 * Created by Ricky on 2017/10/5.
 */

public class DialogUtil {

    //定义显示一个消息的对话框
    public static void showDialog(final Context ctx, String msg, boolean goHome)
    {
        //创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(msg);
        builder.setCancelable(false);
        if (goHome)
        {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ctx, AdminActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);
                }
            });
        }
        else
        {
            builder.setPositiveButton("确定", null);
        }
        builder.create().show();
    }

    //定义一个显示指定组件的对话框
    public static void showDialog(Context ctx, View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", null);
        builder.create().show();
    }
}
