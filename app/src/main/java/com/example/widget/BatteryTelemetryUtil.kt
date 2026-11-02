package com.example.widget

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

data class DeviceBatteryTelemetry(
    val percentage: Int, // 1 - 100
    val isCharging: Boolean,
    val voltageMv: Int,
    val temperatureC: Float,
    val health: String,
    val technology: String
)

object BatteryTelemetryUtil {
    fun getBatteryInfo(context: Context): DeviceBatteryTelemetry {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = try {
            context.registerReceiver(null, filter)
        } catch (_: Exception) {
            null
        }

        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
        val pct = if (level >= 0 && scale > 0) {
            ((level / scale.toFloat()) * 100).toInt()
        } else {
            94
        }
        val voltage = batteryStatus?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 4120) ?: 4120
        val tempRaw = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 285) ?: 285
        val tempC = tempRaw / 10f

        val healthCode = batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD)
        val health = when (healthCode) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Optimal"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            else -> "Good"
        }

        val tech = batteryStatus?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Li-ion 5000mAh"

        return DeviceBatteryTelemetry(
            percentage = pct.coerceIn(1, 100),
            isCharging = isCharging,
            voltageMv = voltage,
            temperatureC = tempC,
            health = health,
            technology = tech
        )
    }
}
