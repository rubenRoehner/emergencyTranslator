package com.example.emergencytranslator.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.emergencytranslator.R
import com.example.emergencytranslator.ui.screens.history.HistoryScreen
import com.example.emergencytranslator.ui.screens.settings.SettingsScreen
import com.example.emergencytranslator.ui.screens.translator.TranslatorScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen = getCurrentScreen(currentDestination?.route)

    Scaffold(topBar = {
        AppBar(
            currentScreen = currentScreen
        )
    }, bottomBar = {
        NavBar(
            navController = navController, currentDestination = currentDestination
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, Screen.Translator.route) {
                composable(Screen.Translator.route) {
                    TranslatorScreen(viewModel = viewModel())
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(viewModel = hiltViewModel())
                }
                composable(Screen.History.route) {
                    HistoryScreen(viewModel = viewModel())
                }
            }
        }
    }
}

@Composable
private fun AppBar(currentScreen: Screen) {
    TopAppBar {
        Text(text = stringResource(id = currentScreen.title))
    }
}

@Composable
private fun NavBar(navController: NavController, currentDestination: NavDestination?) {
    val items = listOf(
        Screen.Translator, Screen.History, Screen.Settings
    )
    BottomNavigation {
        items.forEach { screen ->
            BottomNavigationItem(icon = {
                Icon(
                    painterResource(id = screen.icon), contentDescription = null
                )
            },
                label = { Text(stringResource(screen.title)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                })
        }
    }
}

private fun getCurrentScreen(route: String?): Screen {
    return when (route) {
        Screen.History.route -> Screen.History
        Screen.Settings.route -> Screen.Settings
        else -> Screen.Translator
    }
}

sealed class Screen(val route: String, @StringRes val title: Int, @DrawableRes val icon: Int) {
    object Translator : Screen("translator", R.string.translator, R.drawable.baseline_translate_24)
    object History : Screen("history", R.string.history, R.drawable.baseline_history_24)
    object Settings : Screen("settings", R.string.settings, R.drawable.baseline_settings_24)
}