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

public class AddCardActivity extends AppCompatActivity {
    private Button bt_add;
    private EditText cards_name;
    private EditText cards_pic_name;
    private EditText cards_hp;
    private EditText cards_attack;
    private EditText cards_type;
    private DBAdapter dbAdapter;
    private ContentValues cValues;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        initWeightItems();
        dbAdapter=new DBAdapter(this,"CardsGame.db",null,1);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SQLiteDatabase db= dbAdapter.getWritableDatabase();
                    Cursor dataset=db.query("Cards",null,null,null,null,null,null);
                    cValues=new ContentValues();
                    //"CardID"由数据库自行给出
                    cValues.put("CardName",cards_name.getText().toString());
                    cValues.put("CardPhotoName",cards_pic_name.getText().toString());
                    cValues.put("CardHP",cards_hp.getText().toString());
                    cValues.put("CardAttack",cards_attack.getText().toString());
                    cValues.put("CardType",cards_type.getText().toString());
                    //db.beginTransaction();
                    db.insert("Cards",null,cValues);
                    //db.setTransactionSuccessful();
                    cValues.clear();
                    dataset=db.query("Cards",null,null,null,null,null,null);
                    dataset.moveToLast();
                    String info=dataset.getString(dataset.getColumnIndex("CardID"));
                    info+=";";
                    info+=dataset.getString(dataset.getColumnIndex("CardName"));
                    Toast.makeText(getApplicationContext(),info,
                            Toast.LENGTH_SHORT).show();
                    //db.close();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void initWeightItems() {
        bt_add=(Button)this.findViewById(R.id.bt_add);
        cards_name=(EditText)this.findViewById(R.id.pt_cn);
        cards_pic_name=(EditText)this.findViewById(R.id.pt_cpn);
        cards_hp=(EditText)this.findViewById(R.id.pt_card_hp);
        cards_attack=(EditText)this.findViewById(R.id.pt_card_attack);
        cards_type=(EditText)this.findViewById(R.id.pt_type);
    }
}
