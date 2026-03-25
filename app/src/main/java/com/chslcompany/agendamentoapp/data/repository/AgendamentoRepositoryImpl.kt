package com.chslcompany.agendamentoapp.data.repository

import com.chslcompany.agendamentoapp.data.mapper.toDomain
import com.chslcompany.agendamentoapp.data.mapper.toDto
import com.chslcompany.agendamentoapp.data.remote.api.AgendamentoService
import com.chslcompany.agendamentoapp.domain.model.Agendamento
import com.chslcompany.agendamentoapp.domain.repository.AgendamentoRepository
import com.chslcompany.agendamentoapp.util.AppException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AgendamentoRepositoryImpl(
    private val service: AgendamentoService,
) : AgendamentoRepository {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    override suspend fun buscarAgendamentosDoDia(data: LocalDate): Result<List<Agendamento>> =
        runCatching {
            service.listarAgendamentos(data).map { it.toDomain() }
        }.recover { erro ->
            if (erro is AppException.NotFoundException) emptyList()
            else throw erro
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
        service.deletarAgendamento(
            cliente = cliente,
            dataHoraAgendamento = dataHoraAgendamento.format(formatter)
        )
    }.recover { erro ->
        if (erro is AppException.NotFoundException) Unit
        else throw erro
    }
}