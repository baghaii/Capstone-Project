package com.sepidehmiller.alumniconnector.network;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
  private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
  private static FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

  public static DatabaseReference getAlumniTable() {
    return mFirebaseDatabase.getReference().child("alumni");
  }

  public static DatabaseReference getMessagesTable() {
    return mFirebaseDatabase.getReference().child("messages");
  }

  public static String getUid() {
    return mFirebaseAuth.getUid();
  }

  public static String getFirebaseAuthName() {
    String name = "";

    if (mFirebaseAuth != null) {
      name = mFirebaseAuth.getCurrentUser().getDisplayName();
    }

    return name;
  }

  public static boolean isAuth() {
    boolean auth;

    if (mFirebaseAuth != null) {
      auth = true;
    } else {
      auth = false;
    }
    return auth;
  }
}
