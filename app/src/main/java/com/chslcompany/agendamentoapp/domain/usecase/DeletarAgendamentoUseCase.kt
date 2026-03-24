package com.chslcompany.agendamentoapp.domain.usecase

import com.chslcompany.agendamentoapp.domain.repository.AgendamentoRepository
import com.chslcompany.agendamentoapp.util.AppException
import java.time.LocalDateTime

class DeletarAgendamentoUseCase(
    private val repository: AgendamentoRepository
) {
    suspend operator fun invoke(
        cliente: String,
        dataHoraAgendamento: LocalDateTime
    ): Result<Unit> {
        if (cliente.isBlank())
            return Result.failure(
                AppException.ValidationException("É necessário cliente para deletar")
            )
        return repository.deletar(cliente, dataHoraAgendamento)
    }
}
