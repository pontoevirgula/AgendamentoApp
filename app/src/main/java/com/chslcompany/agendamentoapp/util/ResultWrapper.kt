package com.chslcompany.agendamentoapp.util

/**
 * Sealed class que representa o estado de uma operação de rede ou repositório.
 * Substitui o uso direto de Result<T> nos use cases e ViewModel,
 * permitindo distinção entre erros de rede, erros HTTP e erros genéricos.
 */
sealed class ResultWrapper<out T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: AppException) : ResultWrapper<Nothing>()
    data object Loading : ResultWrapper<Nothing>()
}

/**
 * Hierarquia de exceções da aplicação para tratamento centralizado na UI.
 */
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    /** Sem conexão com a internet ou timeout */
    class NetworkException(cause: Throwable? = null) :
        AppException("Sem conexão com a internet. Verifique sua rede.", cause)

    /** Servidor retornou um código HTTP de erro */
    class HttpException(val code: Int, message: String) :
        AppException("Erro do servidor ($code): $message")

    /** Recurso não encontrado (404) */
    class NotFoundException(resource: String = "Recurso") :
        AppException("$resource não encontrado.")

    /** Erro de validação / regra de negócio (400/422) */
    class ValidationException(message: String) : AppException(message)

    /** Erro genérico não mapeado */
    class UnknownException(cause: Throwable? = null) :
        AppException("Ocorreu um erro inesperado. Tente novamente.", cause)
}

/** Extensão para transformar Result<T> em ResultWrapper<T> */
fun <T> Result<T>.toResultWrapper(): ResultWrapper<T> =
    fold(
        onSuccess = { ResultWrapper.Success(it) },
        onFailure = { ResultWrapper.Error(it.toAppException()) }
    )

fun Throwable.toAppException(): AppException = when (this) {
    is AppException -> this
    is java.net.UnknownHostException,
    is java.net.ConnectException,
    is java.net.SocketTimeoutException -> AppException.NetworkException(this)
    else -> AppException.UnknownException(this)
}