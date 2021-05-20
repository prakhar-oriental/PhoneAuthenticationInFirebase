package com.example.lastphoneauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
Button mverifycodebtn;
EditText otpEdit;
String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        firebaseAuth = FirebaseAuth.getInstance();
        mverifycodebtn = findViewById(R.id.verifycodebtn);
        otpEdit = findViewById(R.id.verifycodedit);
        otp = getIntent().getStringExtra("auth");
        mverifycodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verification_code = otpEdit.getText().toString();
                if(!verification_code.isEmpty())
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp,verification_code);
                    signIn(credential);
                }
            }
        });
    }
    public void signIn(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    sendtoMain();
                }else
                {
                    Toast.makeText(OTPActivity.this, "verification fialed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser =firebaseAuth.getCurrentUser();
        if(currentuser!=null)
        {
            sendtoMain();
        }
    }

    private void sendtoMain() {
        startActivity(new Intent(OTPActivity.this,MainActivity.class));
        finish();
    }
}