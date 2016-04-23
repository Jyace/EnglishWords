package com.example.jyace.englishwords;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
MyDatabaseHelper dbHelper;
    Button insert,search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);
        dbHelper=new MyDatabaseHelper(this,"dict.db",1);
        insert=(Button)findViewById(R.id.button);
        search=(Button)findViewById(R.id.button2);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word=((EditText)findViewById(R.id.editText2)).getText().toString();
                String detail=((EditText)findViewById(R.id.editText1)).getText().toString();
                insertData(dbHelper.getReadableDatabase(),word,detail);
                Toast.makeText(MainActivity.this,"sucessful",Toast.LENGTH_LONG).show();

            }
        });
       search.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String key=((EditText)findViewById(R.id.editText)).getText().toString();
               Cursor cursor=dbHelper.getReadableDatabase().rawQuery("select *from dict where word like ? or detail like ?", new String[]{"%" + key + "%", "%" + key + "%"});
               Bundle data=new Bundle();
               data.putSerializable("data",converCursorToList(cursor));
               Intent intent=new Intent(MainActivity.this,ResultActivity.class);
               intent.putExtras(data);
               startActivity(intent);

           }
       });
    }
    protected ArrayList<Map<String,String>>converCursorToList(Cursor cursor){

        ArrayList<Map<String,String>> result=new ArrayList<Map<String,String>>();

        while(cursor.moveToNext()){
           Map<String,String> map=new HashMap<>();
            map.put("word",cursor.getString(1));
            map.put("detail",cursor.getString(2));
            result.add(map);



        }
        return result;

    }
    protected void insertData(SQLiteDatabase db,String word,String detail){


        db.execSQL("insert into dict values(null,?,?)",new String[]{word,detail});


    }
 @Override
    public void onDestroy(){

        super.onDestroy();;
        if(dbHelper!=null)dbHelper.close();

    }
}
