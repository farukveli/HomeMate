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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity4 extends AppCompatActivity implements LocationListener {

    Spinner status_register4, time_register4;
    EditText day_register4;
    TextView disatanceText_register4,findLocation_register4;
    Button register_register4;
    String token;
    ArrayAdapter<String> adapterStatus;

    ArrayAdapter<String> adapterTime;

    FirebaseDatabase database;

    DatabaseReference reference, refLoc;

    ArrayList<String> status;

    ArrayList<String> times;
    LocationManager locationManager;
    Double latitude, longitude;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);

        User user = (User) getIntent().getParcelableExtra("User");

        status_register4 = findViewById(R.id.status_register4);
        time_register4 = findViewById(R.id.time_register4);
        day_register4 = findViewById(R.id.day_register4);
        disatanceText_register4 = findViewById(R.id.disatanceText_register4);
        register_register4 = findViewById(R.id.register_register4);
        findLocation_register4 = findViewById(R.id.findLocation_register4);

        day_register4.setText("0");
        disatanceText_register4.setText("0 m");

        status = new ArrayList<>();
        status.add("Not Looking");
        status.add("Looking for Room");
        status.add("Looking for Mate");

        times = new ArrayList<>();
        times.add("Day");
        times.add("Week");
        times.add("Month");

        findLocation_register4.setClickable(false);

        adapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_register4.setAdapter(adapterStatus);

        adapterTime = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, times);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_register4.setAdapter(adapterTime);

        status_register4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String st = adapterView.getItemAtPosition(i).toString();
                if (st.equals("Not Looking")) {
                    time_register4.setEnabled(false);
                    day_register4.setEnabled(false);
                } else {
                    time_register4.setEnabled(true);
                    day_register4.setEnabled(true);
                    if(st.equals("Looking for Mate")){
                        findLocation_register4.setClickable(true);
                    }else{
                        findLocation_register4.setClickable(false);
                    }
                }
                user.setStatus(st);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String st = "Not Looking";
                user.setStatus(st);
            }
        });

        time_register4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int number = Integer.parseInt(day_register4.getText().toString());
                String time = adapterView.getItemAtPosition(i).toString();
                switch (time) {
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
                int number = Integer.parseInt(day_register4.getText().toString());
                user.setMaxStayTime(number);
            }
        });

        findLocation_register4.setOnClickListener(new View.OnClickListener() {
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
                    disatanceText_register4.setText(((float)distance/1000)-((float)distance/1000)%0.1 + " km");
                }else{
                    disatanceText_register4.setText(distance+" m");
                }
                user.setDistanceToCampus(distance);
            }
        });


        register_register4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");
                refLoc = database.getReference("locations");
                HashMap data = new HashMap();
                data.put("status", user.getStatus());
                data.put("maxStayTime", user.getMaxStayTime());
                data.put("distanceToCampus", user.getDistanceToCampus());
                reference.child(user.getUid()).updateChildren(data);

                if (status_register4.getSelectedItemPosition() == 0 || status_register4.getSelectedItemPosition() == 1) {
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
                } else if (status_register4.getSelectedItemPosition() == 2) {
                    getLocation();
                    DatabaseReference newLocationRef = refLoc.push();
                    newLocationRef.child("userID").setValue(user.getUid());
                    newLocationRef.child("latitude").setValue(latitude);
                    newLocationRef.child("longitude").setValue(longitude);
                    newLocationRef.child("iconURI").setValue(user.getImageUrl());
                }
                startActivity(new Intent(RegisterActivity4.this, HomePageActivity.class));
            }
        });


    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, RegisterActivity4.this);
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