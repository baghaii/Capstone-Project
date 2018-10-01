package com.sepidehmiller.alumniconnector.data;

public class Member {

  private String mId;
  private String mName;
  private int mYear;
  private Address mAddress;

  public Member() {

  }

  public Member(String name, int year, Address address) {
    this.mName = name;
    this.mYear = year;
    this. mAddress = address;
  }

  public String getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public int getYear() {
    return mYear;
  }

  public Address getAddress() {
    return mAddress;
  }

  public void setId(String id) {
    mId = id;
  }

  public void setName(String name) {
    mName = name;
  }

  public void setYear(int year) {
    mYear = year;
  }

  public void setAddress(Address address) {
    mAddress = address;
  }
}
