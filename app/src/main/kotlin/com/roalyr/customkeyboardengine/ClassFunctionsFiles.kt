package com.roalyr.customkeyboardengine

import android.content.Context
import android.content.res.Resources
import kotlinx.serialization.json.Json

object SettingsManager {
    private const val TAG = "SettingsManager"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
    }

    @Volatile
    private var cachedSettings: KeyboardSettings? = null

    /**
     * Loads keyboard settings from user file or defaults with caching.
     *
     * Attempts to load `settings.json` from the media directory. If the file doesn't exist
     * or parsing fails, falls back to built-in defaults from `R.raw.settings_default`.
     * Result is cached for subsequent calls.
     *
     * @param context Android context for resource access.
     * @param onError Callback invoked if parsing fails (receives error message).
     * @return [KeyboardSettings] instance with user or default values (never null).
     */
    fun loadSettings(
        context: Context,
        onError: (String) -> Unit
    ): KeyboardSettings {
        cachedSettings?.let { return it }
        val settings = loadDefaultSettings(context, onError)
        cachedSettings = settings
        return settings
    }

    /**
     * Loads built-in default settings from resources.
     *
     * Falls back to hard-coded [KeyboardSettings]() constructor if resource parsing fails,
     * ensuring settings are always available (never null).
     *
     * @param context Android context for resource access.
     * @param onError Callback invoked if resource parsing fails (receives error message).
     * @return [KeyboardSettings] instance with default values (never null).
     */
    private fun loadDefaultSettings(
        context: Context,
        onError: (String) -> Unit
    ): KeyboardSettings {
        return try {
            val text = context.resources.openRawResource(R.raw.settings_default)
                .bufferedReader()
                .use { it.readText() }
            json.decodeFromString<KeyboardSettings>(text)
        } catch (e: Exception) {
            onError(
                "Failed to parse built-in default settings resource (R.raw.settings_default): ${e.message}. " +
                    "Using hardcoded defaults."
            )
            KeyboardSettings() // Hard-coded fallback; never null
        }
    }

    /**
     * Clears the cached settings, forcing a reload on the next [loadSettings] call.
     *
     * Useful when settings file has been updated externally and should be re-read.
     */
    fun reloadSettings() {
        cachedSettings = null
    }
}
