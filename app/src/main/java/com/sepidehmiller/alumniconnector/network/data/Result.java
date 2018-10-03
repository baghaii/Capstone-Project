package com.sepidehmiller.alumniconnector.network.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
  @Expose
  private ProvidedLocation providedLocation;
  @SerializedName("locations")
  @Expose
  private List<Location> locations = null;

  public ProvidedLocation getProvidedLocation() {
    return providedLocation;
  }

  public void setProvidedLocation(ProvidedLocation providedLocation) {
    this.providedLocation = providedLocation;
  }

  public List<Location> getLocations() {
    return locations;
  }

  public void setLocations(List<Location> locations) {
    this.locations = locations;
  }

}
