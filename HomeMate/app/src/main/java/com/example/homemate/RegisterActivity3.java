package com.example.homemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity3 extends AppCompatActivity {

    Spinner faculty_register3,department_register3, grade_register3;
    Button next_register3;
    String faculty;
    String department;
    int grade;
    ArrayAdapter<CharSequence> adapterFaculty;
    ArrayAdapter<CharSequence> adapterDepartment;
    ArrayAdapter<Integer> adapterYear;
    ArrayList<Integer> years;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        User user = (User) getIntent().getParcelableExtra("User");

        faculty_register3 = findViewById(R.id.faculty_register3);
        department_register3 = findViewById(R.id.department_register3);
        grade_register3 = findViewById(R.id.grade_register3);
        next_register3 = findViewById(R.id.next_register3);

        adapterFaculty = ArrayAdapter.createFromResource(this,R.array.FacultyList, android.R.layout.simple_spinner_item);
        adapterFaculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty_register3.setAdapter(adapterFaculty);

        faculty_register3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                faculty = adapterView.getItemAtPosition(i).toString();
                setDepartmentSpinner(faculty);
                user.setFaculty(faculty);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        department_register3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                department=adapterView.getItemAtPosition(i).toString();
                user.setDepartment(department);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        grade_register3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grade=Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                user.setGrade(grade);
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
        grade_register3.setAdapter(adapterYear);

        next_register3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database=FirebaseDatabase.getInstance();
                reference=database.getReference("users");
                HashMap data = new HashMap();
                data.put("faculty",user.getFaculty());
                data.put("department",user.getDepartment());
                data.put("grade",user.getGrade());
                reference.child(user.getUid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(RegisterActivity3.this,RegisterActivity4.class);
                            intent.putExtra("User", user);
                            startActivity(intent);
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
        department_register3.setAdapter(adapterDepartment);
    }

}