package com.sepidehmiller.alumniconnector.network.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProvidedLocation {
  @SerializedName("street")
  @Expose
  private String street;
  @SerializedName("city")
  @Expose
  private String city;
  @SerializedName("state")
  @Expose
  private String state;
  @SerializedName("postalCode")
  @Expose
  private String postalCode;

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

}
