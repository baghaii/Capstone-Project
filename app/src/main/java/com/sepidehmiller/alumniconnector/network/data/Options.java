package com.sepidehmiller.alumniconnector.network.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Options {
  @SerializedName("maxResults")
  @Expose
  private Integer maxResults;
  @SerializedName("thumbMaps")
  @Expose
  private Boolean thumbMaps;
  @SerializedName("ignoreLatLngInput")
  @Expose
  private Boolean ignoreLatLngInput;

  public Integer getMaxResults() {
    return maxResults;
  }

  public void setMaxResults(Integer maxResults) {
    this.maxResults = maxResults;
  }

  public Boolean getThumbMaps() {
    return thumbMaps;
  }

  public void setThumbMaps(Boolean thumbMaps) {
    this.thumbMaps = thumbMaps;
  }

  public Boolean getIgnoreLatLngInput() {
    return ignoreLatLngInput;
  }

  public void setIgnoreLatLngInput(Boolean ignoreLatLngInput) {
    this.ignoreLatLngInput = ignoreLatLngInput;
  }
}
