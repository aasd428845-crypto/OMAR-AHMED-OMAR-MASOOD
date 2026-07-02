package com.wasal.app.data.remote

import io.github.jan-tennert.supabase.SupabaseClient
import io.github.jan-tennert.supabase.createSupabaseClient
import io.github.jan-tennert.supabase.postgrest.Postgrest
import io.github.jan-tennert.supabase.postgrest.postgrest
import io.github.jan-tennert.supabase.auth.Auth
import io.github.jan-tennert.supabase.auth.auth
import io.github.jan-tennert.supabase.storage.Storage
import io.github.jan-tennert.supabase.storage.storage
import io.github.jan-tennert.supabase.realtime.Realtime
import io.github.jan-tennert.supabase.realtime.realtime

object WasalSupabase {
    const val SUPABASE_URL = "https://hhqhoqwpebnmfuhwhllw.supabase.co"
    const val SUPABASE_KEY = "YOUR_SUPABASE_ANON_KEY"

    val client: SupabaseClient by lazy {
        createSupabaseClient(SUPABASE_URL, SUPABASE_KEY) {
            install(Auth) {
                scheme = "wasal"
                host = "callback"
            }
            install(Postgrest)
            install(Storage)
            install(Realtime)
        }
    }

    val auth get() = client.auth
    val db get() = client.postgrest
    val storage get() = client.storage
    val realtime get() = client.realtime
}
