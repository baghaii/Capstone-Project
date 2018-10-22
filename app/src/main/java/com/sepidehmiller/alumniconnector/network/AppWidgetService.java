package com.sepidehmiller.alumniconnector.network;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sepidehmiller.alumniconnector.R;
import com.sepidehmiller.alumniconnector.data.ChatMessage;
import com.sepidehmiller.alumniconnector.data.Member;
import com.sepidehmiller.alumniconnector.ui.SelectorActivity;

//http://www.vogella.com/tutorials/AndroidWidgets/article.html#exercise-update-widget-via-a-service

public class AppWidgetService extends JobIntentService {

  private static final String TAG = "AppWidgetService";
  private AppWidgetManager mAppWidgetManager;

  @Override
  public void onHandleWork(Intent intent) {
    Context context = this.getApplicationContext();
    mAppWidgetManager = AppWidgetManager.getInstance(context);
    int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);


    for (final int widgetId : allWidgetIds) {
      final RemoteViews remoteViews = new RemoteViews(this.getApplication().getPackageName(),
          R.layout.alumni_app_widget);

      final DatabaseReference databaseReference = FirebaseHelper.getMessagesTable();
      Query query = databaseReference.orderByChild("time").limitToLast(1);

      query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          /* It was not obvious that I needed a for loop for a query that returns a single value,
             but okay. */

          //https://stackoverflow.com/questions/44811526/datasnapshot-has-the-object-but-getvalue-will-return-null
          for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            ChatMessage chatMessage = childSnapshot.getValue(ChatMessage.class);
            final long chatTime = chatMessage.getTime();
            DatabaseReference alumniReference = FirebaseHelper.getAlumniTable();
            String uid = FirebaseAuth.getInstance().getUid();
            if (uid != null) {
              alumniReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  Member member = dataSnapshot.getValue(Member.class);
                  if (member != null) {
                    long time = member.getLastSeen();
                    setRemoteViews(remoteViews, time, chatTime);
                    mAppWidgetManager.updateAppWidget(widgetId, remoteViews);
                  }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
              });
            }
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });


    }
  }

  private void setRemoteViews(RemoteViews remoteViews, long lastOpenedTime, long lastMessageTime) {

    final String[] messages = this.getApplication().getResources().getStringArray(R.array.appwidget_text);

    Log.d(TAG, "lastOpenedTime: " + lastOpenedTime + " lastMessageTime: " + lastMessageTime);


    if (lastOpenedTime < lastMessageTime) {
      remoteViews.setTextViewText(R.id.appwidget_text, messages[1]);
      remoteViews.setContentDescription(R.id.appwidget_text, messages[1]);
    } else {
      remoteViews.setTextViewText(R.id.appwidget_text, messages[0]);
      remoteViews.setContentDescription(R.id.appwidget_text, messages[0]);
    }


    //Create onClickListener for widget
    Intent newIntent = new Intent(this.getApplicationContext(), SelectorActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    remoteViews.setOnClickPendingIntent(R.id.appwidget, pendingIntent);
  }
}
