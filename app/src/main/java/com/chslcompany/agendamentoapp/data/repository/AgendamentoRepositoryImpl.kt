package com.chslcompany.agendamentoapp.data.repository

import com.chslcompany.agendamentoapp.data.mapper.toDomain
import com.chslcompany.agendamentoapp.data.mapper.toDto
import com.chslcompany.agendamentoapp.data.remote.api.AgendamentoService
import com.chslcompany.agendamentoapp.domain.model.Agendamento
import com.chslcompany.agendamentoapp.domain.repository.AgendamentoRepository
import java.time.LocalDate
import java.time.LocalDateTime

class AgendamentoRepositoryImpl(
    private val service: AgendamentoService,
) : AgendamentoRepository {

    override suspend fun buscarAgendamentosDoDia(data: LocalDate): Result<List<Agendamento>> = runCatching {
        service.listarAgendamentos(data).map { it.toDomain() }
    }

    override suspend fun criar(agendamento: Agendamento): Result<Agendamento> = runCatching {
        service.criarAgendamento(agendamento.toDto()).toDomain()
    }

    override suspend fun atualizar(
        agendamento: Agendamento,
        cliente: String,
        dataHoraAgendamento: LocalDateTime
    ): Result<Agendamento> = runCatching {
        service.atualizarAgendamento(
            agendamento.toDto(), cliente, dataHoraAgendamento
        ).toDomain()
    }

    override suspend fun deletar(
        cliente: String,
        dataHoraAgendamento: LocalDateTime
    ): Result<Unit> = runCatching {
        service.deletarAgendamento(cliente, dataHoraAgendamento)
    }
}