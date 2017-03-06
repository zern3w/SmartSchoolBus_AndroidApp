package com.project.ischoolbus.model;

/**
 * Created by Puttipong New on 20/2/2559.
 */
public class Driver {

    /**
     * driver_id : 0045
     * driver_firstname : Puttipong
     * driver_lastname : Tadang
     * email : zer.n3w@gmail.com
     * password : 1596321478
     * mobile_tel : 0907507501
     * plate_number : 1AA1111
     * photo :
     */

    private String driver_id;
    private String driver_firstname;
    private String driver_lastname;
    private String email;
    private String password;
    private String phone;
    private String platenum;
    private String photo;

    public Driver(String driver_id, String driver_firstname, String driver_lastname, String email, String password, String phone, String platenum, String photo) {
        this.driver_id = driver_id;
        this.driver_firstname = driver_firstname;
        this.driver_lastname = driver_lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.platenum = platenum;
        this.photo = photo;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public void setDriver_firstname(String driver_firstname) {
        this.driver_firstname = driver_firstname;
    }

    public void setDriver_lastname(String driver_lastname) {
        this.driver_lastname = driver_lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile_tel(String mobile_tel) {
        this.phone = mobile_tel;
    }

    public void setPlate_number(String plate_number) {
        this.platenum = plate_number;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getDriver_firstname() {
        return driver_firstname;
    }

    public String getDriver_lastname() {
        return driver_lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile_tel() {
        return phone;
    }

    public String getPlate_number() {
        return platenum;
    }

    public String getPhoto() {
        return photo;
    }
}
