package com.sepidehmiller.alumniconnector.ui.chat;

import android.animation.ObjectAnimator;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.JobIntentService;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sepidehmiller.alumniconnector.R;
import com.sepidehmiller.alumniconnector.data.ChatMessage;
import com.sepidehmiller.alumniconnector.data.Member;
import com.sepidehmiller.alumniconnector.network.AppWidgetService;
import com.sepidehmiller.alumniconnector.network.FirebaseHelper;
import com.sepidehmiller.alumniconnector.ui.alumni.AlumniProfileActivity;
import com.sepidehmiller.alumniconnector.ui.widget.AlumniAppWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;


//https://code.tutsplus.com/tutorials/how-to-create-an-android-chat-app-using-firebase--cms-27397


public class ChatActivity extends AppCompatActivity {
  public static final String TAG = "ChatActivity";
  private static final String ANONYMOUS = "Anonymous";

  @BindView(R.id.floatingActionButton)
  FloatingActionButton mFab;

  @BindView(R.id.chatEditText)
  EditText mMessageText;

  @BindView(R.id.chatListView)
  ListView mChatListView;

  private DatabaseReference mDatabaseReference;
  private ChildEventListener mChildEventListener;

  private MessageAdapter mMessageAdapter;

  private String mUserName;
  private String mUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    mDatabaseReference = FirebaseHelper.getMessagesTable();

    mUserName = FirebaseHelper.getFirebaseAuthName();
    mUserId = FirebaseHelper.getUid();

    if (mUserName == null || mUserName.isEmpty()) {
      mUserName = ANONYMOUS;
    }

    ButterKnife.bind(this);

    List<ChatMessage> chatMessages = new ArrayList<>();
    mMessageAdapter = new MessageAdapter(this, R.layout.item_message, chatMessages);
    mChatListView.setAdapter(mMessageAdapter);
    mChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChatMessage chatMessage = mMessageAdapter.getItem(position);
        Toast.makeText(ChatActivity.this, chatMessage.getUserId(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ChatActivity.this, AlumniProfileActivity.class);
        intent.putExtra( AlumniProfileActivity.KEY_USER_ID, chatMessage.getUserId());
        startActivity(intent);
      }
    });

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
          updateWidget();

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

    final DatabaseReference memberReference = FirebaseHelper.getAlumniTable();

    memberReference.child(FirebaseHelper.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
          Member member = dataSnapshot.getValue(Member.class);
          member.setLastSeen(System.currentTimeMillis());
          memberReference.child(FirebaseHelper.getUid()).setValue(member);
        } else {
          Member member = new Member(FirebaseHelper.getFirebaseAuthName(),
              System.currentTimeMillis());
          memberReference.child(FirebaseHelper.getUid())
              .setValue(member);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

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
        .setValue(new ChatMessage(message, mUserName, mUserId));

    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    float width = size.x;

    ObjectAnimator fabAnimation = ObjectAnimator.ofFloat(mFab,
        "translationX", -width, 0f);
    fabAnimation.setDuration(300);
    fabAnimation.start();

    mMessageText.setText("");

  }

  private void updateWidget() {
    Context context = ChatActivity.this;
    ComponentName thisWidget = new ComponentName(context, AlumniAppWidget.class);
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ChatActivity.this);
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    Intent intent = new Intent(context.getApplicationContext(), AppWidgetService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
    JobIntentService.enqueueWork(context, AppWidgetService.class, AlumniAppWidget.JOB_ID, intent);
  }

}
