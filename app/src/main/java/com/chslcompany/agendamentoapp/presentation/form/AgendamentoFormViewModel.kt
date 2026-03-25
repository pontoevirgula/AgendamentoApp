package com.chslcompany.agendamentoapp.presentation.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chslcompany.agendamentoapp.domain.model.Agendamento
import com.chslcompany.agendamentoapp.domain.usecase.AlterarAgendamentoUseCase
import com.chslcompany.agendamentoapp.domain.usecase.SalvarAgendamentoUseCase
import com.chslcompany.agendamentoapp.util.AppException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AgendamentoFormViewModel(
    private val salvarAgendamentoUseCase: SalvarAgendamentoUseCase,
    private val alterarAgendamentoUseCase: AlterarAgendamentoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgendamentoFormUiState())
    val uiState: StateFlow<AgendamentoFormUiState> = _uiState.asStateFlow()

    fun carregarParaEdicao(agendamento: Agendamento) {
        _uiState.update {
            it.copy(
                agendamentoEmEdicao = agendamento,
                cliente = agendamento.cliente,
                servico = agendamento.servico,
                profissional = agendamento.profissional,
                telefoneCliente = agendamento.telefoneCliente,
                dataHoraAgendamento = agendamento.dataHoraAgendamento,
                salvoComSucesso = false,
                erro = null
            )
        }
    }

    fun onClienteChange(valor: String) = _uiState.update { it.copy(cliente = valor) }
    fun onServicoChange(valor: String) = _uiState.update { it.copy(servico = valor) }
    fun onProfissionalChange(valor: String) = _uiState.update { it.copy(profissional = valor) }
    fun onTelefoneChange(valor: String) = _uiState.update { it.copy(telefoneCliente = valor) }
    fun onDataHoraChange(valor: LocalDateTime) = _uiState.update { it.copy(dataHoraAgendamento = valor) }

    fun salvar() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            if (state.isEditando) editar(state) else criar(state)
        }
    }

    private suspend fun criar(state: AgendamentoFormUiState) {
        salvarAgendamentoUseCase(
            Agendamento(
                cliente = state.cliente,
                servico = state.servico,
                profissional = state.profissional,
                telefoneCliente = state.telefoneCliente,
                dataHoraAgendamento = state.dataHoraAgendamento
            )
        )
            .onSuccess { _uiState.update { it.copy(isLoading = false, salvoComSucesso = true) } }
            .onFailure { erro -> _uiState.update { it.copy(erro = erro.toMensagem(), isLoading = false) } }
    }

    private suspend fun editar(state: AgendamentoFormUiState) {
        val original = state.agendamentoEmEdicao ?: return
        val novaDataHora = state.dataHoraAgendamento ?: return
        alterarAgendamentoUseCase(
            agendamento = original,
            novoCliente = state.cliente,
            novaDataHora = novaDataHora
        )
            .onSuccess { _uiState.update { it.copy(isLoading = false, salvoComSucesso = true) } }
            .onFailure { erro -> _uiState.update { it.copy(erro = erro.toMensagem(), isLoading = false) } }
    }

    fun limparErro() = _uiState.update { it.copy(erro = null) }

    fun resetar() = _uiState.update { AgendamentoFormUiState() }

    private fun Throwable.toMensagem(): String = when (this) {
        is AppException.NetworkException    -> "Sem conexão com a internet"
        is AppException.ValidationException -> message ?: "Verifique os campos"
        is AppException.HttpException       -> "Erro do servidor ($code)"
        else                                -> "Ocorreu um erro inesperado"
    }

}