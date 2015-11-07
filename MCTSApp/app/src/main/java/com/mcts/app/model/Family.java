package com.mcts.app.model;

/**
 * Created by Raj on 10/11/2015.
 */
public class Family {

    String id;
    String familyNumber;
    String houseNumber;
    String address;
    String cast;
    String religion;
    String bpl;
    String lat;
    String lng;
    String villageName;
    String memberName;
    String memberId;
    byte[] userImageArray;


    public byte[] getUserImageArray() {
        return userImageArray;
    }

    public void setUserImageArray(byte[] userImageArray) {
        this.userImageArray = userImageArray;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmamtaFamilyId() {
        return familyNumber;
    }

    public void setEmamtaFamilyId(String familyNumber) {
        this.familyNumber = familyNumber;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getBpl() {
        return bpl;
    }

    public void setBpl(String bpl) {
        this.bpl = bpl;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
