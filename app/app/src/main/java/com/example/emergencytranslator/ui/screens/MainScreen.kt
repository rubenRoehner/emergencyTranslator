package com.example.emergencytranslator.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.emergencytranslator.R
import com.example.emergencytranslator.ui.screens.history.HistoryScreen
import com.example.emergencytranslator.ui.screens.settings.SettingsScreen
import com.example.emergencytranslator.ui.screens.settings.download.DownloadSSTDataScreen
import com.example.emergencytranslator.ui.screens.settings.download.DownloadTranslationDataScreen
import com.example.emergencytranslator.ui.screens.translator.TranslatorScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(bottomBar = {
        NavBar(
            navController = navController, currentDestination = currentDestination
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, Screen.Translator.route) {
                composable(Screen.Translator.route) {
                    TranslatorScreen(viewModel = hiltViewModel())
                }
                composable(Screen.History.route) {
                    HistoryScreen(viewModel = hiltViewModel())
                }
                navigation(
                    startDestination = Screen.SettingsScreen.route,
                    route = Screen.Settings.route
                ) {
                    composable(Screen.SettingsScreen.route) {
                        SettingsScreen(
                            viewModel = hiltViewModel(),
                            onNavigateToDownloadSTTData = { navController.navigate(Screen.DownloadSTTData.route) },
                            onNavigateToDownloadTranslateData = { navController.navigate(Screen.DownloadTranslationData.route) },
                        )
                    }
                    composable(Screen.DownloadTranslationData.route) {
                        DownloadTranslationDataScreen(
                            viewModel = hiltViewModel(),
                            popBackStack = { navController.popBackStack() },
                        )
                    }
                    composable(Screen.DownloadSTTData.route) {
                        DownloadSSTDataScreen(
                            viewModel = hiltViewModel(),
                            popBackStack = { navController.popBackStack() },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavBar(navController: NavController, currentDestination: NavDestination?) {
    val items = listOf(
        Screen.History, Screen.Translator, Screen.Settings
    )
    BottomNavigation {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = screen.icon), contentDescription = null
                    )
                },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = MaterialTheme.colors.onPrimary,
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
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
            )
        }
    }
}

sealed class Screen(val route: String, @StringRes val title: Int, @DrawableRes val icon: Int) {
    object Translator : Screen("translator", R.string.translator, R.drawable.baseline_translate_24)
    object History : Screen("history", R.string.history, R.drawable.baseline_history_24)
    object Settings : Screen("settings", R.string.settings, R.drawable.baseline_settings_24)
    object SettingsScreen :
        Screen("settings-screen", R.string.settings, R.drawable.baseline_settings_24)

    object DownloadTranslationData :
        Screen("download-translation", R.string.settings, R.drawable.baseline_settings_24)

    object DownloadSTTData :
        Screen("download-stt", R.string.settings, R.drawable.baseline_settings_24)
}