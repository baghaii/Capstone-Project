package com.sepidehmiller.alumniconnector.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    String streetAddress = mAddress1.getText().toString().trim() +
        mAddress2.getText().toString().trim() +
        mAddress3.getText().toString().trim();

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
