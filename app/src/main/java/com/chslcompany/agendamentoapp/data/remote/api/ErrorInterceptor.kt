package com.chslcompany.agendamentoapp.data.remote.api

import com.chslcompany.agendamentoapp.util.AppException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor OkHttp que inspeciona TODAS as respostas HTTP.
 * Quando o código não é 2xx, lança a AppException correta
 * para que o repositório não precise tratar código HTTP manualmente.
 */
class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = try {
            chain.proceed(request)
        } catch (e: java.net.UnknownHostException) {
            throw AppException.NetworkException(e)
        } catch (e: java.net.ConnectException) {
            throw AppException.NetworkException(e)
        } catch (e: java.net.SocketTimeoutException) {
            throw AppException.NetworkException(e)
        }

        if (!response.isSuccessful) {
            val errorBody = response.body?.string()?.takeIf { it.isNotBlank() }
                ?: response.message.takeIf { it.isNotBlank() }
                ?: "Erro desconhecido"

            throw when (response.code) {
                400 -> AppException.ValidationException(errorBody)
                404 -> AppException.NotFoundException()
                422 -> AppException.ValidationException(errorBody)
                else -> AppException.HttpException(response.code, errorBody)
            }
        }

        return response
    }
}
