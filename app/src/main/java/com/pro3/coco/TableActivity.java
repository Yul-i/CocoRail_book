package com.pro3.coco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {

    DatabaseReference database;
    ListView station_list, time_list;
    ArrayList<TrainTable> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);


        station_list = findViewById(R.id.station_list);
        time_list = findViewById(R.id.time_list);

        database = FirebaseDatabase.getInstance().getReference("table");
        Log.d("파이어베이스>>", database + " ");

        arrayList = new ArrayList<>();

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("파이어베이스>>", "아무거나");

                // 하나하나의 노드를 스냅샷이라고 한다.
                // 유저들의 목록을 가지고 오는 메서드
                arrayList.clear();
                //유저 아래에 있는 데이터들의 갯수
                Log.d("파이어베이스>>", "users아래의 자식들의 개수 : " + snapshot.getChildrenCount());
                Log.d("파이어베이스>>","전체 Json목록 가지고 온 것 " + snapshot.getChildren());
                // for문으로 하나씩 꺼내보자
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Log.d("파이어베이스>>","하나의 snapshot : " + snapshot1);
                    Log.d("파이어베이스>>","하나의 snapshot value : " + snapshot1.getValue());
                    TrainTable table = snapshot1.getValue(TrainTable.class);
                    Log.d("파이어베이스>>", "user 1명 : "+table);
                    arrayList.add(table);
                }
                Log.d("파이어베이스>>", "user목록 전체: "+arrayList);
                Log.d("파이어베이스>>", "user목록 개수: "+arrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}