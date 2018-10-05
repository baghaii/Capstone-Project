package com.sepidehmiller.alumniconnector.network;

import com.sepidehmiller.alumniconnector.network.data.GeoLocationApiResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoLocationService {

  /* TODO This works for addresses in the US. We will eventually need to expand for the other addresses. */

  @GET("address")
  Call<GeoLocationApiResult> getLocation (
    @Query("key") String apiKey,
    @Query("street") String street,
    @Query("city") String city,
    @Query("state") String state,
    @Query("postalCode") String postalCode
  );
}
