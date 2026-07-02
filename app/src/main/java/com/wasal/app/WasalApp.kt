package com.wasal.app

import android.app.Application
import com.wasal.app.data.remote.WasalSupabase

class WasalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Reference client to trigger lazy initialization of WasalSupabase
        val supabaseClient = WasalSupabase.client
    }
}
