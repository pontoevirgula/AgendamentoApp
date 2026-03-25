package com.chslcompany.agendamentoapp.util

import java.time.ZoneId

object ApiConstants {
    const val BASE_URL = "http://72.60.240.6:8080/"
    const val TIMEOUT_SECONDS = 30L
    val FUSO_HORARIO = ZoneId.of("America/Sao_Paulo")

    object DateFormat {
        const val ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss"
    }
}
