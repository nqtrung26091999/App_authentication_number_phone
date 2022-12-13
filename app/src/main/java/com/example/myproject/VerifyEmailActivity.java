package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyEmailActivity extends AppCompatActivity {
    private EditText edtVerifyEmail;
    private Button btnVerifyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        initUI();
        initListener();
    }

    private void initListener() {
        btnVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSendVerifyEmail();
            }
        });
    }

    private void onClickSendVerifyEmail() {

    }

    private void initUI() {
        btnVerifyEmail = findViewById(R.id.btn_verify_email);
        edtVerifyEmail = findViewById(R.id.edt_verify_email);
    }
}