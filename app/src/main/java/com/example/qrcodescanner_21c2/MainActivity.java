package com.example.qrcodescanner_21c2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //view object
    private Button buttonScan;
    private TextView textViewName, textViewClass, textViewId;
    //qr object
    private IntentIntegrator qrScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewNama);
        textViewClass = (TextView) findViewById(R.id.textViewKelas);
        textViewId = (TextView) findViewById(R.id.textViewNIM);
        //scan obj
        qrScan = new IntentIntegrator(this);
        //imp onclick list
        buttonScan.setOnClickListener(this);
    }
    // scan result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
                resultCode, data);
        if (result != null) {
            //if qr don't exist
            if (result.getContents() == null) {
                Toast.makeText(this, "Qr code was cancelled",
                        Toast.LENGTH_LONG).show();
                //if qr code exist >>>
            }
            // web url
            if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent OpenBrowser = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(result.getContents()));
                startActivity(OpenBrowser);
            }
            //phone call
            String number;
            number = new String(result.getContents());
            if(number.matches("^[0-9]*$") && number.length() > 11){
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(Uri.parse("tel:" + number));
                startActivity(dial);
            } else {
                try {
                    //json
                    JSONObject obj = new JSONObject(result.getContents());
                    //set result
                    textViewName.setText(obj.getString("nama"));
                    textViewClass.setText(obj.getString("kelas"));
                    textViewId.setText(obj.getString("nim"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(),
                            Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        qrScan.initiateScan();

    }

    }