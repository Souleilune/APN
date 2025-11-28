package com.example.apn.util

import com.example.apn.BuildConfig

object Constants {
    // Supabase credentials loaded from BuildConfig (secure)
    // These are injected during build from local.properties
    val SUPABASE_URL: String = BuildConfig.SUPABASE_URL
    val SUPABASE_ANON_KEY: String = BuildConfig.SUPABASE_ANON_KEY

    // API endpoints
    val BASE_URL: String = BuildConfig.BASE_URL
}