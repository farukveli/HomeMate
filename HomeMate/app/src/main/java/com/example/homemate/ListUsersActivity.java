package com.example.homemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ListUsersActivity extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;

    RCAdapter rcAdapter;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    HashMap users;

    ArrayList<RCModel> arrayList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        arrayList=new ArrayList<>();
        rcAdapter=new RCAdapter(this,arrayList,this);
        recyclerView.setAdapter(rcAdapter);

        users = new HashMap();


        reference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren()){
                    User user = s.getValue(User.class);
                    users.put(s.getKey().toString(),user);
                }

                users.forEach((k,v) -> insertRCModel((String) k, (User) v,arrayList));

                rcAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void insertRCModel (String key, User u,ArrayList<RCModel> arrayList ){
        RCModel rcModel = new RCModel(u.getFirstName()+" "+u.getLastName(), u.getImageUrl());
        rcModel.setUid(key);
        arrayList.add(rcModel);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ListUsersActivity.this,ViewProfile.class);
        RCModel rcModel1 = arrayList.get(position);
        intent.putExtra("transfer",rcModel1.uid.toString());
        startActivity(intent);
    }
}