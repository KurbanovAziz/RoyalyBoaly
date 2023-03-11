package com.royaly.boaly.data

import android.content.Context

class Pref(context: Context) : java.io.Serializable {

    private val  pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setAuthSeen(isSeen:Boolean){
        pref.edit().putBoolean(IS_AUTH, isSeen).apply()
    }

    fun isAuthSeen():Boolean{
        return pref.getBoolean(IS_AUTH,false)
    }

    fun saveBalance(balance : Int){
        pref.edit().putInt(BALANCE_KEY,balance).apply()
    }
    fun getBalance(): Int {
        return pref.getInt(BALANCE_KEY,0)
    }

    companion object {
        private const val PREF_NAME = "pref"
        const val IS_AUTH = "isAuthorized"
        private const val BALANCE_KEY = "name.pref"
    }
}