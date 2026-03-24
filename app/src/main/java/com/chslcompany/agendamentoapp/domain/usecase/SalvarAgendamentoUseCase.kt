package com.chslcompany.agendamentoapp.domain.usecase

import com.chslcompany.agendamentoapp.domain.model.Agendamento
import com.chslcompany.agendamentoapp.domain.repository.AgendamentoRepository
import com.chslcompany.agendamentoapp.util.AppException
import java.time.LocalDateTime

class SalvarAgendamentoUseCase(
    private val repository: AgendamentoRepository
) {
    suspend operator fun invoke(agendamento: Agendamento): Result<Agendamento> {
        validar(agendamento)?.let { return Result.failure(it) }
        return repository.criar(agendamento)
    }

    private fun validar(agendamento: Agendamento): AppException.ValidationException? {
        if (agendamento.cliente.isBlank())
            return AppException.ValidationException("Nome do cliente é obrigatório")
        if (agendamento.servico.isBlank())
            return AppException.ValidationException("Serviço é obrigatório")
        if (agendamento.profissional.isBlank())
            return AppException.ValidationException("Profissional é obrigatório")
        if (agendamento.dataHoraAgendamento == null)
            return AppException.ValidationException("Data e horário são obrigatórios")
        if (agendamento.dataHoraAgendamento.isBefore(LocalDateTime.now()))
            return AppException.ValidationException("Não é possível agendar em data passada")
        return null
    }
}