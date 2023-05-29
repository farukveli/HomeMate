package com.example.homemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class EditEducation extends AppCompatActivity {

    Spinner faculty_editEducation,department_editEducation, grade_editEducation;
    Button save_editEducation;
    String faculty;
    String department;
    int grade;
    ArrayAdapter<CharSequence> adapterFaculty;
    ArrayAdapter<CharSequence> adapterDepartment;
    ArrayAdapter<Integer> adapterYear;
    ArrayList<Integer> years;
    User user;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_education);

        faculty_editEducation = findViewById(R.id.faculty_editEducation);
        department_editEducation = findViewById(R.id.department_editEducation);
        grade_editEducation = findViewById(R.id.grade_editEducation);
        save_editEducation = findViewById(R.id.save_editEducation);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("users");
        reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                int i =0;
                while (!user.getFaculty().equals(faculty_editEducation.getItemAtPosition(i).toString())){
                    i++;
                }
                faculty_editEducation.setSelection(i);
                setDepartmentSpinner(user.getFaculty());
                i=0;
                while (!user.getDepartment().equals(department_editEducation.getItemAtPosition(i).toString())){
                    i++;
                }
                department_editEducation.setSelection(i);
                grade_editEducation.setSelection(user.getGrade()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapterFaculty = ArrayAdapter.createFromResource(this,R.array.FacultyList, android.R.layout.simple_spinner_item);
        adapterFaculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty_editEducation.setAdapter(adapterFaculty);


        HashMap data = new HashMap();
        faculty_editEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                faculty = adapterView.getItemAtPosition(i).toString();
                data.put("faculty", faculty);
                setDepartmentSpinner(faculty);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        department_editEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                department=adapterView.getItemAtPosition(i).toString();
                data.put("department", department);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        grade_editEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grade=Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                data.put("grade", grade);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        years = new ArrayList<>();
        for(int i=1;i<=4;i++)
            years.add(i);

        adapterYear = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,years);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade_editEducation.setAdapter(adapterYear);

        save_editEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(EditEducation.this,HomePageActivity.class));
                        }
                    }
                });
            }
        });


    }
    public void setDepartmentSpinner(String faculty){
        switch (faculty){
            case "Education":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.Education, android.R.layout.simple_spinner_item);
                break;
            case "Electrical and Electronics":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.ElectricalandElectronics, android.R.layout.simple_spinner_item);
                break;
            case "Art and Science":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.ArtandScience, android.R.layout.simple_spinner_item);
                break;
            case "Naval Architecture and Maritime":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.NavalArchitectureandMaritime, android.R.layout.simple_spinner_item);
                break;
            case "Economic and Administrative Sciences":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.EconomicandAdministrativeSciences, android.R.layout.simple_spinner_item);
                break;
            case "Civil Engineering":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.CivilEngineering, android.R.layout.simple_spinner_item);
                break;
            case "Chemical and Metallurgical Engineering":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.ChemicalandMetallurgicalEngineering, android.R.layout.simple_spinner_item);
                break;
            case "Mechanical Engineering":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.MechanicalEngineering, android.R.layout.simple_spinner_item);
                break;
            case "Architecture":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.Architecture, android.R.layout.simple_spinner_item);
                break;
            case "Art and Design":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.ArtandDesign, android.R.layout.simple_spinner_item);
                break;
            case "Applied Sciences":
                adapterDepartment = ArrayAdapter.createFromResource(this,R.array.AppliedSciences, android.R.layout.simple_spinner_item);
                break;
        }
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_editEducation.setAdapter(adapterDepartment);
    }
}