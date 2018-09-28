package com.sepidehmiller.alumniconnector.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.sepidehmiller.alumniconnector.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectorActivity extends AppCompatActivity {
  private FirebaseAuth mFirebaseAuth;
  private String mUserName;

  @BindView(R.id.chatButton)
  ImageButton chatButton;

  @BindView(R.id.mapButton)
  ImageButton mapButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_selector);

    ButterKnife.bind(this);

    chatButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(SelectorActivity.this, ChatActivity.class);
        startActivity(intent);
      }
    });

    mFirebaseAuth = FirebaseAuth.getInstance();

    //TODO Log in on this screen
    mFirebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
          mUserName = firebaseAuth.getCurrentUser().getDisplayName();
        } else {

        }
      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.selector_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {

      case R.id.menu_profile:
        Intent intent = new Intent(SelectorActivity.this, ProfileActivity.class);
        startActivity(intent);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
