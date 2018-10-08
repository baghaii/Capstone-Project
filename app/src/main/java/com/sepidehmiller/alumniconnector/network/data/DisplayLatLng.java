package com.sepidehmiller.alumniconnector.network.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Class created with http://www.jsonschema2pojo.org/ using Mapquest API results

public class DisplayLatLng {

  @SerializedName("lat")
  @Expose
  private Double lat;
  @SerializedName("lng")
  @Expose
  private Double lng;

  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Double getLng() {
    return lng;
  }

  public void setLng(Double lng) {
    this.lng = lng;
  }
}