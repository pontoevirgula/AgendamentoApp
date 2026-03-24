package com.chslcompany.agendamentoapp.data.di

import com.chslcompany.agendamentoapp.data.remote.api.AgendamentoService
import com.chslcompany.agendamentoapp.data.remote.api.ErrorInterceptor
import com.chslcompany.agendamentoapp.domain.repository.AgendamentoRepository
import com.chslcompany.agendamentoapp.data.repository.AgendamentoRepositoryImpl
import com.chslcompany.agendamentoapp.util.ApiConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Módulo Koin responsável exclusivamente pela camada de rede:
 * Gson, OkHttp (com interceptors), Retrofit e criação das interfaces de API.
 */
val dataModule = module {

    single<Gson> {
        GsonBuilder()
            .setDateFormat(ApiConstants.DateFormat.ISO_DATE_TIME)
            .create()
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single { ErrorInterceptor() }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<ErrorInterceptor>())       // trata erros HTTP primeiro
            .addInterceptor(get<HttpLoggingInterceptor>()) // loga depois
            .connectTimeout(ApiConstants.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(ApiConstants.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(ApiConstants.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<AgendamentoService> {
        get<Retrofit>().create(AgendamentoService::class.java)
    }

    single<AgendamentoRepository> {
        AgendamentoRepositoryImpl(service = get())
    }
}