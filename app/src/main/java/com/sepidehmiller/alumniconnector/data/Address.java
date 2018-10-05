package com.sepidehmiller.alumniconnector.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

  private String street;
  private String city;
  private String state;
  private String zipcode;
  private String country;

  public Address() {

  }

  public Address(String street, String city, String state, String zipcode, String country) {
    this.street = street;
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

  public Address(Parcel in) {
    street = in.readString();
    city = in.readString();
    state = in.readString();
    zipcode = in.readString();
    country = in.readString();
  }

  public String getStreet() {
    return street;
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

  public void setStreet(String street) {
    this.street = street;
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

  //http://www.vogella.com/tutorials/AndroidParcelable/article.html

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(street);
    dest.writeString(city);
    dest.writeString(state);
    dest.writeString(zipcode);
    dest.writeString(country);
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public Address createFromParcel(Parcel in) {
      return new Address(in);
    }

    public Address[] newArray(int size) {
      return new Address[size];
    }
  };
}
