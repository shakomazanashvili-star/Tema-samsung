package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.MainActivity

object WidgetPinHelper {

    fun isPinningSupported(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appWidgetManager = context.getSystemService(AppWidgetManager::class.java)
            appWidgetManager?.isRequestPinAppWidgetSupported == true
        } else {
            false
        }
    }

    fun pinClockWidget(context: Context): Boolean {
        return pinWidget(context, S24ClockWidgetProvider::class.java)
    }

    fun pinBatteryWidget(context: Context): Boolean {
        return pinWidget(context, S24BatteryWidgetProvider::class.java)
    }

    fun pinGlanceWidget(context: Context): Boolean {
        return pinWidget(context, S24GlanceWidgetProvider::class.java)
    }

    private fun pinWidget(context: Context, providerClass: Class<*>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appWidgetManager = context.getSystemService(AppWidgetManager::class.java) ?: return false
            if (!appWidgetManager.isRequestPinAppWidgetSupported) return false

            val myProvider = ComponentName(context, providerClass)
            val successCallback = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            return appWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
        }
        return false
    }
}
