package com.sepidehmiller.alumniconnector.network.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {
  @SerializedName("street")
  @Expose
  private String street;
  @SerializedName("adminArea6")
  @Expose
  private String adminArea6;
  @SerializedName("adminArea6Type")
  @Expose
  private String adminArea6Type;
  @SerializedName("adminArea5")
  @Expose
  private String adminArea5;
  @SerializedName("adminArea5Type")
  @Expose
  private String adminArea5Type;
  @SerializedName("adminArea4")
  @Expose
  private String adminArea4;
  @SerializedName("adminArea4Type")
  @Expose
  private String adminArea4Type;
  @SerializedName("adminArea3")
  @Expose
  private String adminArea3;
  @SerializedName("adminArea3Type")
  @Expose
  private String adminArea3Type;
  @SerializedName("adminArea1")
  @Expose
  private String adminArea1;
  @SerializedName("adminArea1Type")
  @Expose
  private String adminArea1Type;
  @SerializedName("postalCode")
  @Expose
  private String postalCode;
  @SerializedName("geocodeQualityCode")
  @Expose
  private String geocodeQualityCode;
  @SerializedName("geocodeQuality")
  @Expose
  private String geocodeQuality;
  @SerializedName("dragPoint")
  @Expose
  private Boolean dragPoint;
  @SerializedName("sideOfStreet")
  @Expose
  private String sideOfStreet;
  @SerializedName("linkId")
  @Expose
  private String linkId;
  @SerializedName("unknownInput")
  @Expose
  private String unknownInput;
  @SerializedName("type")
  @Expose
  private String type;
  @SerializedName("latLng")
  @Expose
  private LatLng latLng;
  @SerializedName("displayLatLng")
  @Expose
  private DisplayLatLng displayLatLng;
  @SerializedName("mapUrl")
  @Expose
  private String mapUrl;

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getAdminArea6() {
    return adminArea6;
  }

  public void setAdminArea6(String adminArea6) {
    this.adminArea6 = adminArea6;
  }

  public String getAdminArea6Type() {
    return adminArea6Type;
  }

  public void setAdminArea6Type(String adminArea6Type) {
    this.adminArea6Type = adminArea6Type;
  }

  public String getAdminArea5() {
    return adminArea5;
  }

  public void setAdminArea5(String adminArea5) {
    this.adminArea5 = adminArea5;
  }

  public String getAdminArea5Type() {
    return adminArea5Type;
  }

  public void setAdminArea5Type(String adminArea5Type) {
    this.adminArea5Type = adminArea5Type;
  }

  public String getAdminArea4() {
    return adminArea4;
  }

  public void setAdminArea4(String adminArea4) {
    this.adminArea4 = adminArea4;
  }

  public String getAdminArea4Type() {
    return adminArea4Type;
  }

  public void setAdminArea4Type(String adminArea4Type) {
    this.adminArea4Type = adminArea4Type;
  }

  public String getAdminArea3() {
    return adminArea3;
  }

  public void setAdminArea3(String adminArea3) {
    this.adminArea3 = adminArea3;
  }

  public String getAdminArea3Type() {
    return adminArea3Type;
  }

  public void setAdminArea3Type(String adminArea3Type) {
    this.adminArea3Type = adminArea3Type;
  }

  public String getAdminArea1() {
    return adminArea1;
  }

  public void setAdminArea1(String adminArea1) {
    this.adminArea1 = adminArea1;
  }

  public String getAdminArea1Type() {
    return adminArea1Type;
  }

  public void setAdminArea1Type(String adminArea1Type) {
    this.adminArea1Type = adminArea1Type;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getGeocodeQualityCode() {
    return geocodeQualityCode;
  }

  public void setGeocodeQualityCode(String geocodeQualityCode) {
    this.geocodeQualityCode = geocodeQualityCode;
  }

  public String getGeocodeQuality() {
    return geocodeQuality;
  }

  public void setGeocodeQuality(String geocodeQuality) {
    this.geocodeQuality = geocodeQuality;
  }

  public Boolean getDragPoint() {
    return dragPoint;
  }

  public void setDragPoint(Boolean dragPoint) {
    this.dragPoint = dragPoint;
  }

  public String getSideOfStreet() {
    return sideOfStreet;
  }

  public void setSideOfStreet(String sideOfStreet) {
    this.sideOfStreet = sideOfStreet;
  }

  public String getLinkId() {
    return linkId;
  }

  public void setLinkId(String linkId) {
    this.linkId = linkId;
  }

  public String getUnknownInput() {
    return unknownInput;
  }

  public void setUnknownInput(String unknownInput) {
    this.unknownInput = unknownInput;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public LatLng getLatLng() {
    return latLng;
  }

  public void setLatLng(LatLng latLng) {
    this.latLng = latLng;
  }

  public DisplayLatLng getDisplayLatLng() {
    return displayLatLng;
  }

  public void setDisplayLatLng(DisplayLatLng displayLatLng) {
    this.displayLatLng = displayLatLng;
  }

  public String getMapUrl() {
    return mapUrl;
  }

  public void setMapUrl(String mapUrl) {
    this.mapUrl = mapUrl;
  }
}
