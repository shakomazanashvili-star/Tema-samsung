package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R

class S24BatteryWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val batteryInfo = BatteryTelemetryUtil.getBatteryInfo(context)
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, batteryInfo)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_BATTERY_CHANGED || intent.action == Intent.ACTION_POWER_CONNECTED || intent.action == Intent.ACTION_POWER_DISCONNECTED) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, S24BatteryWidgetProvider::class.java))
            if (ids.isNotEmpty()) {
                val batteryInfo = BatteryTelemetryUtil.getBatteryInfo(context)
                for (id in ids) {
                    updateAppWidget(context, appWidgetManager, id, batteryInfo)
                }
            }
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            batteryInfo: DeviceBatteryTelemetry
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_s24_battery)

            // Update Phone battery
            views.setTextViewText(R.id.widget_phone_percentage, "${batteryInfo.percentage}%")
            views.setProgressBar(R.id.widget_phone_progress, 100, batteryInfo.percentage, false)

            if (batteryInfo.isCharging) {
                views.setViewVisibility(R.id.widget_battery_bolt, View.VISIBLE)
                views.setTextViewText(R.id.widget_battery_status_badge, "Fast Charging")
            } else {
                views.setViewVisibility(R.id.widget_battery_bolt, View.GONE)
                views.setTextViewText(R.id.widget_battery_status_badge, "Optimal • One UI")
            }

            // Click to open App
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_battery_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
