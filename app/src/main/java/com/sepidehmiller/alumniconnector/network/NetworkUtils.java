package com.sepidehmiller.alumniconnector.network;

import com.sepidehmiller.alumniconnector.BuildConfig;
import com.sepidehmiller.alumniconnector.network.data.GeoLocationApiResult;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://developer.mapquest.com/documentation/geocoding-api/address/get/
public class NetworkUtils {
  private static final String GEOLOCATION_URL = "http://www.mapquestapi.com/geocoding/v1/address";

  private static Retrofit sRetrofit = new Retrofit.Builder()
      .baseUrl(GEOLOCATION_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build();

  private static GeoLocationService sGeoLocationService = sRetrofit.create(GeoLocationService.class);

  public static Call<GeoLocationApiResult> buildGeocodingCall(String street, String city, String state, String postalCode) {
    Call<GeoLocationApiResult> call = sGeoLocationService.getLocation(BuildConfig.MAPQUEST_KEY,
        street, city, state, postalCode);
    return call;
  }
}
