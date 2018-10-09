package com.sepidehmiller.alumniconnector.network;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sepidehmiller.alumniconnector.data.Address;
import com.sepidehmiller.alumniconnector.data.Member;
import com.sepidehmiller.alumniconnector.network.data.GeoLocationApiResult;
import com.sepidehmiller.alumniconnector.network.data.Location;
import com.sepidehmiller.alumniconnector.ui.ProfileActivity;

import java.io.IOException;
import java.util.List;

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


        if (response.message().contentEquals("OK")) {
          mGeoLocationApiResult = response.body();
        }

      } catch (IOException io) {
        Log.e(TAG, "Could not call Geocoding API");
      }

      if (mGeoLocationApiResult != null) {

        List<Location> locationList = mGeoLocationApiResult.getResults().get(0).getLocations();

        String firebaseUID="";
        if (intent.hasExtra(ProfileActivity.FIREBASE_UID)) {
          firebaseUID = intent.getStringExtra(ProfileActivity.FIREBASE_UID);
        }

        if (locationList.size() == 1) {
          storeInFirebase(firebaseUID, locationList.get(0));
        }
      }
    }
  }

  private void storeInFirebase(final String firebaseUID, final Location location) {
    if (!firebaseUID.isEmpty()) {
      FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
      final DatabaseReference dbReference = firebaseDb.getReference("alumni");

      dbReference.child(firebaseUID).addListenerForSingleValueEvent(
          new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()) {
                Member member = dataSnapshot.getValue(Member.class);
                member.setLatitude(location.getLatLng().getLat());
                member.setLongitude(location.getLatLng().getLng());
                dbReference.child(firebaseUID).setValue(member);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
          }
      );
    }
  }
}
