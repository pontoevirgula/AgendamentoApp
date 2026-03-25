package com.chslcompany.agendamentoapp.presentation.form

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


private val fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy")

// Máscara HH:mm — transforma "1220" em "12:20" visualmente
private object HoraMascara : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text // apenas dígitos, ex: "1220"
        val result = buildString {
            digits.forEachIndexed { i, c ->
                if (i == 2) append(':')
                append(c)
            }
        }
        val offsetMap = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                if (offset <= 2) offset else offset + 1

            override fun transformedToOriginal(offset: Int): Int =
                if (offset <= 2) offset else offset - 1
        }
        return TransformedText(AnnotatedString(result), offsetMap)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendamentoFormScreen(
    viewModel: AgendamentoFormViewModel,
    onConcluidoComSucesso: (String) -> Unit,
    onVoltar: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Dígitos brutos da hora, ex: "1220" para "12:20"
    var horaDigitos by remember { mutableStateOf("") }
    var erroHora by remember { mutableStateOf<String?>(null) }

    // Pré-preenche ao editar
    LaunchedEffect(uiState.agendamentoEmEdicao) {
        uiState.dataHoraAgendamento?.let { dataHora ->
            horaDigitos = "%02d%02d".format(dataHora.hour, dataHora.minute)
        }
    }

    // Sincroniza com ViewModel quando os 4 dígitos são válidos
    fun sincronizarHora(digitos: String, data: LocalDate?) {
        erroHora = null
        if (digitos.length == 4 && data != null) {
            val h = digitos.substring(0, 2).toInt()
            val m = digitos.substring(2, 4).toInt()
            when {
                h !in 0..23 -> erroHora = "Hora inválida (00–23)"
                m !in 0..59 -> erroHora = "Minuto inválido (00–59)"
                else -> viewModel.onDataHoraChange(
                    LocalDateTime.of(data, LocalTime.of(h, m, 0))
                )
            }
        }
    }

    LaunchedEffect(uiState.salvoComSucesso) {
        if (uiState.salvoComSucesso) {
            val mensagem = if (uiState.isEditando) {
                "Agendamento atualizado com sucesso"
            } else {
                "Agendamento criado com sucesso"
            }
            onConcluidoComSucesso(mensagem)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.isEditando) "Editar agendamento" else "Novo agendamento")
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = uiState.cliente,
                onValueChange = viewModel::onClienteChange,
                label = { Text("Nome do cliente *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )

            OutlinedTextField(
                value = uiState.servico,
                onValueChange = viewModel::onServicoChange,
                label = { Text("Serviço *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            OutlinedTextField(
                value = uiState.profissional,
                onValueChange = viewModel::onProfissionalChange,
                label = { Text("Profissional *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )

            OutlinedTextField(
                value = uiState.telefoneCliente,
                onValueChange = viewModel::onTelefoneChange,
                label = { Text("Telefone") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // Campo Data — DatePickerDialog nativo
            OutlinedTextField(
                value = uiState.dataHoraAgendamento?.toLocalDate()?.format(fmtData) ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Data *") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {
                        val hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"))
                        val dataAtual = uiState.dataHoraAgendamento?.toLocalDate() ?: hoje
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                val novaData = LocalDate.of(year, month + 1, day)
                                // atualiza data mantendo hora já digitada
                                sincronizarHora(horaDigitos, novaData)
                                // se hora ainda não foi digitada, só salva a data sem hora
                                if (horaDigitos.length < 4) {
                                    viewModel.onDataHoraChange(
                                        LocalDateTime.of(novaData, LocalTime.of(0, 0))
                                    )
                                }
                            },
                            dataAtual.year,
                            dataAtual.monthValue - 1,
                            dataAtual.dayOfMonth
                        ).show()
                    }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar data")
                    }
                }
            )

            // Campo Hora — máscara HH:mm, teclado numérico
            OutlinedTextField(
                value = horaDigitos,
                onValueChange = { novo ->
                    // aceita só dígitos, máximo 4
                    val soDigitos = novo.filter { it.isDigit() }.take(4)
                    horaDigitos = soDigitos
                    sincronizarHora(soDigitos, uiState.dataHoraAgendamento?.toLocalDate())
                },
                label = { Text("Horário *") },
                placeholder = { Text("12:20") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = erroHora != null,
                supportingText = erroHora?.let { { Text(it) } },
                trailingIcon = {
                    Icon(Icons.Default.Schedule, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = HoraMascara
            )

            // Erro do ViewModel (validação de negócio)
            uiState.erro?.let { mensagem ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = mensagem,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = viewModel::salvar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (uiState.isEditando) "Salvar alterações" else "Criar agendamento",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
