package com.example.social_auth.Storage

import android.content.Context
import android.content.SharedPreferences

class PrefManager(var _context: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0
    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.commit()
        }

    companion object {
        private const val PREF_NAME = "androidhive-welcome"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"


        const val IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN"
        const val STAGE = "STAGE"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val Status = "Active"
        const val USER_ID = "USER_ID"
        const val TOKEN_TYPE = "TOKEN_TYPE"
        const val FIREBASE_TOKEN = "TOKEN_TYPE"
        const val MOBILE_NUMBER = "MOBILE_NUMBER"
        const val SLUG = "SLUG"
        const val OTP = "OTP"
        const val ADDRESS = "ADDRESS"
        const val CITY = "CITY"
        const val COUNTRY = "COUNTRY"
        const val STATE = "STATE"
        const val ZIP = "ZIP"
        const val userName = "userName"
        const val address = "first_name"
        const val resume_name = "resumename"
        const val user_image = "user_image"
        const val email = "email"
        const val name = "name"
        const val lastName = "lastName"
        const val isRegisterd = "isRegisterd"
        const val isVerified = "isVerified"
        const val saved_status = "saved_status"


        const val NOTIFICATION_GIVEAWAY_ID = "NOTIFICATION_GIVEAWAY_ID"
        fun getEditor(context: Context?): SharedPreferences.Editor {
            val sharedpreferences = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedpreferences.edit()
        }

        fun getSharedPreference(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        fun getString(context: Context, name: String?): String? {
            val sharedPreferences = getSharedPreference(context)
            return sharedPreferences.getString(name, "")
        }

        fun setString(context: Context?, name: String?, value: String?) {
            val editor = getEditor(context)
            editor.putString(name, value)
            editor.commit()
        }


    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}