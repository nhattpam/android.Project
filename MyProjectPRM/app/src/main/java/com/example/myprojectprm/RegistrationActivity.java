package com.example.myprojectprm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Register: Khang
public class RegistrationActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etFullName, etAddress, etPhone;
    private Button btnRegister;

    private TextView btnLogin;


    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etFullName = findViewById(R.id.et_fullName);
        etAddress = findViewById(R.id.et_address);
        etPhone = findViewById(R.id.et_phone);
        btnRegister = findViewById(R.id.btn_register);

        //login button
        btnLogin = findViewById(R.id.tv_login);

        databaseHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String fullName = etFullName.getText().toString();
                String address = etAddress.getText().toString();
                String phone = etPhone.getText().toString();

                if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else if (databaseHelper.isUsernameExists(username)) {
                    Toast.makeText(RegistrationActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.addUser(username, password, fullName, address, phone, RegistrationActivity.this);
                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
//                    finish();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}