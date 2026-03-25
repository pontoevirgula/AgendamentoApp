package com.chslcompany.agendamentoapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.chslcompany.agendamentoapp.presentation.navigation.AgendaNavHost
import com.chslcompany.agendamentoapp.presentation.theme.AgendamentoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgendamentoAppTheme {
                AgendaNavHost()
            }
        }
    }
}
