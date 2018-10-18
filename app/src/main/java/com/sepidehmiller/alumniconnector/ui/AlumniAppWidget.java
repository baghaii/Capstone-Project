package com.sepidehmiller.alumniconnector.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.JobIntentService;

import com.sepidehmiller.alumniconnector.network.AppWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class AlumniAppWidget extends AppWidgetProvider {
  private static final int JOB_ID = 2001;

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    ComponentName thisWidget = new ComponentName(context, AlumniAppWidget.class);
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    Intent intent = new Intent(context.getApplicationContext(), AppWidgetService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
    JobIntentService.enqueueWork(context, AppWidgetService.class, JOB_ID, intent);
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

