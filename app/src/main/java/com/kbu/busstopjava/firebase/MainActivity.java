package com.kbu.busstopjava.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> barcodeLauncher;
    int siteCount = 0;
    int maxSite = 4;
    String tag = "diorTAG";
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        textView = findViewById(R.id.textview);

        startQRCodeScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                Log.d(tag, result.getContents().substring(1, 7));
                siteCount++;
                Log.d(tag, String.valueOf(siteCount));
                if(!(siteCount > maxSite)){
                    Toast.makeText(this, "스캔완료\n남은 좌석: "+(siteCount - 29), Toast.LENGTH_SHORT).show();
                    Log.i(tag, "스캔완료\n남은 좌석: "+(siteCount - 29));
                    startQRCodeScan();
                } else {
                    Toast.makeText(this, "남은 좌석이 없습니다.", Toast.LENGTH_SHORT).show();
                    textView.setText("남은 좌석이 없습니다.");
                    btn.setEnabled(true);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startQRCodeScan();
                        }
                    });
                }
            }
        }
    }

    /**/
    private void startQRCodeScan() {
        IntentIntegrator qrScan = new IntentIntegrator(this);
        qrScan.setCameraId(1);
        qrScan.setOrientationLocked(true);

        qrScan.initiateScan();
    }
}
