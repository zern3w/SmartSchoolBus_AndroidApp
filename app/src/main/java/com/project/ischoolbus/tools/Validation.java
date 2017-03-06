package com.project.ischoolbus.tools;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Puttipong New on 31/3/2559.
 */
public class Validation {

    public static boolean isMinimumLength(String entered, int length) {
        if (entered.length() >= length)
            return true;
        else
            return false;
    }

    public static boolean isFieldNotEmpty(String entered) {
        if (entered.equals("")) {
            return false;
        } else
            return true;
    }

    public static boolean isValidName(String entered) {
        return entered.matches("^[\\p{L} .'-]+$");
    }

    public static boolean isSpace(String entered) {
        return entered.contains(" ");
    }

    public static boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isStringEqual(String enter, String reenter) {
        if (enter.equals(reenter)) {
            return true;
        } else
            return false;
    }

    public static boolean isValidPlate(String plate) {
        String PLATE_PATTERN = "[0-9]{0,1}" + "[a-zA-Zก-ฮ]{2,3}" + "[0-9]{1,4}";
        Pattern pattern = Pattern.compile(PLATE_PATTERN);
        Matcher matcher = pattern.matcher(plate);
        return matcher.matches();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isQrValid(String qrValidate) {    //SchoolBusApp_15_NEW
        String PLATE_PATTERN = "SchoolBusApp_" + "[0-9]{1,4}" + "_" + "[a-zA-Zก-ฮ]{1,12}";
        Pattern pattern = Pattern.compile(PLATE_PATTERN);
        Matcher matcher = pattern.matcher(qrValidate);
        return matcher.matches();
    }

    public static boolean isExceedSpeedLimit (double speed) {
        if (speed > 40) {
            return true;
        } else {
            return false;
        }
    }

//    public static boolean isScannedValid(String first, String second) {    //SchoolBusApp_15_NEW
//        if (first == second){
//            return false;
//        }else return true;
//    }
}