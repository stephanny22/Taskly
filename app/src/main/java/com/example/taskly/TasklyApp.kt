package com.example.taskly

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.taskly.data.repository.NoteRepository
import com.example.taskly.data.repository.SettingsRepository
import com.example.taskly.presentacion.Config.TasklyTheme
import com.example.taskly.presentacion.Screens.CreateEditNoteScreen
import com.example.taskly.presentacion.Screens.HomeScreen
import com.example.taskly.presentacion.Screens.LoginScreen
import com.example.taskly.presentacion.Screens.ProfileScreen
import com.example.taskly.presentacion.Screens.RegisterScreen
import com.example.taskly.presentacion.Screens.SettingsScreen
import com.example.taskly.presentacion.Screens.ShareNoteScreen
import com.example.taskly.presentacion.Screens.StatisticsScreen
import com.example.taskly.presentacion.ViewModel.ViewModelHome
import com.example.taskly.presentacion.ViewModel.ViewModelLR
import com.example.taskly.presentacion.ViewModel.ViewModelSN
import com.example.taskly.presentacion.ViewModel.ViewModelSettings
import com.example.taskly.presentacion.ViewModel.ViewModelStatics
import com.example.taskly.presentacion.ViewModel.ViewmodelCE

// ─────────────────────────────────────────────────────────────
// Navigation routes
// ─────────────────────────────────────────────────────────────
object Routes {
    const val LOGIN       = "login"
    const val REGISTER    = "register"
    const val HOME        = "home"
    const val CREATE_NOTE = "create_note"
    const val EDIT_NOTE   = "edit_note/{noteId}"
    const val SHARE_NOTE  = "share_note/{noteId}"
    const val STATISTICS  = "statistics"
    const val SETTINGS    = "settings"
    const val PROFILE     = "profile"

    fun editNote(id: String)  = "edit_note/$id"
    fun shareNote(id: String) = "share_note/$id"
}

// ─────────────────────────────────────────────────────────────
// TasklyApp — root composable wired into MainActivity
// ─────────────────────────────────────────────────────────────
@Composable
fun TasklyApp() {
    // Shared repositories (in production, inject via Hilt/Koin)
    val noteRepo     = remember { NoteRepository() }
    val settingsRepo = remember { SettingsRepository() }

    val settings by settingsRepo.settings.collectAsState()

    TasklyTheme(darkTheme = settings.darkMode) {
        val navController = rememberNavController()
        val currentEntry  by navController.currentBackStackEntryAsState()
        val currentRoute  = currentEntry?.destination?.route ?: Routes.LOGIN

        NavHost(
            navController    = navController,
            startDestination = Routes.LOGIN,
        ) {

            // ── Auth ──────────────────────────────────────────
            composable(Routes.LOGIN) {
                val vm = remember { ViewModelLR() }
                LoginScreen(
                    viewModel            = vm,
                    onLoginSuccess       = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                )
            }

            composable(Routes.REGISTER) {
                val vm = remember { ViewModelLR() }
                RegisterScreen(
                    viewModel         = vm,
                    onRegisterSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() },
                )
            }

            // ── Main ──────────────────────────────────────────
            composable(Routes.HOME) {
                val vm = remember { ViewModelHome(noteRepo, settingsRepo) }
                HomeScreen (
                    viewModel    = vm,
                    currentRoute = Routes.HOME,
                    onNavigate   = { navController.navigate(it) },
                    onCreateNote = { navController.navigate(Routes.CREATE_NOTE) },
                    onEditNote   = { id -> navController.navigate(Routes.editNote(id)) },
                    onShareNote  = { id -> navController.navigate(Routes.shareNote(id)) },
                )
            }

            composable(Routes.CREATE_NOTE) {
                val vm = remember { ViewmodelCE(noteRepo) }
                CreateEditNoteScreen(
                    viewModel  = vm,
                    editingId  = null,
                    onBack     = { navController.popBackStack() },
                )
            }

            composable(
                route = Routes.EDIT_NOTE,
                arguments = listOf(navArgument("noteId") { type = NavType.StringType }),
            ) { backStack ->
                val id = backStack.arguments?.getString("noteId") ?: return@composable
                val vm = remember { ViewmodelCE(noteRepo) }
                CreateEditNoteScreen(viewModel = vm, editingId = id,
                    onBack = { navController.popBackStack() })
            }

            composable(
                route = Routes.SHARE_NOTE,
                arguments = listOf(navArgument("noteId") { type = NavType.StringType }),
            ) { backStack ->
                val id = backStack.arguments?.getString("noteId") ?: return@composable
                val vm = remember { ViewModelSN(noteRepo) }
                ShareNoteScreen(viewModel = vm, noteId = id,
                    onBack = { navController.popBackStack() })
            }

            composable(Routes.STATISTICS) {
                val vm = remember { ViewModelStatics(noteRepo) }
                StatisticsScreen(viewModel = vm, currentRoute = Routes.STATISTICS,
                    onNavigate = { navController.navigate(it) })
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    currentRoute = Routes.PROFILE,
                    onNavigate   = { navController.navigate(it) },
                )
            }

            composable(Routes.SETTINGS) {
                val vm = remember { ViewModelSettings(settingsRepo) }
                SettingsScreen (viewModel = vm, currentRoute = Routes.SETTINGS,
                    onNavigate = { navController.navigate(it) })
            }
        }
    }
}