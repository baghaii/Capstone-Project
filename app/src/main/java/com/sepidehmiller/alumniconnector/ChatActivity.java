package com.sepidehmiller.alumniconnector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sepidehmiller.alumniconnector.data.ChatMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;


//https://code.tutsplus.com/tutorials/how-to-create-an-android-chat-app-using-firebase--cms-27397


public class ChatActivity extends AppCompatActivity {
  public static final int SIGN_IN_REQUEST_CODE = 101;

  private static final String ANONYMOUS = "User";

  @BindView(R.id.floatingActionButton)
  FloatingActionButton mFab;

  @BindView(R.id.chatEditText)
  EditText mMessageText;

  @BindView(R.id.chatListView)
  ListView mChatListView;

  private FirebaseDatabase mFirebaseDatabase;
  private DatabaseReference mDatabaseReference;
  private ChildEventListener mChildEventListener;

  private FirebaseAuth mFirebaseAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;

  private MessageAdapter mMessageAdapter;

  private String mUserName;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.chat_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_sign_out) {
      AuthUI.getInstance().signOut(this);
    }
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    mFirebaseAuth = FirebaseAuth.getInstance();

    mFirebaseDatabase = FirebaseDatabase.getInstance();
    mDatabaseReference = mFirebaseDatabase.getReference().child("messages");

    ButterKnife.bind(this);

    List<ChatMessage> chatMessages = new ArrayList<>();
    mMessageAdapter = new MessageAdapter(this, R.layout.item_message, chatMessages);
    mChatListView.setAdapter(mMessageAdapter);


    mFab.setBackgroundTintList(getResources().getColorStateList(R.color.button_color_list));
    mFab.setEnabled(false);


    mAuthStateListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
          //signed in
          onSignedInInitialize(mFirebaseAuth.getCurrentUser().getDisplayName());
        } else {
          //signed out
          onSignedOutCleanUp();
          List<AuthUI.IdpConfig> providers = Arrays.asList(
              new AuthUI.IdpConfig.EmailBuilder().build(),
              new AuthUI.IdpConfig.GoogleBuilder().build());

          startActivityForResult(
              AuthUI.getInstance()
                  .createSignInIntentBuilder()
                  .setAvailableProviders(providers)
                  .setLogo(R.drawable.ic_plug)
                  .build(),
              SIGN_IN_REQUEST_CODE);
        }
      }
    };

  }

  private void onSignedInInitialize(String username) {
    mUserName = ANONYMOUS;
    attachDatabaseReadListener();
  }

  private void onSignedOutCleanUp() {
    mUserName = ANONYMOUS;
    detachDatabaseReadListener();
    mMessageAdapter.clear();

  }

  public void attachDatabaseReadListener() {
    //The child event listener below relied heavily on the Friendly Chat tutorial
    //https://github.com/udacity/and-nd-firebase/blob/1.03-firebase-database-read/app/src/main/java/com/google/firebase/udacity/friendlychat/MainActivity.java

    if (mChildEventListener == null) {
      mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
          ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
          mMessageAdapter.add(chatMessage);
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

  public void detachDatabaseReadListener() {
    if (mChildEventListener != null) {
      mDatabaseReference.removeEventListener(mChildEventListener);
      mChildEventListener = null;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == SIGN_IN_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        Toast.makeText(this, "Successfully signed in. Welcome!", Toast.LENGTH_LONG)
            .show();
        mUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
      } else {
        Toast.makeText(this, "We couldn't sign you in. Please try later.", Toast.LENGTH_LONG)
            .show();
        finish();
      }
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    detachDatabaseReadListener();
    mMessageAdapter.clear();

  }

  @Override
  protected void onResume() {
    super.onResume();
    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
  }

  @OnTextChanged(R.id.chatEditText)
  public void onTextChanged(CharSequence text) {
    if (text.toString().trim().length() > 0) {
      mFab.setEnabled(true);
    } else {
      mFab.setEnabled(false);
    }
  }

  @OnClick(R.id.floatingActionButton)
  public void onClick() {
    String message = mMessageText.getText().toString();

    mDatabaseReference
        .push()
        .setValue(new ChatMessage(message, mUserName));

    mMessageText.setText("");
  }

}
