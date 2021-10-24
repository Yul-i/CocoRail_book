package com.pro3.coco;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PayActivity extends AppCompatActivity {

    TextView totalPrice, bookPrice, mileage;
    EditText usedMileage;
    Button btn_bank, btn_kakao, btn_naver, btn_card, btn_cancel;
    MyDBHelper myDBHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        getSupportActionBar().hide();

        totalPrice = findViewById(R.id.totalPrice);
        bookPrice = findViewById(R.id.bookPrice);
        mileage = findViewById(R.id.mileage);
        usedMileage = findViewById(R.id.usedMileage);
        btn_bank = findViewById(R.id.btn_bank);
        btn_kakao = findViewById(R.id.btn_kakao);
        btn_naver = findViewById(R.id.btn_naver);
        btn_card = findViewById(R.id.btn_card);
        btn_cancel = findViewById(R.id.btn_cancel);

        //앞 book액티비티에서 가져온 데이터 두개, bookId, price
        Intent intent = getIntent();
        String bookId = intent.getStringExtra("bookId");
        int price = intent.getIntExtra("price", 0);

        /*
            회원 값 가져오기, 임시로 마일리지 데이터 넣음
        */
        int memberMileage = 3000;

        totalPrice.setText(price+"원");
        bookPrice.setText(price+"원");
        mileage.setText(memberMileage+"P");

        usedMileage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        int checkMileage = Integer.parseInt(usedMileage.getText().toString());
                        if (memberMileage<checkMileage){
                            Toast.makeText(getApplicationContext(), "보유 마일리지가 부족합니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(price<checkMileage){
                                Toast.makeText(getApplicationContext(), "마일리지가 구매 금액보다 큽니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                totalPrice.setText(price-checkMileage+"원");
                            }
                        }
                }
                return false;
            }
        });//usedMileage end

        btn_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass("bank", bookId);
            }
        });

        btn_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass("kakao", bookId);
            }
        });

        btn_naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass("naver", bookId);
            }
        });

        btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass("card", bookId);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
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

                                Intent intent = new Intent(PayActivity.this, MainActivity.class);
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
        });//btn_cancel end

    }//onCreate end

    public void pass(String payKind, String bookId){
        switch (payKind){
            case "bank" :
                inputDB("bankbook", bookId);
            case "kakao" :
                inputDB("kakaoPay", bookId);
            case "naver" :
                inputDB("naverPay", bookId);
            case "card" :
                inputDB("creditCard", bookId);
        }
    }

    public void inputDB(String pay, String bookId){
        myDBHelper = new MyDBHelper(this);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        String bookPriceText = bookPrice.getText().toString();
        String totalPriceText = totalPrice.getText().toString();
        String bookPayText = pay;
        String sql = "update bookTBL set bookPrice='"+bookPriceText+"', bookTotalPrice='"+bookPriceText+"', bookPay='"+bookPayText+"' where bookId = '"+bookId+"';";
        sqlDB.execSQL(sql);
        Log.d("sqlite3DML", "데이터 삽입 성공");

        sqlDB.close();
        Log.d("sqlite3DML", "데이터베이스 Closed");

        Toast.makeText(getApplicationContext(), "예매 성공하였습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PayActivity.this, ResultActivity.class);
        startActivity(intent);
    }
}