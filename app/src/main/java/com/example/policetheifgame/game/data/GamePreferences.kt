package com.example.policetheifgame.game.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages persistent state for onboarding, current level progress, and audio settings.
 */
class GamePreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    var hasSeenOnboarding: Boolean
        get() = prefs.getBoolean(KEY_HAS_SEEN_ONBOARDING, false)
        set(value) = prefs.edit().putBoolean(KEY_HAS_SEEN_ONBOARDING, value).apply()

    var currentLevelIndex: Int
        get() = prefs.getInt(KEY_CURRENT_LEVEL_INDEX, 0)
        set(value) = prefs.edit().putInt(KEY_CURRENT_LEVEL_INDEX, value).apply()

    var unlockedLevelIndex: Int
        get() = prefs.getInt(KEY_UNLOCKED_LEVEL_INDEX, 0)
        set(value) = prefs.edit().putInt(KEY_UNLOCKED_LEVEL_INDEX, value).apply()

    var isSirenMuted: Boolean
        get() = prefs.getBoolean(KEY_IS_SIREN_MUTED, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_SIREN_MUTED, value).apply()

    companion object {
        private const val PREFS_NAME = "police_vs_thief_prefs"
        private const val KEY_HAS_SEEN_ONBOARDING = "has_seen_onboarding"
        private const val KEY_CURRENT_LEVEL_INDEX = "current_level_index"
        private const val KEY_UNLOCKED_LEVEL_INDEX = "unlocked_level_index"
        private const val KEY_IS_SIREN_MUTED = "is_siren_muted"
    }
}
