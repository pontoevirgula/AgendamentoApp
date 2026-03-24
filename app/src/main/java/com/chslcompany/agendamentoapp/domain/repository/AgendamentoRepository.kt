package com.chslcompany.agendamentoapp.domain.repository

import com.chslcompany.agendamentoapp.domain.model.Agendamento
import java.time.LocalDate
import java.time.LocalDateTime

interface AgendamentoRepository {
    suspend fun buscarAgendamentosDoDia(data: LocalDate): Result<List<Agendamento>>

    suspend fun criar(agendamento: Agendamento): Result<Agendamento>

    suspend fun atualizar(
        agendamento: Agendamento,
        cliente: String,
        dataHoraAgendamento: LocalDateTime
    ): Result<Agendamento>

    suspend fun deletar(
        cliente: String,
        dataHoraAgendamento: LocalDateTime
    ): Result<Unit>
}