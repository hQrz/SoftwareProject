package com.example.thinker.cardsmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.*;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.net.Uri;

import com.example.thinker.cardsmanager.util.DBAdapter;
import com.example.thinker.cardsmanager.util.OkHttpHelper;
import com.example.thinker.cardsmanager.util.UrlResources;
import com.example.thinker.cardsmanager.util.PublicFuntion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddCardActivity extends AppCompatActivity {
    private Button      bt_add;
    private Button      bt_back;
    private EditText    cards_name;
    private ImageView   cards_pic_name;
    private EditText    cards_hp;
    private EditText    cards_attack;
    private Spinner     cards_type;
    private DBAdapter   dbAdapter;
    private ContentValues cValues;
    private String      card_pic_path;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ActivityCompat.requestPermissions(AddCardActivity.this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        initWeightItems();
        //ImageView点击事件，获取一个图片，并为 card_pic_path 赋值
        cards_pic_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, 0x1);
            }
        });
        //添加按钮事件 ———— 上传卡牌信息到服务器，添加信息到本地数据库
        dbAdapter=new DBAdapter(this,DBAdapter.DB_NAME,null,1);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(cards_pic_name.getDrawable()==null || cards_attack.getText()==null || cards_hp.getText()==null || cards_name.getText()==null||cards_type.getSelectedItem().toString()==null) {
                        Toast.makeText(getApplicationContext(), "卡牌信息不完整！",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //添加卡牌基本信息到cValue中
                    initcValues();
                    //卡牌基本信息上传到服务器，成功，则继续上传图片
                    Map<String,String> card_info=ContentValuesToMap(cValues);
                    OkHttpHelper httpHelper = new OkHttpHelper();
                    Call call = httpHelper.postRequest(UrlResources.NEW_CARD_INFO, card_info);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "服务器响应失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //图片上传到服务器
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        OkHttpHelper post_pic=new OkHttpHelper();
                                        Call pic_call = post_pic.imageUpLoad(UrlResources.NEW_CARD_PIC,card_pic_path);
                                        pic_call.enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                Toast.makeText(getApplicationContext(), "图片上传失败",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                //添加到本地数据库中
                                                SQLiteDatabase db= dbAdapter.getWritableDatabase();
                                                Cursor dataset=db.query(DBAdapter.TABLE_NAME,null,null,null,null,null,null);
                                                db.insert(DBAdapter.TABLE_NAME,null,cValues);
                                                cValues.clear();
                                            }
                                        });
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    });

                    /*//测试是否添加成功
                    dataset=db.query("Cards",null,null,null,null,null,null);
                    dataset.moveToLast();
                    String info=dataset.getString(dataset.getColumnIndex("CardID"));
                    info+=";";
                    info+=dataset.getString(dataset.getColumnIndex("CardName"));
                    info+=";"+dataset.getCount();
                    Toast.makeText(getApplicationContext(),info,Toast.LENGTH_SHORT).show();
                    */
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //返回按钮事件 ———— 返回上一级
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void initWeightItems() {
        bt_add=(Button)this.findViewById(R.id.ac_btAdd);
        cards_name=(EditText)this.findViewById(R.id.ac_etCardName);
        cards_pic_name=(ImageView)this.findViewById(R.id.ac_image);
        cards_hp=(EditText)this.findViewById(R.id.ac_etCardHp);
        cards_attack=(EditText)this.findViewById(R.id.ac_etCardAttack);
        cards_type=(Spinner)this.findViewById(R.id.ac_spCardType);
        bt_back=(Button)this.findViewById(R.id.ac_btBack);
    }
    //显示图片
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());

            PublicFuntion pf=new PublicFuntion();
            card_pic_path=pf.getRealPathFromUri(this,uri);
            /*String isFileHasSave=String.valueOf(pf.isSave(value));
            Toast.makeText(getApplicationContext(), isFileHasSave,Toast.LENGTH_SHORT).show();
            cards_name.setText(value);*/

            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                ImageView imageView = (ImageView) findViewById(R.id.ac_image);
                /* 将Bitmap设定到ImageView */
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //ContentValues 转成 Map<String,String>
    protected  Map<String,String> ContentValuesToMap(ContentValues cv){
        Map<String,String> temp=new HashMap<>();
        for (String key:cv.keySet()){
            temp.put(key,cv.get(key).toString());
        }
        return temp;
    }
    //初始化cValue信息为卡牌基本信息
    protected  void initcValues(){
        cValues=new ContentValues();
        //"CardID"由数据库自行给出
        cValues.put(DBAdapter.COL_NAME,    cards_name.getText().toString());
        //卡牌图片名称与卡牌同名
        cValues.put(DBAdapter.COL_PIC_NAME,cards_name.getText().toString());
        cValues.put(DBAdapter.COL_HP,      cards_hp.getText().toString());
        cValues.put(DBAdapter.COL_ATTACK,  cards_attack.getText().toString());
        cValues.put(DBAdapter.COL_TYPE,    cards_type.getSelectedItem().toString());
    }

}
