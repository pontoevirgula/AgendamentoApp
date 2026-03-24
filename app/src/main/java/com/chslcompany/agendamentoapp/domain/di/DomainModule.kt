package com.chslcompany.agendamentoapp.domain.di

import com.chslcompany.agendamentoapp.domain.usecase.AlterarAgendamentoUseCase
import com.chslcompany.agendamentoapp.domain.usecase.BuscarAgendamentosDoDiaUseCase
import com.chslcompany.agendamentoapp.domain.usecase.DeletarAgendamentoUseCase
import com.chslcompany.agendamentoapp.domain.usecase.SalvarAgendamentoUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { BuscarAgendamentosDoDiaUseCase(get()) }
    factory { SalvarAgendamentoUseCase(get()) }
    factory { AlterarAgendamentoUseCase(get()) }
    factory { DeletarAgendamentoUseCase(get()) }
}