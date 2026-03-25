package com.chslcompany.agendamentoapp.presentation.form

import com.chslcompany.agendamentoapp.domain.model.Agendamento
import java.time.LocalDateTime

data class AgendamentoFormUiState(
    // Campos do formulário
    val cliente: String = "",
    val servico: String = "",
    val profissional: String = "",
    val telefoneCliente: String = "",
    val dataHoraAgendamento: LocalDateTime? = null,

    // Controle de modo: null = criando, não-null = editando
    val agendamentoEmEdicao: Agendamento? = null,

    // Estados da tela
    val isLoading: Boolean = false,
    val erro: String? = null,
    val salvoComSucesso: Boolean = false
) {
    val isEditando: Boolean get() = agendamentoEmEdicao != null
}
