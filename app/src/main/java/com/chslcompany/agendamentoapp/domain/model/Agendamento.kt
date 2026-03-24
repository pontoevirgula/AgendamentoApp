package com.chslcompany.agendamentoapp.domain.model

import java.time.LocalDateTime

data class Agendamento(
    val id: Long? = null,
    val servico: String = "",
    val profissional: String = "",
    val dataHoraAgendamento: LocalDateTime? = null,
    val cliente: String = "",
    val telefoneCliente: String = ""
)