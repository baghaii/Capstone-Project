package com.sepidehmiller.alumniconnector.data;


// This class is based on section 5 of
// https://code.tutsplus.com/tutorials/how-to-create-an-android-chat-app-using-firebase--cms-27397

import java.util.Date;

public class ChatMessage {
  private String message;
  private String user;
  private long time;
  private String id;

  public ChatMessage() {

  }

  public ChatMessage(String message, String user) {
    this.message = message;
    this.user = user;
    this.time = new Date().getTime();
  }

  public ChatMessage(String message, String user, long time) {
    this.message = message;
    this.user = user;
    this.time = time;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getId() { return id; }

  public void setId(String id) { this.id = id; }

}
