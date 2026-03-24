package com.chslcompany.agendamentoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AgendamentoDto(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("servico")
    val servico: String? = null,

    @SerializedName("profissional")
    val profissional: String? = null,

    @SerializedName("dataHoraAgendamento")
    val dataHoraAgendamento: String? = null,

    @SerializedName("cliente")
    val cliente: String? = null,

    @SerializedName("telefoneCliente")
    val telefoneCliente: String? = null
)