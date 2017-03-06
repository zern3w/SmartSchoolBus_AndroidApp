package com.project.ischoolbus.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Puttipong New on 6/5/2559.
 */
public class Student {
    @SerializedName("student_id")
    private String studentId;
    @SerializedName("student_firstname")
    private String studentFirstname;
    @SerializedName("student_lastname")
    private String studentLastname;
    @SerializedName("student_nickname")
    private String studentNickname;
    @SerializedName("parent_phone")
    private String parentPhone;
    @SerializedName("emergency_tel")
    private String emergencyTel;
    @SerializedName("parent_firstname")
    private String parentFirstname;
    @SerializedName("parent_lastname")
    private String parentLastname;

    public Student(String photo, String studentFirstname, String studentLastname, String studentNickname, String schoolName,
                   String mobileTel, String studentId, String emergencyTel, String parentPhone, String parentFirstname, String parentLastname) {
        this.photo = photo;
        this.studentFirstname = studentFirstname;
        this.studentLastname = studentLastname;
        this.studentNickname = studentNickname;
        this.schoolName = schoolName;
        this.mobileTel = mobileTel;
        this.studentId = studentId;
        this.parentPhone = parentPhone;
        this.emergencyTel =  emergencyTel;
    }

    public String getEmergencyTel() {
        return emergencyTel;
    }
    public void setEmergencyTel(String emergencyTel) {
        this.emergencyTel = emergencyTel;
    }
    public String getParentPhone() {
        return parentPhone;
    }
    public void setParentPhone(String emergencyTel) {
        this.parentPhone = parentPhone;
    }
    public String getParentFirstname() {
        return parentFirstname;
    }
    public void setParentFirstname(String parentFirstname) {
        this.parentFirstname = parentFirstname;
    }
    public String getParentLastname() {
        return parentLastname;
    }
    public void setParentLastname(String parentLastname) {
        this.parentLastname = parentLastname;
    }

    @SerializedName("school_name")

    private String schoolName;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentFirstname() {
        return studentFirstname;
    }

    public void setStudentFirstname(String studentFirstname) {
        this.studentFirstname = studentFirstname;
    }

    public String getStudentLastname() {
        return studentLastname;
    }

    public void setStudentLastname(String studentLastname) {
        this.studentLastname = studentLastname;
    }

    public String getStudentNickname() {
        return studentNickname;
    }

    public void setStudentNickname(String studentNickname) {
        this.studentNickname = studentNickname;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMobileTel() {
        return mobileTel;
    }

    public void setMobileTel(String mobileTel) {
        this.mobileTel = mobileTel;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @SerializedName("phone")

    private String mobileTel;

    private String photo;


}
