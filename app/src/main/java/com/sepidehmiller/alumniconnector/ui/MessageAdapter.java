package com.sepidehmiller.alumniconnector.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sepidehmiller.alumniconnector.R;
import com.sepidehmiller.alumniconnector.data.ChatMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//The adapter below relied heavily on the Friendly Chat tutorial
//https://github.com/udacity/and-nd-firebase/blob/1.03-firebase-database-read/app/src/main/java/com/google/firebase/udacity/friendlychat/MainActivity.java


public class MessageAdapter extends ArrayAdapter<ChatMessage> {
  public MessageAdapter(Context context, int resource, List<ChatMessage> object) {
    super(context, resource, object);
  }

  @BindView(R.id.messageText)
  TextView messageText;

  @BindView(R.id.messageUser)
  TextView messageUser;

  @BindView(R.id.messageTime)
  TextView messageTime;

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    if (convertView == null) {
      convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
    }

    ButterKnife.bind(this, convertView);

    ChatMessage chatMessage = getItem(position);
    messageText.setText(chatMessage.getMessage());
    messageUser.setText(chatMessage.getUser());
    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
        chatMessage.getTime()));

    return convertView;
  }
}
