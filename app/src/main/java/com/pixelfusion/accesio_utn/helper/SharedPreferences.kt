package com.pixelfusion.accesio_utn.helper

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    var hasSeenStartScreen: Boolean
        get() = prefs.getBoolean("has_seen_start_screen", false)
        set(value) {
            prefs.edit().putBoolean("has_seen_start_screen", value).apply()
        }
}