package com.chslcompany.agendamentoapp.presentation.list

import com.chslcompany.agendamentoapp.domain.model.Agendamento
import com.chslcompany.agendamentoapp.util.ApiConstants
import java.time.LocalDate

data class AgendamentoListUiState(
    val agendamentos: List<Agendamento> = emptyList(),
    val dataSelecionada: LocalDate = LocalDate.now(ApiConstants.FUSO_HORARIO),
    val isLoading: Boolean = false,
    val erro: String? = null,
    val mensagemSucesso: String? = null
)
