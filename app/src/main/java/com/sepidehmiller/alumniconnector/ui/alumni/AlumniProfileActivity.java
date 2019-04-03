package com.sepidehmiller.alumniconnector.ui.alumni;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sepidehmiller.alumniconnector.R;
import com.sepidehmiller.alumniconnector.data.Address;
import com.sepidehmiller.alumniconnector.data.Member;
import com.sepidehmiller.alumniconnector.network.FirebaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlumniProfileActivity extends AppCompatActivity {
  public static final String KEY_USER_ID = "UserId";

  @BindView(R.id.AlumniNameTextView)
  TextView mAlumniName;

  @BindView(R.id.AlumniClassTextView)
  TextView mAlumniClass;

  @BindView(R.id.AlumniAddressTextView)
  TextView mAlumniAddress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alumni_profile);

    ButterKnife.bind(this);

    Bundle bundle = getIntent().getExtras();

    if (bundle != null && bundle.containsKey(KEY_USER_ID)) {
      String key = bundle.getString(KEY_USER_ID);
      Toast.makeText(this, key, Toast.LENGTH_LONG).show();
      loadData(key);
    }

  }

  private void loadData(String key) {
    FirebaseHelper.getAlumniByUID(key).addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Member member = dataSnapshot.getValue(Member.class);
            if (member == null) {
              return;
            }

            String name = member.getName();
            if (name != null) {
              mAlumniName.setText(name);
            }

            int year = member.getYear();

            if (year > 0) {
              String strYear = String.format(getResources().getString(R.string.class_format),
                  String.valueOf(year));

              mAlumniClass.setText(strYear);
            }

            Address address = member.getAddress();

            if (address != null) {
              String strAddress = String.format(getResources().getString(R.string.address_format),
                  address.getStreet(),
                  address.getCity(),
                  address.getState(),
                  address.getZipcode(),
                  address.getCountry());
              mAlumniAddress.setText(strAddress);
            }

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
        }
    );
  }
}
