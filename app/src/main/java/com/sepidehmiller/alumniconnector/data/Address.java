package com.sepidehmiller.alumniconnector.data;

public class Address {

  private String address;
  private String city;
  private String state;
  private String zipcode;
  private String country;

  public Address() {

  }

  public Address(String address, String city, String state, String zipcode, String country) {
    this.address = address;
    this.city = city;

    // Check for null values if the person is outside the US.
    if (state != null) {
      this.state = state;
    }

    if (zipcode != null) {
      this.zipcode = zipcode;
    }

    this.country = country;
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getZipcode() {
    return zipcode;
  }

  public String getCountry() {
    return country;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
