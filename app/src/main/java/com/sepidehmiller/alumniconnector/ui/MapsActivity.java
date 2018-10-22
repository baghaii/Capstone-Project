package com.sepidehmiller.alumniconnector.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sepidehmiller.alumniconnector.R;
import com.sepidehmiller.alumniconnector.data.Member;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

  private GoogleMap mMap;
  private FirebaseAuth mFirebaseAuth;
  private FirebaseDatabase mFirebaseDatabase;
  private DatabaseReference mDatabaseReference;
  private ChildEventListener mChildEventListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirebaseDatabase = FirebaseDatabase.getInstance();
    mDatabaseReference = mFirebaseDatabase.getReference().child("alumni");

    //To make this scalable, we will eventually need to look into clustering.
    //TODO https://developers.google.com/maps/documentation/android-sdk/utility/marker-clustering

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }


  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {

    mMap = googleMap;
    mMap.setMinZoomPreference(4.0f);
    mMap.setMaxZoomPreference(14.0f);
    mMap.getUiSettings().setZoomControlsEnabled(true);
    mMap.getUiSettings().setZoomGesturesEnabled(true);
    // The default camera position is the lat/lon value of the town where the
    // school is located.
    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(33.507083, -88.49678)));

  }

  public void attachChildEventListener() {
    if (mChildEventListener == null) {
      mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
          Member member = dataSnapshot.getValue(Member.class);
          if (member == null) {
            return;
          }

          String uid = dataSnapshot.getKey();

          if (member.getLatitude() != 1000 && member.getLongitude() != 1000) {
            LatLng point = new LatLng(member.getLatitude(), member.getLongitude());
            MarkerOptions marker = new MarkerOptions().position(point).title(member.getName());

            //https://stackoverflow.com/questions/16441994/change-marker-color-in-google-maps-v2

            if (uid != null && mFirebaseAuth.getUid() != null) {
              if (uid.contentEquals(mFirebaseAuth.getUid())) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                marker.icon(bitmapDescriptor);
              }
            }

            mMap.addMarker(marker);
          }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      };
      mDatabaseReference.addChildEventListener(mChildEventListener);
    }
  }

  public void detachChildEventListener() {
    if (mChildEventListener != null) {
      mDatabaseReference.removeEventListener(mChildEventListener);
      mChildEventListener = null;
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    attachChildEventListener();
  }

  @Override
  protected void onPause() {
    super.onPause();
    detachChildEventListener();
  }
}
