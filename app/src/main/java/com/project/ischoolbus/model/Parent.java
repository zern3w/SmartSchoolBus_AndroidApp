package com.project.ischoolbus.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Puttipong New on 20/3/2559.
 */
public class Parent {

    /**
     * parent_id : 0046
     * parent_firstname : asfhjkdshk
     * parent_lastname : sjkdhflkajsd
     * mobile_tel : sdahfk
     * photo :
     */

    @SerializedName("parent_id")
    private String parentId;
    @SerializedName("parent_firstname")
    private String parentFirstname;
    @SerializedName("parent_lastname")
    private String parentLastname;
    @SerializedName("mobile_tel")
    private String mobileTel;

    private String photo;

    public Parent() {

    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

}