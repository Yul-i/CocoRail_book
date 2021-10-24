package com.pro3.coco;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReserveActivity extends AppCompatActivity {

    TextView reserveDay, reserveDepart, reserveDepartTime, reserveArrive, reserveArriveTime, reserveTrain, reserveSeat, reserveId;
    Button btn_delete, btn_time;
    MyDBHelper myDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        getSupportActionBar().hide();

        reserveDay = findViewById(R.id.reserveDay);
        reserveDepart = findViewById(R.id.reserveDepart);
        reserveDepartTime = findViewById(R.id.reserveDepartTime);
        reserveArrive = findViewById(R.id.reserveArrive);
        reserveArriveTime = findViewById(R.id.reserveArriveTime);
        reserveTrain = findViewById(R.id.reserveTrain);
        reserveSeat = findViewById(R.id.reserveSeat);
        reserveId = findViewById(R.id.reserveId);
        btn_delete = findViewById(R.id.btn_delete);
        btn_time = findViewById(R.id.btn_time);

        Intent intent = getIntent();
        String bookId = intent.getStringExtra("bookId");

        myDBHelper = new MyDBHelper(this);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        String sql = "select * from bookTBL where bookId = '"+bookId+"';";
        Cursor cursor = sqlDB.rawQuery(sql, null);

        ArrayList<String> reserveList = new ArrayList<>();

        while (cursor.moveToNext()){
            reserveId.setText(cursor.getString(0));
            reserveDay.setText(cursor.getString(3));
            reserveDepart.setText(cursor.getString(2));
            reserveDepartTime.setText(cursor.getString(4));
            reserveArrive.setText(cursor.getString(5));
            reserveArriveTime.setText(cursor.getString(6));
            reserveTrain.setText(cursor.getString(12));
            reserveSeat.setText(cursor.getString(7));
        }


        cursor.close();
        sqlDB.close();

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReserveActivity.this);
                builder.setMessage("예매를 취소하시겠습니까?");
                builder.setTitle("예매 취소")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
                                String sql = "delete from bookTBL where bookId = '"+bookId+"'";
                                sqlDB.execSQL(sql);
                                Log.d("sqlite3DML", "데이터 삭제 성공");

                                sqlDB.close();
                                Log.d("sqlite3DML", "데이터베이스 Closed");

                                Intent intent = new Intent(ReserveActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("종료 알림창");
                alert.show();
            }
        });

    }
}