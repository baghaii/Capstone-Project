package com.sepidehmiller.alumniconnector.ui.alumni;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sepidehmiller.alumniconnector.R;

public class AlumniProfileActivity extends AppCompatActivity {
  public static final String KEY_USER_ID = "UserId";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alumni_profile);
  }
}
