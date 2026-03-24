package com.chslcompany.agendamentoapp.data.mapper

import com.chslcompany.agendamentoapp.data.remote.dto.AgendamentoDto
import com.chslcompany.agendamentoapp.domain.model.Agendamento
import com.chslcompany.agendamentoapp.util.ApiConstants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private val isoFormatter = DateTimeFormatter.ofPattern(ApiConstants.DateFormat.ISO_DATE_TIME)

/**
 * Converte DTO de rede → modelo de domínio.
 * dataHoraAgendamento é parseada com segurança; null se o formato for inválido.
 */
fun AgendamentoDto.toDomain(): Agendamento = Agendamento(
    id = id,
    servico = servico.orEmpty(),
    profissional = profissional.orEmpty(),
    dataHoraAgendamento = dataHoraAgendamento?.let {
        runCatching { LocalDateTime.parse(it, isoFormatter) }.getOrNull()
    },
    cliente = cliente.orEmpty(),
    telefoneCliente = telefoneCliente.orEmpty()
)

/**
 * Converte modelo de domínio → DTO para envio ao backend.
 * id é omitido em criação (null); incluído em atualização.
 */
fun Agendamento.toDto(): AgendamentoDto = AgendamentoDto(
    id = id,
    servico = servico,
    profissional = profissional,
    dataHoraAgendamento = dataHoraAgendamento?.format(isoFormatter),
    cliente = cliente,
    telefoneCliente = telefoneCliente
)