package com.chslcompany.agendamentoapp

import android.app.Application
import com.chslcompany.agendamentoapp.data.di.dataModule
import com.chslcompany.agendamentoapp.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AgendaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AgendaApplication)
            modules(
                dataModule,
                domainModule
            )
        }
    }
}