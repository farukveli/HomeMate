package com.example.homemate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity2 extends AppCompatActivity {
    EditText firstName_register2, lastName_register2,secondaryEmail_register2, phone_register2;
    Button next_register2;
    FloatingActionButton photoSelect_register2;
    ImageView profilePhoto_register2;

    String imageUrl,token;

    Uri uri;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference reference, postRef;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);


        firstName_register2 = findViewById(R.id.firstName_register2);
        lastName_register2 = findViewById(R.id.lastName_register2);
        phone_register2 = findViewById(R.id.phone_register2);
        secondaryEmail_register2 = findViewById(R.id.secondaryEmail_register2);
        next_register2 = findViewById(R.id.next_register2);
        photoSelect_register2 = findViewById(R.id.photoSelect_register2);
        profilePhoto_register2 = findViewById(R.id.profilePhoto_homePageActivity);
        auth = FirebaseAuth.getInstance();

        photoSelect_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(RegisterActivity2.this)
                        .crop(1f,1f) 			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        next_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = getIntent().getStringExtra("Email");
                String password = getIntent().getStringExtra("Password");
                String firstName = firstName_register2.getText().toString();
                String lastName = lastName_register2.getText().toString();
                String secondaryEmail = secondaryEmail_register2.getText().toString();
                String phone = phone_register2.getText().toString();
                database=FirebaseDatabase.getInstance();
                reference=database.getReference("users");
                storageReference = FirebaseStorage.getInstance().getReference().child("Profile Photos").child(uri.getLastPathSegment());

                User user = new User(email, password, auth.getUid());

                if(firstName.isEmpty()){
                    firstName_register2.setError("First Name cannot be empty!");
                }else if (lastName.isEmpty()){
                    lastName_register2.setError("Last Name cannot be empty!");
                }else if (phone.isEmpty()){
                    phone_register2.setError("Phone cannot be empty");
                } else if (uri == null) {
                    profilePhoto_register2.setImageResource(R.drawable.pp);
                    uri = Uri.parse(profilePhoto_register2.getTag().toString());
                } else{
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            firebaseUser = auth.getCurrentUser();
                            String uid = firebaseUser.getUid();
                            user.setUid(uid);
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri urlImage = uriTask.getResult();
                            imageUrl = urlImage.toString();

                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if(!task.isSuccessful()){
                                        return;
                                    }
                                    token = task.getResult();
                                    user.setToken(token);
                                }
                            });

                            user.setImageUrl(imageUrl);
                            user.setFirstName(firstName);
                            user.setLastName(lastName);
                            user.setPhone(phone);
                            user.setSecondary_email(secondaryEmail);
                            reference.child(uid).setValue(user);
                            Intent intent = new Intent(RegisterActivity2.this,RegisterActivity3.class);
                            intent.putExtra("User", user);
                            startActivity(intent);
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        profilePhoto_register2.setImageURI(uri);
    }



}