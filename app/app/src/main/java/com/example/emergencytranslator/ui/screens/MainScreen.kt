package com.example.emergencytranslator.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen: Screen? = getCurrentScreen(currentDestination?.route)

    Scaffold(bottomBar = {
        NavBar(
            navController = navController, currentDestination = currentDestination
        )
    }, topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(id = currentScreen?.title ?: R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }, navigationIcon = {
            if (showBackButton(currentDestination?.route)) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    content = {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, contentDescription = ""
                        )
                    },
                )
            }
        })
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
                    startDestination = Screen.SettingsScreen.route, route = Screen.Settings.route
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
                            viewModel = hiltViewModel()
                        )
                    }
                    composable(Screen.DownloadSTTData.route) {
                        DownloadSSTDataScreen(
                            viewModel = hiltViewModel()
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
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
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

private fun getCurrentScreen(route: String?): Screen? {
    return when (route) {
        Screen.Translator.route -> Screen.Translator
        Screen.History.route -> Screen.History
        Screen.SettingsScreen.route -> Screen.SettingsScreen
        Screen.DownloadSTTData.route -> Screen.DownloadSTTData
        Screen.DownloadTranslationData.route -> Screen.DownloadTranslationData
        else -> null
    }
}

private fun showBackButton(route: String?): Boolean {
    return when (route) {
        Screen.DownloadTranslationData.route -> true
        Screen.DownloadSTTData.route -> true
        else -> false
    }
}

sealed class Screen(val route: String, @StringRes val title: Int, @DrawableRes val icon: Int) {
    object Translator : Screen("translator", R.string.translator, R.drawable.baseline_translate_24)
    object History : Screen("history", R.string.history, R.drawable.baseline_history_24)
    object Settings : Screen("settings", R.string.settings, R.drawable.baseline_settings_24)
    object SettingsScreen :
        Screen("settings-screen", R.string.settings, R.drawable.baseline_settings_24)

    object DownloadTranslationData : Screen(
        "download-translation",
        R.string.settings_download_translation,
        R.drawable.baseline_settings_24
    )

    object DownloadSTTData :
        Screen("download-stt", R.string.settings_download_stt, R.drawable.baseline_settings_24)
}