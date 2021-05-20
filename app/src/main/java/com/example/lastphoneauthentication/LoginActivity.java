package com.example.lastphoneauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
Button mSendotp;
TextView processText;
EditText countrycodeedit,phoneNumberEdit;
FirebaseAuth auth;
 PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        mSendotp = findViewById(R.id.sendcodebtn);
        processText = findViewById(R.id.text_process);
        countrycodeedit = findViewById(R.id.inputcountrycode);
        phoneNumberEdit = findViewById(R.id.inputphonenumber);
        mSendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country_code = countrycodeedit.getText().toString();
                String phone = phoneNumberEdit.getText().toString();
                String PhoneNumber = "+" + country_code +"" + phone;
                if(!country_code.isEmpty() || !phone.isEmpty())
                {
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(PhoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(LoginActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }else
                {
                    processText.setText("Please enter phone number & country code");
                    processText.setVisibility(View.VISIBLE);
                }
            }
        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                   processText.setText(e.getMessage());
                   processText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                processText.setText("Otp has been sent");
                processText.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent otpIntent = new Intent(LoginActivity.this,OTPActivity.class);
                        otpIntent.putExtra("auth",s);
                        startActivity(otpIntent);
                    }
                },10000);

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user!=null)
        {
            sendtoMain();
        }
    }

    private void sendtoMain() {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
    public void signIn(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    sendtoMain();
                }else
                {
                    processText.setText(task.getException().getMessage());
                    processText.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}