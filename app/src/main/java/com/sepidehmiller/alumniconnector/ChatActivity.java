package com.sepidehmiller.alumniconnector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;


//TODO - completed step 4 of
//https://code.tutsplus.com/tutorials/how-to-create-an-android-chat-app-using-firebase--cms-27397

public class ChatActivity extends AppCompatActivity {
  public static final int SIGN_IN_REQUEST_CODE = 101;

  @BindView(R.id.floatingActionButton)
  FloatingActionButton mFab;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.chat_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_sign_out) {
      AuthUI.getInstance().signOut(this)
          .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              Toast.makeText(ChatActivity.this,
                  "You have been signed out", Toast.LENGTH_LONG).show();
              finish();
            }
          });
    }
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);


    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
      startActivityForResult(
          AuthUI.getInstance()
              .createSignInIntentBuilder()
              .setLogo(R.drawable.plug)
              .build(),
             SIGN_IN_REQUEST_CODE);
    } else {
      String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

      if (name == null) {
        name = "User";
      }

      Toast.makeText(this,
          "Welcome " + name,
          Toast.LENGTH_LONG).show();
          displayChatMessages();
    }

    ButterKnife.bind(this);
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

  @OnTextChanged(R.id.inputEditText)
  public void onTextChanged(CharSequence text) {
    if (text.toString().trim().length() > 0 ) {
      mFab.setEnabled(true);
    } else {
      mFab.setEnabled(false);
    }
  }


}
