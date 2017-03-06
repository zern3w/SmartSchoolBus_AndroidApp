package com.project.ischoolbus.tools;

/**
 * Created by Puttipong New on 7/2/2559.
 */
public class URL {
//    public static String BASE_URL = "http://smartschoolbus.esy.es/SmartSchoolBus/";
    public static String BASE_URL = "http://192.168.137.1/smartschoolbus/";

    //          DRIVER
    public static String SIGNUP = BASE_URL + "driver_register.php";
    public static String LOGIN = BASE_URL + "driver_login.php";
    public static String DRIVER_DETAIL = BASE_URL + "driver_detail.php";
    public static String EDITPROFILE = BASE_URL + "driver_editprofile.php";

    //          PARENT
    public static String ADD_PARENT = BASE_URL + "parent_adding.php";
    public static String LIST_PARENT = BASE_URL + "parent_list.php";
    public static String DELETE_PARENT = BASE_URL + "parent_delete.php";
    public static String PARENT_DETAIL = BASE_URL + "parent_detail.php";

    //          STUDENT
    public static String ADD_STUDENT = BASE_URL + "student_adding.php";
    public static String LIST_STUDENT = BASE_URL + "student_list.php";
    public static String DELETE_STUDENT = BASE_URL + "student_delete.php";
    public static String STUDENT_DETAIL = BASE_URL + "student_detail.php";
    public static String STUDENT_CHKATTENDANCE = BASE_URL + "student_chkattendance.php";

    //          OTHER
    public static String FORGOTPASSWORD = BASE_URL + "forgotPw.php";
    public static String VALIDATE_OTP = BASE_URL + "validateOTP.php";
    public static String CHANGEPASSWORD = BASE_URL + "changePw.php";
}