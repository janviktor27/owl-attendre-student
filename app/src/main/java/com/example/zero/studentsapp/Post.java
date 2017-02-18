package com.example.zero.studentsapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zero on 17/02/08.
 */

public class Post {
    @SerializedName("std_id")
    public int std_id;
    @SerializedName("student_cin")
    public String student_cin;
    @SerializedName("student_fname")
    public String student_fname;
    @SerializedName("student_lname")
    public String student_lname;
    @SerializedName("course_id")
    public String course_id;
}
