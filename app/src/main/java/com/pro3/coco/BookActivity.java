package com.pro3.coco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookActivity extends AppCompatActivity {

    TextView departDay, depart, departTime, arrive, arriveTime, trainNum, seatNum;
    Button btn_pay;
    MyDBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        getSupportActionBar().hide();

        /*
            회원 정보 받아오기
        */

        String mId = "test1";
        int price = 19500;

        departDay = findViewById(R.id.departDay);
        depart = findViewById(R.id.depart);
        departTime = findViewById(R.id.reserveDepartTime);
        arrive = findViewById(R.id.reserveArrive);
        arriveTime = findViewById(R.id.reserveArriveTime);
        trainNum = findViewById(R.id.trainNum);
        seatNum = findViewById(R.id.seatNum);

        btn_pay = findViewById(R.id.btn_pay);

        /*
            DB 받아오면 모든 TextView 전부 setText() 처리 해줄 것

                departDay.setText();
                depart.setText();
                departTime.setText();
                arrive.setText();
                arriveTime.setText();
                trainNum.setText();
                seatNum.setText();
        */


        myDBHelper = new MyDBHelper(this);

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
                String departDayText = departDay.getText().toString();
                String departText = depart.getText().toString();
                String departTimeText = departTime.getText().toString();
                String arriveText = arrive.getText().toString();
                String arriveTimeText = arriveTime.getText().toString();
                String trainNumText = trainNum.getText().toString();
                String seatNumText = seatNum.getText().toString();

                SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
                String nowDay = date.format(new Date());

                String sql = "insert into bookTBL (bookId, mId, bookDepartDay, bookDepart, bookDepartTime, bookArrive, " +
                        "bookArriveTime, bookTrainNum, bookSeatNum)values " +
                        "('"+nowDay+"','"+mId+"','"+departText+"','"+departDayText+"','"+departTimeText+"','"+arriveText+"'," +
                        "'"+arriveTimeText+"','"+trainNumText+"','"+seatNumText+"');";
                sqlDB.execSQL(sql);
                Log.d("sqlite3DML", "데이터 삽입 성공");

                sqlDB.close();
                Log.d("sqlite3DML", "데이터베이스 Closed");

                Intent intent = new Intent(BookActivity.this, PayActivity.class);
                //가격은 intent로 넘기고 나머지는 일단 DB에 저장해두는걸로
                intent.putExtra("bookId", nowDay);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });
    }
}