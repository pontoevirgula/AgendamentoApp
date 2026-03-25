package com.chslcompany.agendamentoapp.data.remote.api

import com.chslcompany.agendamentoapp.data.remote.dto.AgendamentoDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.time.LocalDate
import java.time.LocalDateTime

interface AgendamentoService {

    @GET("agendamentos")
    suspend fun listarAgendamentos(@Query("data") data: LocalDate): List<AgendamentoDto>

    @POST("agendamentos")
    suspend fun criarAgendamento(@Body agendamento: AgendamentoDto): AgendamentoDto

    @PUT("agendamentos")
    suspend fun atualizarAgendamento(
        @Body agendamento: AgendamentoDto,
        cliente: String,
        dataHoraAgendamento: LocalDateTime
    ): AgendamentoDto

    @DELETE("agendamentos")
    suspend fun deletarAgendamento(
        @Query("cliente") cliente: String,
        @Query("dataHoraAgendamento") dataHoraAgendamento: String
    )
}