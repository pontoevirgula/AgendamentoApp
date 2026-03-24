package com.chslcompany.agendamentoapp.domain.usecase

import com.chslcompany.agendamentoapp.domain.model.Agendamento
import com.chslcompany.agendamentoapp.domain.repository.AgendamentoRepository
import com.chslcompany.agendamentoapp.util.AppException
import java.time.LocalDateTime

class AlterarAgendamentoUseCase(
    private val repository: AgendamentoRepository
) {
    suspend operator fun invoke(
        agendamento: Agendamento,
        novoCliente: String,
        novaDataHora: LocalDateTime
    ): Result<Agendamento> {
        validar(novoCliente, novaDataHora)?.let { return Result.failure(it) }
        return repository.atualizar(agendamento, novoCliente, novaDataHora)
    }

    private fun validar(
        cliente: String,
        dataHora: LocalDateTime
    ): AppException.ValidationException? {
        if (cliente.isBlank())
            return AppException.ValidationException("Nome do cliente é obrigatório")
        if (dataHora.isBefore(LocalDateTime.now()))
            return AppException.ValidationException("Não é possível agendar em data passada")
        return null
    }
}
