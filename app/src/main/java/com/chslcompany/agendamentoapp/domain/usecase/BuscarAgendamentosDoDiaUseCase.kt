package com.chslcompany.agendamentoapp.domain.usecase
import com.chslcompany.agendamentoapp.domain.model.Agendamento
import com.chslcompany.agendamentoapp.domain.repository.AgendamentoRepository
import java.time.LocalDate

/**
 * Busca todos os agendamentos de um dia específico.
 * Ordena por horário antes de devolver ao ViewModel.
 */
class BuscarAgendamentosDoDiaUseCase(
    private val repository: AgendamentoRepository
) {
    suspend operator fun invoke(data: LocalDate): Result<List<Agendamento>> {
        return repository
            .buscarAgendamentosDoDia(data)
            .map { lista ->
                lista.sortedBy { it.dataHoraAgendamento }
            }
    }
}
