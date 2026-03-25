package com.chslcompany.agendamentoapp.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chslcompany.agendamentoapp.domain.usecase.BuscarAgendamentosDoDiaUseCase
import com.chslcompany.agendamentoapp.domain.usecase.DeletarAgendamentoUseCase
import com.chslcompany.agendamentoapp.util.AppException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class AgendamentoListViewModel(
    private val buscarAgendamentosDoDia: BuscarAgendamentosDoDiaUseCase,
    private val deletarAgendamento: DeletarAgendamentoUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AgendamentoListUiState())
    val uiState: StateFlow<AgendamentoListUiState> = _uiState.asStateFlow()

    init {
        carregarAgendamentos()
    }

    fun carregarAgendamentos(data: LocalDate = _uiState.value.dataSelecionada) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            buscarAgendamentosDoDia(data)
                .onSuccess { lista ->
                    _uiState.update {
                        it.copy(
                            agendamentos = lista,
                            dataSelecionada = data,
                            isLoading = false
                        )
                    }
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(
                            erro = erro.toMensagem(),
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun deletar(cliente: String, dataHoraAgendamento: LocalDateTime) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            deletarAgendamento(cliente, dataHoraAgendamento)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            mensagemSucesso = "Agendamento removido com sucesso",
                            isLoading = false
                        )
                    }
                    carregarAgendamentos()
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(
                            erro = erro.toMensagem(),
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun selecionarData(data: LocalDate) {
        carregarAgendamentos(data)
    }

    fun mostrarSucesso(mensagem: String) =
        _uiState.update { it.copy(mensagemSucesso = mensagem) }

    fun limparMensagens() {
        _uiState.update { it.copy(erro = null, mensagemSucesso = null) }
    }

    private fun Throwable.toMensagem(): String = when (this) {
        is AppException.NetworkException -> "Sem conexão com a internet"
        is AppException.NotFoundException -> "Nenhum agendamento encontrado"
        is AppException.ValidationException -> message ?: "Erro de validação"
        is AppException.HttpException -> "Erro do servidor ($code)"
        else -> "Ocorreu um erro inesperado"
    }
}