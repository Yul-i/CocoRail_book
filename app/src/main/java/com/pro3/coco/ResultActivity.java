package com.pro3.coco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    MyDBHelper myDBHelper;
    LinearLayout not_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().hide();

        not_layout = findViewById(R.id.not_layout);

        myDBHelper = new MyDBHelper(this);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        String sql = "select bookId, bookDepartDay, bookDepart, bookArrive from bookTBL";
        Cursor cursor = sqlDB.rawQuery(sql, null);

        //요소 추가
        //리스트뷰
        ListView list = findViewById(R.id.list);

        //단어 담을 리스트
        ArrayList<String> resultList = new ArrayList<>();
        ArrayList<String> idList = new ArrayList<>();

        while (cursor.moveToNext()){
            not_layout.setVisibility(View.INVISIBLE);
            resultList.add(cursor.getString(2)+" / 출발지 : "+cursor.getString(1)+" / 도착지 : "+cursor.getString(3));
            idList.add(cursor.getString(0));
        }


        cursor.close();
        sqlDB.close();

        ArrayAdapter<String> resultAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, resultList);


        list.setAdapter(resultAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ResultActivity.this, ReserveActivity.class);
                //가격은 intent로 넘기고 나머지는 일단 DB에 저장해두는걸로
                intent.putExtra("bookId", idList.get(position));
                startActivity(intent);
            }
        });

    }
}