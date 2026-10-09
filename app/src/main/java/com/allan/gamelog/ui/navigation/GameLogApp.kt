package com.allan.gamelog.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.allan.gamelog.ui.screens.comingsoon.ComingSoonScreen
import com.allan.gamelog.ui.screens.gamelist.GameListScreen

// Raiz do app: a barra inferior fixa e, acima dela, a tela da aba selecionada.
@Composable
fun GameLogApp() {
    // O NavController é quem sabe em que tela estamos e lembra a pilha de telas.
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomBarDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination?.hasRoute(destination.routeClass) == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                // Voltar ao início da pilha evita empilhar a mesma aba várias
                                // vezes, e salvar/restaurar o estado mantém o scroll de cada aba.
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = stringResource(destination.labelRes),
                            )
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<HomeRoute> { ComingSoonScreen() }
            composable<SearchRoute> { ComingSoonScreen() }
            composable<AddRoute> { ComingSoonScreen() }
            composable<ActivityRoute> { ComingSoonScreen() }
            // Por enquanto o perfil mostra a lista de jogos do usuário.
            composable<ProfileRoute> { GameListScreen() }
        }
    }
}
