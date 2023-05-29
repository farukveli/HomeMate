package com.example.homemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditLooking extends AppCompatActivity implements LocationListener {


    Spinner status_editLocation, time_editLocation;
    EditText day_editLocation;
    TextView disatanceText_editLocation,findLocation_editLocation;
    Button save_editLocation;

    ArrayAdapter<String> adapterStatus;

    ArrayAdapter<String> adapterTime;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;

    DatabaseReference reference,refLoc;

    ArrayList<String> status;
    User user;
    ArrayList<String> times;
    private LocationManager locationManager;
    private double latitude, longitude;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_looking);

        status_editLocation = findViewById(R.id.status_editLocation);
        time_editLocation = findViewById(R.id.time_editLocation);
        day_editLocation = findViewById(R.id.day_editLocation);
        findLocation_editLocation = findViewById(R.id.findLocation_editLocation);
        disatanceText_editLocation = findViewById(R.id.disatanceText_editLocation);
        save_editLocation = findViewById(R.id.save_editLocation);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("users");
        reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                if(user.getStatus().equals("Not Looking")){
                    time_editLocation.setEnabled(false);
                    day_editLocation.setEnabled(false);
                    findLocation_editLocation.setClickable(false);
                    status_editLocation.setSelection(0);
                }else if(user.getStatus().equals("Looking for Room")){
                    status_editLocation.setSelection(1);
                    findLocation_editLocation.setClickable(false);
                }else{
                    findLocation_editLocation.setClickable(true);
                    status_editLocation.setSelection(2);
                }

                if(user.getMaxStayTime()%30 == 0 && user.getMaxStayTime() != 0){
                    day_editLocation.setText(String.valueOf(user.getMaxStayTime()/30));
                    time_editLocation.setSelection(2);
                }else if(user.getMaxStayTime()%7==0 && user.getMaxStayTime() != 0){
                    day_editLocation.setText(String.valueOf(user.getMaxStayTime()/7));
                    time_editLocation.setSelection(1);
                }else{
                    day_editLocation.setText(String.valueOf(user.getMaxStayTime()));
                    time_editLocation.setSelection(0);
                }

                if(user.getDistanceToCampus()<1000){
                    disatanceText_editLocation.setText(user.getDistanceToCampus()+" m");
                }else if(user.getDistanceToCampus()%1000 == 0){
                    disatanceText_editLocation.setText(user.getDistanceToCampus()/1000+" km");
                }else{
                    disatanceText_editLocation.setText((float)user.getDistanceToCampus()/1000 -((float)user.getDistanceToCampus()/1000)%0.1 + " km"+" km");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        status = new ArrayList<>();
        status.add("Not Looking");
        status.add("Looking for Room");
        status.add("Looking for Mate");

        times = new ArrayList<>();
        times.add("Day");
        times.add("Week");
        times.add("Month");

        adapterStatus= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,status);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_editLocation.setAdapter(adapterStatus);

        adapterTime = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,times);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_editLocation.setAdapter(adapterTime);

        HashMap data = new HashMap();

        status_editLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String st = adapterView.getItemAtPosition(i).toString();
                if(st.equals("Not Looking")){
                    time_editLocation.setEnabled(false);
                    day_editLocation.setText("0");
                    day_editLocation.setEnabled(false);
                    findLocation_editLocation.setEnabled(false);
                }else{
                    time_editLocation.setEnabled(true);
                    day_editLocation.setEnabled(true);
                    findLocation_editLocation.setEnabled(true);
                }
                user.setStatus(st);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        time_editLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int number = Integer.parseInt(day_editLocation.getText().toString());
                String time = adapterView.getItemAtPosition(i).toString();
                switch (time){
                    case "Week":
                        number *= 7;
                        break;
                    case "Month":
                        number *= 30;
                        break;
                }
                user.setMaxStayTime(number);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                int number = Integer.parseInt(day_editLocation.getText().toString());
                user.setMaxStayTime(number);
            }
        });

        findLocation_editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                Location ytu = new Location("");
                ytu.setLatitude(41.02590);
                ytu.setLongitude(28.88949);
                Location home = new Location("");
                home.setLatitude(latitude);
                home.setLongitude(longitude);
                int distance = (int) home.distanceTo(ytu);
                if(distance>1000){
                    disatanceText_editLocation.setText(((float)distance/1000)-((float)distance/1000)%0.1 + " km");
                }else{
                    disatanceText_editLocation.setText(distance+" m");
                }
                user.setDistanceToCampus(distance-distance % 100);
            }
        });


        save_editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");
                refLoc = database.getReference("locations");
                data.put("status", user.getStatus());
                data.put("maxStayTime", user.getMaxStayTime());
                data.put("distanceToCampus", user.getDistanceToCampus());
                reference.child(user.getUid()).updateChildren(data);

                if(status_editLocation.getSelectedItemPosition() == 0 || status_editLocation.getSelectedItemPosition() == 1){
                    refLoc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                LocationClass loc = childSnapshot.getValue(LocationClass.class);
                                String uid = loc.getUserID();
                                if (uid != null && uid.equals(user.getUid())) {

                                    refLoc.child(childSnapshot.getKey()).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Child deleted successfully
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // An error occurred while deleting the child
                                                }
                                            });

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else if (status_editLocation.getSelectedItemPosition() == 2){
                    getLocation();
                    refLoc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int flag = 0;
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {       //SETTINGSTE KULLANILACAK
                                LocationClass loc = childSnapshot.getValue(LocationClass.class);
                                String tmp = loc.getUserID();
                                if (tmp.equals(user.getUid())){
                                    flag=1;
                                    HashMap data = new HashMap();
                                    data.put("userID",user.getUid());
                                    data.put("latitude",latitude);
                                    data.put("longitude",longitude);
                                    data.put("iconURI",user.getImageUrl());
                                    refLoc.child(childSnapshot.getKey()).updateChildren(data).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {

                                        }
                                    });
                                }
                            }
                            if(flag==0){
                                DatabaseReference newLocationRef = refLoc.push();
                                newLocationRef.child("userID").setValue(user.getUid());
                                newLocationRef.child("latitude").setValue(latitude);
                                newLocationRef.child("longitude").setValue(longitude);
                                newLocationRef.child("iconURI").setValue(user.getImageUrl());
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                startActivity(new Intent(EditLooking.this,HomePageActivity.class));
            }
        });

    }
    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, EditLooking.this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                latitude = lastKnownLocation.getLatitude();
                longitude = lastKnownLocation.getLongitude();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }
}