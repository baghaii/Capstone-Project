package com.sepidehmiller.alumniconnector.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sepidehmiller.alumniconnector.R;
import com.sepidehmiller.alumniconnector.data.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;


//https://code.tutsplus.com/tutorials/how-to-create-an-android-chat-app-using-firebase--cms-27397


public class ChatActivity extends AppCompatActivity {
  private final String ANONYMOUS = "Anonymous";

  @BindView(R.id.floatingActionButton)
  FloatingActionButton mFab;

  @BindView(R.id.chatEditText)
  EditText mMessageText;

  @BindView(R.id.chatListView)
  ListView mChatListView;

  private FirebaseAuth mFirebaseAuth;
  private FirebaseDatabase mFirebaseDatabase;
  private DatabaseReference mDatabaseReference;
  private ChildEventListener mChildEventListener;

  private MessageAdapter mMessageAdapter;

  private String mUserName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirebaseDatabase = FirebaseDatabase.getInstance();
    mDatabaseReference = mFirebaseDatabase.getReference().child("messages");

    mUserName = mFirebaseAuth.getCurrentUser().getDisplayName();


    if (mUserName == null || mUserName.isEmpty()) {
      mUserName = ANONYMOUS;
    }

    ButterKnife.bind(this);

    List<ChatMessage> chatMessages = new ArrayList<>();
    mMessageAdapter = new MessageAdapter(this, R.layout.item_message, chatMessages);
    mChatListView.setAdapter(mMessageAdapter);

    mFab.setBackgroundTintList(getResources().getColorStateList(R.color.button_color_list));
    mFab.setEnabled(false);

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
  protected void onPause() {
    super.onPause();
    detachDatabaseReadListener();
    mMessageAdapter.clear();

    //Store time in SharedPreferences for widget.
    SharedPreferences sharedPreferences = getSharedPreferences(
        getResources().getString(R.string.widget_time), Context.MODE_PRIVATE);

    SharedPreferences.Editor editor = sharedPreferences.edit();
    long now = System.currentTimeMillis();
    editor.putLong(getResources().getString(R.string.newest_message), now);
    editor.apply();

  }

  @Override
  protected void onResume() {
    super.onResume();
    attachDatabaseReadListener();
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
