package com.sepidehmiller.alumniconnector.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sepidehmiller.alumniconnector.R;
import com.sepidehmiller.alumniconnector.data.Address;
import com.sepidehmiller.alumniconnector.data.Member;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class ProfileActivity extends AppCompatActivity {

  public static final int US_SPINNER_POSITION = 222;

  private String mName;
  private FirebaseAuth mFirebaseAuth;
  private FirebaseDatabase mFirebaseDatabase;
  private DatabaseReference mDatabaseReference;

  private String[] mClasses;
  private String[] mStates;
  private String[] mCountries;

  @BindView(R.id.classSpinner)
  Spinner mClassSpinner;

  @BindView(R.id.nameEditText)
  EditText mNameEditText;

  @BindView(R.id.address1EditText)
  EditText mAddress1;

  @BindView(R.id.address2EditText)
  EditText mAddress2;

  @BindView(R.id.address3EditText)
  EditText mAddress3;

  @BindView(R.id.cityEditText)
  EditText mCityEditText;

  @BindView(R.id.stateSpinner)
  Spinner mStateSpinner;

  @BindView(R.id.zipcodeEditText)
  EditText mZipCodeEditText;

  @BindView(R.id.countrySpinner)
  Spinner mCountrySpinner;

  @OnItemSelected(R.id.countrySpinner)
  public void onItemSelected(Spinner spinner, int position) {
    if (position != US_SPINNER_POSITION) {
      mStateSpinner.setEnabled(false);
    } else {
      mStateSpinner.setEnabled(true);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    ButterKnife.bind(this);

    if (getIntent().hasExtra(SelectorActivity.NAME_EXTRA)) {
      mName = getIntent().getStringExtra(SelectorActivity.NAME_EXTRA);
    } else {
      mName = SelectorActivity.ANONYMOUS;
    }

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirebaseDatabase = FirebaseDatabase.getInstance();
    mDatabaseReference = mFirebaseDatabase.getReference().child("alumni");

    setupUI();
    loadData();
    mClasses = getResources().getStringArray(R.array.class_array);
    mStates = getResources().getStringArray(R.array.states_array);
    mCountries = getResources().getStringArray(R.array.countries_array);

  }

  private void setupUI() {
    mClassSpinner.setSelection(10);
    mStateSpinner.setSelection(23);
    mCountrySpinner.setSelection(US_SPINNER_POSITION);
    mNameEditText.setText(mName);
  }

  private void loadData() {
    mDatabaseReference.child(mFirebaseAuth.getUid()).addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Member member = dataSnapshot.getValue(Member.class);

            Address address = member.getAddress();
            String[] addressLine = address.getAddress().split("\n");
            int length = addressLine.length;

            mAddress1.setText(addressLine[0]);

            if (length > 1) {
              mAddress2.setText(addressLine[1]);
            }
            if (length > 2) {
              mAddress3.setText(addressLine[2]);
            }

            mCityEditText.setText(address.getCity());
            mZipCodeEditText.setText(address.getZipcode());

            int year = member.getYear();
            String state = address.getState();
            String country = address.getCountry();

            for (int i = 0; i < mClasses.length; i++) {
              if (year == Integer.valueOf(mClasses[i])) {
                mClassSpinner.setSelection(i);
                break;
              }
            }

            if (country.contentEquals(mCountries[US_SPINNER_POSITION])) {
              mCountrySpinner.setSelection(US_SPINNER_POSITION);
              for (int i = 0; i < mStates.length; i++) {
                if (state.contentEquals(mStates[i])) {
                  mStateSpinner.setSelection(i);
                  break;
                }
              }

            } else {

              mStateSpinner.setEnabled(false);
              for (int i = 0; i < mCountries.length; i++) {
                if (country.contentEquals(mCountries[i])) {
                  mCountrySpinner.setSelection(i);
                  break;
                }
              }
            }

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
        }

    );
  }

  @Override
  protected void onPause() {
    super.onPause();

    if (!mAddress1.getText().toString().isEmpty()) {
      String id = mFirebaseAuth.getUid();
      int classPos = mClassSpinner.getSelectedItemPosition();

      int year = Integer.valueOf(mClasses[classPos]);

      Address address = createAddress();

      mDatabaseReference.child(id)
          .setValue(new Member(mName, year, address));
    }
  }

  public Address createAddress() {
    String address1 = mAddress1.getText().toString().trim();
    String address2 = mAddress2.getText().toString().trim();
    String address3 = mAddress3.getText().toString().trim();

    String streetAddress = "";
    if (address1.length() > 0) {
      streetAddress = address1;
    }

    if (address2.length() > 0) {
      streetAddress = streetAddress + "\n" + address2;
    }

    if (address3.length() > 0) {
      streetAddress = streetAddress + "\n" + address3;
    }

    String city = mCityEditText.getText().toString().trim();

    int posCountry = mCountrySpinner.getSelectedItemPosition();
    String country = mCountries[posCountry];

    String state = null;
    String zipcode = null;

    if (posCountry == US_SPINNER_POSITION) {
      int posState = mStateSpinner.getSelectedItemPosition();
      state = mStates[posState];
      zipcode = mZipCodeEditText.getText().toString().trim();
    }

    return new Address(streetAddress, city, state, zipcode, country);
  }
}
