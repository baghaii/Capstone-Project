package com.sepidehmiller.alumniconnector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;


//TODO - completed step 1 of
//https://code.tutsplus.com/tutorials/how-to-create-an-android-chat-app-using-firebase--cms-27397

public class ChatActivity extends AppCompatActivity {
  public static final int SIGN_IN_REQUEST_CODE = 101;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);
    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
      startActivityForResult(
          AuthUI.getInstance()
              .createSignInIntentBuilder()
              .build(),
             SIGN_IN_REQUEST_CODE);
    } else {
      Toast.makeText(this,
          "Welcome " + FirebaseAuth.getInstance()
          .getCurrentUser()
          .getDisplayName(),
          Toast.LENGTH_LONG).show();
          displayChatMessages();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == SIGN_IN_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        Toast.makeText(this, "Successfully signed in. Welcome!", Toast.LENGTH_LONG)
            .show();
      } else {
        Toast.makeText(this, "We couldn't sign you in. Please try later.", Toast.LENGTH_LONG)
            .show();
        finish();
      }
    }
  }

  private void displayChatMessages() {}

}
