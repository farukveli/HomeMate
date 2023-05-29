package com.example.homemate;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class HomePageActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    ImageView profilePhoto_homePageActivity,moreIcon_homePageActivity;
    ImageView list_homePageActivity,littlePp_homePageActivity,filter_homePageActivity,requests_homePageActivity;
    EditText day_filter;
    TextView name_HomePageActivity,distanceText_filter;
    SeekBar distance_filter;
    Spinner time_filter;
    Button filter;
    ArrayList<LocationClass> locations;
    HashMap<String,MarkerOptions> markers;
    LinearLayout layout;
    CardView bigCard, littleCard, moreCard, filterCard;
    ImageButton settings_homePageActivity;

    ArrayAdapter<String> adapterTime;
    Button profileSettings_homePageActivity, lookingSettings_homePageActivity;
    Button educationSettings_homePageActivity;
    private GoogleMap mMap;
    double latitude, longitude;

    DatabaseReference ref;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    ArrayList<String> times;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        locations = new ArrayList<>();
        markers = new HashMap();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = createLocationRequest();
        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        ref.child("locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    LocationClass loc = snap.getValue(LocationClass.class);
                    locations.add(loc);
                    mappingLocations();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        locationCallback = new LocationCallback() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(HomePageActivity.this);

                    profilePhoto_homePageActivity = findViewById(R.id.profilePhoto_homePageActivity);
                    name_HomePageActivity = findViewById(R.id.name_HomePageActivity);
                    list_homePageActivity = findViewById(R.id.list_homePageActivity);
                    littlePp_homePageActivity = findViewById(R.id.littlePp_homePageActivity);
                    settings_homePageActivity = findViewById(R.id.settings_homePageActivity);
                    moreIcon_homePageActivity = findViewById(R.id.moreIcon_HomePageActivity);
                    bigCard = findViewById(R.id.bigCard);
                    littleCard = findViewById(R.id.littleCard);
                    moreCard = findViewById(R.id.moreCard);
                    layout = findViewById(R.id.settingsCategory);
                    profileSettings_homePageActivity = findViewById(R.id.profileSettings_homePageActivity);
                    lookingSettings_homePageActivity = findViewById(R.id.lookingSettings_homePageActivity);
                    educationSettings_homePageActivity = findViewById(R.id.educationSettings_homePageActivity);
                    requests_homePageActivity = findViewById(R.id.requests_homePageActivity);
                    filter_homePageActivity = findViewById(R.id.filter_homePageActivity);
                    day_filter = findViewById(R.id.day_filter);
                    time_filter = findViewById(R.id.time_filter);
                    distance_filter = findViewById(R.id.distance_filter);
                    distanceText_filter=findViewById(R.id.disatanceText_filter);
                    filter = findViewById(R.id.filter);
                    filterCard = findViewById(R.id.filterCard);

                    ref.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            name_HomePageActivity.setText(user.getFirstName() + " " + user.getLastName());
                            if (user.getImageUrl() != null) {
                                Glide.with(HomePageActivity.this).load(user.getImageUrl().toString()).into(profilePhoto_homePageActivity);
                                Glide.with(HomePageActivity.this).load(user.getImageUrl().toString()).into(littlePp_homePageActivity);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    requests_homePageActivity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(HomePageActivity.this,Requests.class));
                        }
                    });

                    list_homePageActivity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(HomePageActivity.this, ListUsersActivity.class));
                        }
                    });

                    settings_homePageActivity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(layout.getVisibility() == View.GONE){
                                layout.setVisibility(View.VISIBLE);
                            }else{
                                layout.setVisibility(View.GONE);
                            }

                        }
                    });

                    moreIcon_homePageActivity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            moreCard.setVisibility(View.GONE);
                            littleCard.setVisibility(View.GONE);
                            bigCard.setVisibility(View.VISIBLE);
                        }
                    });
                    profilePhoto_homePageActivity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            moreCard.setVisibility(View.VISIBLE);
                            littleCard.setVisibility(View.VISIBLE);
                            bigCard.setVisibility(View.GONE);
                        }
                    });

                    profileSettings_homePageActivity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(HomePageActivity.this,EditProfile.class));
                        }
                    });

                    educationSettings_homePageActivity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(HomePageActivity.this,EditEducation.class));
                        }
                    });

                    lookingSettings_homePageActivity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(HomePageActivity.this,EditLooking.class));
                        }
                    });

                    filter_homePageActivity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            filterCard.setVisibility(View.VISIBLE);
                            filter_homePageActivity.setClickable(false);
                            list_homePageActivity.setClickable(false);
                        }
                    });

                    day_filter.setText("15");
                    distance_filter.setProgress(100);
                    distanceText_filter.setText("10 km");

                    times = new ArrayList<>();
                    times.add("Day");
                    times.add("Week");
                    times.add("Month");

                    adapterTime = new ArrayAdapter<String>(HomePageActivity.this, android.R.layout.simple_spinner_item, times);
                    adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    time_filter.setAdapter(adapterTime);
                    time_filter.setSelection(0);


                    distance_filter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            if (i < 10)
                                distanceText_filter.setText("" + i * 100 + " m");
                            else if (i % 10 == 0)
                                distanceText_filter.setText(String.valueOf(i / 10) + " km");
                            else
                                distanceText_filter.setText(String.valueOf((float) i / 10) + " km");
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    filter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int duration;
                            int distance;
                            if(time_filter.getSelectedItemPosition()==1){
                                duration = Integer.parseInt(day_filter.getText().toString()) * 7;
                            } else if (time_filter.getSelectedItemPosition()==2) {
                                duration = Integer.parseInt(day_filter.getText().toString()) * 30;
                            }else{
                                duration = Integer.parseInt(day_filter.getText().toString());
                            }

                            distance = distance_filter.getProgress() * 100;
                            Location ytu = new Location("");
                            ytu.setLatitude(41.02590);
                            ytu.setLongitude(28.88949);
                            Location home = new Location("");


                            for(LocationClass loc : locations){
                                home.setLatitude(loc.getLatitude());
                                home.setLongitude(loc.getLongitude());
                                if(home.distanceTo(ytu) > distance ){
                                    markers.remove(loc.getUserID());
                                }else{
                                    if(markers.get(loc.getUserID())== null){
                                        markers.put(loc.getUserID(),new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude())));
                                    }
                                }
                                ref.child("users").child(loc.getUserID()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User x = snapshot.getValue(User.class);
                                        if(x.getMaxStayTime() < duration){
                                            markers.remove(loc.getUserID());
                                        }else{
                                            if(markers.get(loc.getUserID())== null){
                                                markers.put(loc.getUserID(),new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude())));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            mMap.clear();
                            filter_homePageActivity.setClickable(true);
                            list_homePageActivity.setClickable(true);
                            filterCard.setVisibility(View.GONE);
                            mMap.clear();
                            showLocations();
                        }
                    });
                }
            }
        };

        requestLocationPermissions();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        // Add a marker in Sydney and move the camera
        LatLng curr = new LatLng(latitude, longitude);
        MarkerOptions marker = new MarkerOptions().position(curr).title("I'm here...");
        if(markers.get("curr")==null){
            markers.put("curr",marker);
        }else{
            if(!markers.get("curr").equals(marker)){
                markers.remove("curr");
                markers.put("curr",marker);
            }
        }
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
        mMap.getUiSettings().setZoomControlsEnabled(true); // + -
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        float zoomLevel = 12f; // Specify the desired zoom
        float zoomLevel2 = 17f;
        LatLng ytu = new LatLng(41.02590, 28.88949);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, zoomLevel));
        showLocations();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if(marker.getTag() != null){
                    String userId = marker.getTag().toString();
                    Intent intent = new Intent(HomePageActivity.this,ViewProfile.class);
                    intent.putExtra("transfer", userId);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }


    @Override
    protected void onStart() {
        super.onStart();
        requestLocationPermissions();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Konum izni reddedildi.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @NonNull
    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); // Konum güncelleme aralığı (milisaniye cinsinden)
        locationRequest.setFastestInterval(500); // En hızlı konum güncelleme aralığı (milisaniye cinsinden)
        locationRequest.setSmallestDisplacement(10);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Konum doğruluğu önceliği
        return locationRequest;
    }

    public void mappingLocations(){
        for(LocationClass loc : locations){
            MarkerOptions marker = new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude()));
            markers.put(loc.getUserID(),marker);
        }

    }

    public void showLocations(){

        for(String key : markers.keySet()){
            if(!key.equals("curr")){
                mMap.addMarker(markers.get(key)).setTag(key);
            }
        }
    }
}

















