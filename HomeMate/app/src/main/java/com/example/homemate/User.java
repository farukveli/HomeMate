package com.example.homemate;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {

    String main_email, password, firstName, lastName, phone, secondary_email, faculty, department, uid, imageUrl, status, token;
    int grade;
    float distanceToCampus;
    int maxStayTime;

    public User() {

    }

    public User(String main_email, String password, String uid) {
        this.main_email = main_email;
        this.password = password;
        this.uid = uid;
    }

    protected User(Parcel in) {
        main_email = in.readString();
        password = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        phone = in.readString();
        secondary_email = in.readString();
        faculty = in.readString();
        department = in.readString();
        uid = in.readString();
        imageUrl = in.readString();
        status = in.readString();
        grade = in.readInt();
        distanceToCampus = in.readFloat();
        maxStayTime = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getMain_email() {
        return main_email;
    }

    public void setMain_email(String main_email) {
        this.main_email = main_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public float getDistanceToCampus() {
        return distanceToCampus;
    }

    public void setDistanceToCampus(float distanceToCampus) {
        this.distanceToCampus = distanceToCampus;
    }

    public int getMaxStayTime() {
        return maxStayTime;
    }

    public void setMaxStayTime(int maxStayTime) {
        this.maxStayTime = maxStayTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSecondary_email() {
        return secondary_email;
    }

    public void setSecondary_email(String secondary_email) {
        this.secondary_email = secondary_email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(main_email);
        parcel.writeString(password);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(phone);
        parcel.writeString(secondary_email);
        parcel.writeString(faculty);
        parcel.writeString(department);
        parcel.writeString(uid);
        parcel.writeString(imageUrl);
        parcel.writeString(status);
        parcel.writeInt(grade);
        parcel.writeFloat(distanceToCampus);
        parcel.writeInt(maxStayTime);
    }
}
