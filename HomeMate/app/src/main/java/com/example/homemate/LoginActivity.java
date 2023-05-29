package com.example.homemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email_login, password_login;
    Button login;

    TextView forgot_login, register_login;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_login = findViewById(R.id.email_register);
        password_login = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forgot_login = findViewById(R.id.forgot_login);
        register_login = findViewById(R.id.register_login);
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_login.getText().toString();
                String password = password_login.getText().toString();

                if(email.indexOf("@std.yildiz.edu.tr") != -1 ){
                    auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "Invalid Email!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgot_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "ON CLICK ÇALIŞIYOR", Toast.LENGTH_SHORT).show();
            }
        });
        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
}