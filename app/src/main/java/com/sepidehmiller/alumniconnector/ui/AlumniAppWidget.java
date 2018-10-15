package com.sepidehmiller.alumniconnector.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.sepidehmiller.alumniconnector.R;

/**
 * Implementation of App Widget functionality.
 */
public class AlumniAppWidget extends AppWidgetProvider {

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                              int appWidgetId) {
    final String[] messages = context.getResources().getStringArray(R.array.appwidget_text);

    CharSequence widgetText = messages[0];

    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.alumni_app_widget);
    views.setTextViewText(R.id.appwidget_text, widgetText);

    SharedPreferences sharedPreferences = context.getSharedPreferences(
        context.getResources().getString(R.string.widget_time), Context.MODE_PRIVATE);


    long lastOpened = sharedPreferences.getLong(
        context.getResources().getString(R.string.newest_message), 0);

    long lastNewMessage = 0;

    if (lastOpened <= lastNewMessage) {
      views.setTextViewText(R.id.appwidget_text, messages[1]);
      views.setContentDescription(R.id.appwidget_text, messages[1]);
    } else {
      views.setTextViewText(R.id.appwidget_text, messages[0]);
      views.setContentDescription(R.id.appwidget_text, messages[0]);
    }

    Intent intent = new Intent (context, SelectorActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    views.setOnClickPendingIntent(R.id.appwidget, pendingIntent);
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}

