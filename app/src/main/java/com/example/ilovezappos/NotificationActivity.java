package com.example.ilovezappos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NotificationActivity extends AppCompatActivity {

    // activity components
    private EditText settingEnterText;
    private Button setButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // get the componenets
        settingEnterText = findViewById(R.id.notification_editText);
        setButton = findViewById(R.id.notification_set_button);
        cancelButton = findViewById(R.id.notification_cancel_button);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newSetting = settingEnterText.getText().toString();

                Intent returnToMain = new Intent();
                returnToMain.putExtra("setting", newSetting);
                setResult(Activity.RESULT_OK, returnToMain);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnToMain = new Intent();
                setResult(Activity.RESULT_CANCELED, returnToMain);
                finish();
            }
        });
    }
}
