package com.example.homemate;

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

public class RegisterActivity extends AppCompatActivity {

    EditText email_register1, password_register1, confirmPassword_register1;
    Button register_register1;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_register1 = findViewById(R.id.email_register1);
        password_register1 = findViewById(R.id.password_register1);
        confirmPassword_register1 = findViewById(R.id.confirmPassword_register1);
        register_register1 = findViewById(R.id.next_register2);
        auth = FirebaseAuth.getInstance();

        register_register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,RegisterActivity2.class));
                String email = email_register1.getText().toString();
                String password = password_register1.getText().toString();
                String confirmPassword = confirmPassword_register1.getText().toString();

                if(email.indexOf("@std.yildiz.edu.tr") != -1){
                    if(!password.isEmpty()){
                        if(password.equals(confirmPassword)){
                            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
                                        intent.putExtra("Email", email);
                                        intent.putExtra("Password", password);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Register Failed "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "Passwords are not the same", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                } else if (email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}