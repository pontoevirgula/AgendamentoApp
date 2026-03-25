package com.chslcompany.agendamentoapp

import android.app.Application
import com.chslcompany.agendamentoapp.data.di.dataModule
import com.chslcompany.agendamentoapp.domain.di.domainModule
import com.chslcompany.agendamentoapp.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AgendamentoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AgendamentoApplication)
            modules(
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}