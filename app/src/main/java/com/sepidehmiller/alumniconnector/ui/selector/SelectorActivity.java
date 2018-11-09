package com.sepidehmiller.alumniconnector.ui.selector;

import android.app.ActivityOptions;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.JobIntentService;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.sepidehmiller.alumniconnector.R;
import com.sepidehmiller.alumniconnector.network.AppWidgetService;
import com.sepidehmiller.alumniconnector.ui.chat.ChatActivity;
import com.sepidehmiller.alumniconnector.ui.map.MapsActivity;
import com.sepidehmiller.alumniconnector.ui.profile.ProfileActivity;
import com.sepidehmiller.alumniconnector.ui.widget.AlumniAppWidget;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//TODO create an indetermine progress frame to load before log in.

public class SelectorActivity extends AppCompatActivity {
  public static final String NAME_EXTRA = "Name";
  public static final String ANONYMOUS = "Anonymous";

  private static final int SIGN_IN_REQUEST_CODE = 101;
  private FirebaseAuth mFirebaseAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;

  private String mUserName;

  @BindView(R.id.selectorLayout)
  ConstraintLayout selectorLayout;

  @BindView(R.id.chatButton)
  ImageButton chatButton;

  @BindView(R.id.mapButton)
  ImageButton mapButton;

  @OnClick(R.id.chatButton)
  public void onClickChat() {
    Intent intent = new Intent(SelectorActivity.this, ChatActivity.class);

    //http://www.vogella.com/tutorials/AndroidAnimation/article.html#exercise-using-the-properties-animations-api
    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(chatButton, 0,
        0, chatButton.getWidth(), chatButton.getHeight());
    startActivity(intent, options.toBundle());
  }

 @OnClick(R.id.mapButton)
  public void onClickMap() {
    Intent intent = new Intent(SelectorActivity.this, MapsActivity.class);
    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(mapButton, 0,
       0, mapButton.getWidth(), mapButton.getHeight());
    startActivity(intent, options.toBundle());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

      Window window = getWindow();
      Explode explode = new Explode();
      explode.setDuration(3000);
      window.setExitTransition(explode);
      window.setReenterTransition(explode);
    }

    setContentView(R.layout.activity_selector);

    ButterKnife.bind(this);

    mFirebaseAuth = FirebaseAuth.getInstance();

    mAuthStateListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
          //signed in
          mUserName = mFirebaseAuth.getCurrentUser().getDisplayName();
          selectorLayout.setVisibility(View.VISIBLE);
          updateWidget();


        } else {
          //signed out
          mUserName = ANONYMOUS;
          selectorLayout.setVisibility(View.INVISIBLE);
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.selector_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {

      case R.id.menu_profile:
        Intent intent = makeProfileIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          startActivity(intent,
              ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
          startActivity(intent);
        }
        return true;

      case R.id.menu_sign_out:
        AuthUI.getInstance().signOut(this);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == SIGN_IN_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        mUserName = getFirebaseUser();
        selectorLayout.setVisibility(View.VISIBLE);
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
  }

  @Override
  protected void onResume() {
    super.onResume();
    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
  }

  private String getFirebaseUser() {
    return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
  }

  private Intent makeProfileIntent() {
    Intent intent = new Intent(SelectorActivity.this, ProfileActivity.class);
    intent.putExtra(NAME_EXTRA, mUserName);
    return intent;
  }

  private void updateWidget() {
    Context context = SelectorActivity.this;
    ComponentName thisWidget = new ComponentName(context, AlumniAppWidget.class);
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(SelectorActivity.this);
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    Intent intent = new Intent(context.getApplicationContext(), AppWidgetService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
    JobIntentService.enqueueWork(context, AppWidgetService.class, AlumniAppWidget.JOB_ID, intent);
  }
}
