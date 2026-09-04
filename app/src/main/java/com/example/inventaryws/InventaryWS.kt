package com.example.inventaryws

import android.app.Application
import com.example.inventaryws.core.firebase.di.firebaseModule
import com.example.inventaryws.core.supabase.di.supabaseModule
import com.example.inventaryws.create.di.createModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class InventaryWS : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@InventaryWS)
            modules(
                firebaseModule,
                createModule,
                supabaseModule
            )
        }
    }
}