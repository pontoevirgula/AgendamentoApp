package com.chslcompany.agendamentoapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chslcompany.agendamentoapp.presentation.form.AgendamentoFormScreen
import com.chslcompany.agendamentoapp.presentation.form.AgendamentoFormViewModel
import com.chslcompany.agendamentoapp.presentation.list.AgendamentoListScreen
import com.chslcompany.agendamentoapp.presentation.list.AgendamentoListViewModel
import org.koin.androidx.compose.koinViewModel


sealed class Screen(val route: String) {
    data object Lista : Screen("lista")
    data object Form  : Screen("form")
}

@Composable
fun AgendaNavHost(
    navController: NavHostController = rememberNavController()
) {
    val listViewModel: AgendamentoListViewModel = koinViewModel()
    val formViewModel: AgendamentoFormViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Lista.route
    ) {
        composable(Screen.Lista.route) {
            AgendamentoListScreen(
                viewModel = listViewModel,
                onNovoCLick = {
                    formViewModel.resetar()
                    navController.navigate(Screen.Form.route)
                },
                onEditarClick = { agendamento ->
                    formViewModel.carregarParaEdicao(agendamento)
                    navController.navigate(Screen.Form.route)
                }
            )
        }

        composable(Screen.Form.route) {
            AgendamentoFormScreen(
                viewModel = formViewModel,
                onConcluidoComSucesso = { mensagem ->
                    navController.popBackStack()
                    listViewModel.carregarAgendamentos()
                    listViewModel.mostrarSucesso(mensagem)
                },
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}