package com.example.bts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText TextInputEditText_name, TextInputEditText_id, TextInputEditText_password,
            TextInputEditText_password_check, TextInputEditText_address, TextInputEditText_phone_num;

    String name, id, password, password_ch, address, phone;

    Button Sign;
    Button Cancel;
    public int TERMS_AGREE_1 = 0;

    AppCompatCheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextInputEditText_name = findViewById(R.id.TextInputEditText_name);
        TextInputEditText_id = findViewById(R.id.TextInputEditText_id);
        TextInputEditText_password = findViewById(R.id.TextInputEditText_password);
        TextInputEditText_password_check = findViewById(R.id.TextInputEditText_password_check);
        TextInputEditText_address = findViewById(R.id.TextInputEditText_address);
        TextInputEditText_phone_num = findViewById(R.id.TextInputEditText_phone_num);

        check = findViewById(R.id.check);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    TERMS_AGREE_1 = 1;
                } else {

                    TERMS_AGREE_1 = 0;
                }
            }
        });

        Sign = findViewById(R.id.Sign);
        Sign.setClickable(true);
        Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = TextInputEditText_name.getText().toString();
                id = TextInputEditText_id.getText().toString();
                password = TextInputEditText_password.getText().toString();
                password_ch = TextInputEditText_password_check.getText().toString();
                address = TextInputEditText_address.getText().toString();
                phone = TextInputEditText_phone_num.getText().toString();

                if (password.equals(password_ch)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "회원가입완료", Toast.LENGTH_LONG);
                    toast.show();


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "비밀번호불일치!!", Toast.LENGTH_LONG);
                    toast.show();
                }

                if (TERMS_AGREE_1 == 1) {
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "약관을 체크해주세요!!", Toast.LENGTH_LONG);
                    toast.show();

                    return;
                }
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Cancel = findViewById(R.id.Cancel);
        Cancel.setClickable(true);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
