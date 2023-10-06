package com.kbu.busstopjava.firebase;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    //view Objects
    private Button buttonScan,btn_save;
    private TextView textViewName, textViewAddress, textViewResult, textViewCounter;
    int a = 0;
    //qr code scanner object
    private int counter = 45;
    private IntentIntegrator qrScan;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mDatabase = FirebaseDatabase.getInstance().getReference();

        readUser();

        //View Objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewPassword);
        textViewResult = (TextView)  findViewById(R.id.textViewResult);
        textViewCounter = findViewById(R.id.counter);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //button onClick
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String getUserName = textViewName.getText().toString();
                String getUserPassword = textViewAddress.getText().toString();
                String getUserNumber = textViewResult.getText().toString();

                //hashmap 만들기
                HashMap result = new HashMap<>();
                result.put("학과", getUserPassword);
                result.put("학번", getUserNumber);
                result.put("이름",getUserName);

                a = a+1;
                writeNewUser(a,getUserPassword,getUserNumber,getUserName);

                //scan option
                qrScan.setPrompt("Scanning...");
                //qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                Toast.makeText(MainActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                try {
                    //data를 json으로 변환
                    JSONObject obj = new JSONObject(result.getContents());
                    textViewAddress.setText(obj.getString("userPassword"));
                    textViewName.setText(obj.getString("userNumber"));
                    textViewResult.setText(obj.getString("userName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    textViewResult.setText(result.getContents());
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void writeNewUser(int userId, String userName, String userPassword, String userNumber) {
        user user = new user(userName, userPassword, userNumber);

        mDatabase.child("users").child(String.valueOf(userId)).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        if (counter == 0){
                            Toast.makeText(MainActivity.this,"자리가 없습니다.",Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(MainActivity.this,"승차하였습니다.",Toast.LENGTH_SHORT).show();
                        counter --;

                        textViewCounter.setText("잔여 좌석 : " +  counter +"/45");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(MainActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void readUser(){
        mDatabase.child("users").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(user.class) != null){
                    user post = dataSnapshot.getValue(user.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                } else {
                    Toast.makeText(MainActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }








}