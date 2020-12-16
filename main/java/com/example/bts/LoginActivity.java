package com.example.bts;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText TextInputEditText_id, TextInputEditText_password;
    RelativeLayout RelativeLayout_login;
    Button Signup;

    String emailOK ="dyh11";
    String passwordOK = "1234";

    String inputEmail = "";
    String inputPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
        startActivity(intent);


        TextInputEditText_id = findViewById(R.id.TextInputEditText_id);
        TextInputEditText_password = findViewById(R.id.TextInputEditText_password);
        RelativeLayout_login = findViewById(R.id.RelativeLayout_login);
        Signup = findViewById(R.id.Signup);

        RelativeLayout_login.setClickable((false));
        RelativeLayout_login.setEnabled(false);
        TextInputEditText_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s != null) {
                    inputEmail = s.toString();
                    RelativeLayout_login.setEnabled(validation());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        TextInputEditText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null) {
                    inputPassword = s.toString();
                    RelativeLayout_login.setEnabled(validation());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });



        RelativeLayout_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = TextInputEditText_id.getText().toString();
                String password = TextInputEditText_password.getText().toString();

                Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);

                startActivity(intent);
            }
        });

        Signup.setClickable(true);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean validation(){
        Log.d("BTS", inputEmail + " / " + inputPassword );
        return inputEmail.equals(emailOK) && inputPassword.equals(passwordOK);
    }

}
