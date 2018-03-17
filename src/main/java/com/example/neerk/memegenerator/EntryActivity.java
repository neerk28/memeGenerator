package com.example.neerk.memegenerator;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class EntryActivity extends AppCompatActivity {


    private boolean isBackButtonPressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ActivityCompat.requestPermissions(EntryActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        RadioButton button1 = findViewById(R.id.radioButton);
        Typeface typeface = ResourcesCompat.getFont(this,R.font.caveat_brush);
        button1.setTypeface(typeface);

        RadioButton button2 = findViewById(R.id.radioButton2);
        button2.setTypeface(typeface);
    }

    public void OnRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        Intent intent;
        switch (view.getId()) {
            case R.id.radioButton:
                if (checked) {
                    intent = new Intent(EntryActivity.this, MainActivity1.class);
                    startActivity(intent);
                }
                break;
            case R.id.radioButton2:
                if (checked) {
                    intent = new Intent(EntryActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (!isBackButtonPressed) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            isBackButtonPressed = true;
        } else {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                isBackButtonPressed = false;
            }
        };

    }
}
