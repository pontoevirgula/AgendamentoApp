package com.chslcompany.agendamentoapp.presentation.di

import com.chslcompany.agendamentoapp.presentation.form.AgendamentoFormViewModel
import com.chslcompany.agendamentoapp.presentation.list.AgendamentoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        AgendamentoListViewModel(
            buscarAgendamentosDoDia = get(),
            deletarAgendamento = get()
        )
    }
    viewModel {
        AgendamentoFormViewModel(
            salvarAgendamentoUseCase = get(),
            alterarAgendamentoUseCase = get()
        )
    }
}