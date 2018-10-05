package com.sepidehmiller.alumniconnector.network;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.sepidehmiller.alumniconnector.data.Address;
import com.sepidehmiller.alumniconnector.network.data.GeoLocationApiResult;
import com.sepidehmiller.alumniconnector.ui.ProfileActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class GeoCodingService extends JobIntentService {
  private static final String TAG = "GeoCodingService";
  private GeoLocationApiResult mGeoLocationApiResult;

  @Override
  protected void onHandleWork(@NonNull Intent intent) {

    if (intent.hasExtra(ProfileActivity.ADDRESS)) {
      Address address = intent.getParcelableExtra(ProfileActivity.ADDRESS);
      Call<GeoLocationApiResult> call= NetworkUtils.buildGeocodingCall(address);

      try {
        Response<GeoLocationApiResult> response = call.execute();

        //TODO - get the locations from the results and do something with that info.

        if (response.message().contentEquals("OK")) {
          mGeoLocationApiResult = response.body();
        }

      } catch (IOException io) {
        Log.e(TAG, "Could not call Geocoding API");
      }
    }



    if (intent.hasExtra(ProfileActivity.FIREBASE_UID)) {
      String firebaseUID = intent.getStringExtra(ProfileActivity.FIREBASE_UID);
    }
  }
}
