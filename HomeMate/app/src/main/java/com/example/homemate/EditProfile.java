package com.example.homemate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity {
    int flag = 0 ;
    EditText firstName_editProfile, lastName_editProfile,
            phone_editProfile, secondaryEmail_editProfile;

    ImageView profilePhoto_editProfile;
    FloatingActionButton photoSelect_editProfile;
    Button save_editProfile;
    Uri uri;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference reference;
    StorageReference storageReference;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firstName_editProfile = findViewById(R.id.firstName_editProfile);
        lastName_editProfile = findViewById(R.id.lastName_editProfile);
        phone_editProfile = findViewById(R.id.phone_editProfile);
        secondaryEmail_editProfile = findViewById(R.id.secondaryEmail_editProfile);
        save_editProfile = findViewById(R.id.save_editProfile);
        profilePhoto_editProfile = findViewById(R.id.profilePhoto_editProfile);
        photoSelect_editProfile = findViewById(R.id.photoSelect_editProfile);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");


        reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                firstName_editProfile.setText(user.getFirstName());
                lastName_editProfile.setText(user.getLastName());
                phone_editProfile.setText(user.getPhone());
                secondaryEmail_editProfile.setText(user.getSecondary_email());
                if (user.getImageUrl() != null) {
                    Glide.with(EditProfile.this).load(user.getImageUrl().toString()).into(profilePhoto_editProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        photoSelect_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(EditProfile.this)
                        .crop(1f,1f) 			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        save_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                firebaseUser = auth.getCurrentUser();
                database=FirebaseDatabase.getInstance();
                reference=database.getReference("users");
                HashMap data = new HashMap();
                data.put("firstName",firstName_editProfile.getText().toString());
                data.put("lastName",lastName_editProfile.getText().toString());
                data.put("phone",phone_editProfile.getText().toString());
                data.put("secondary_email",secondaryEmail_editProfile.getText().toString());
                if(uri != null){
                    reference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            String imageUrl = user.getImageUrl();
                            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                            storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    storageReference = FirebaseStorage.getInstance().getReference().child("Profile Photos").child(uri.getLastPathSegment());
                                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while (!uriTask.isComplete());
                                            Uri urlImage = uriTask.getResult();
                                            String imageUrl = urlImage.toString();
                                            reference.child(firebaseUser.getUid()).child("imageUrl").setValue(imageUrl);
                                        }
                                    });
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                reference.child(firebaseUser.getUid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(EditProfile.this,HomePageActivity.class));
                        }
                    }
                });
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        profilePhoto_editProfile.setImageURI(uri);
    }
}