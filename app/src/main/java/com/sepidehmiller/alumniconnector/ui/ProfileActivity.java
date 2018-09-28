package com.sepidehmiller.alumniconnector.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import com.sepidehmiller.alumniconnector.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    ButterKnife.bind(this);

    setupUI();
  }

  private void setupUI() {
    mClassSpinner.setSelection(10);
    mStateSpinner.setSelection(23);
    mCountrySpinner.setSelection(222);
  }

}
