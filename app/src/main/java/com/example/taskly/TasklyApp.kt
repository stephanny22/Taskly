package com.example.taskly

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.taskly.data.remote.FirebaseAuthDataSource
import com.example.taskly.data.repository.AuthRepositoryImpl
import com.example.taskly.data.repository.NoteRepository
import com.example.taskly.data.repository.SettingsRepository
import com.example.taskly.presentacion.Config.TasklyTheme
import com.example.taskly.presentacion.Screens.Composables.CreateEditNoteScreen
import com.example.taskly.presentacion.Screens.Composables.ForgotPasswordScreen
import com.example.taskly.presentacion.Screens.Composables.HomeScreen
import com.example.taskly.presentacion.Screens.Composables.LoginScreen
import com.example.taskly.presentacion.Screens.Composables.ProfileScreen
import com.example.taskly.presentacion.Screens.Composables.RegisterScreen
import com.example.taskly.presentacion.Screens.Composables.SettingsScreen
import com.example.taskly.presentacion.Screens.Composables.ShareNoteScreen
import com.example.taskly.presentacion.Screens.Composables.StatisticsScreen
import com.example.taskly.presentacion.Screens.Composables.VerifyCodeScreen
import com.example.taskly.presentacion.ViewModel.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.taskly.util.NotificationHelper

object Routes {
    const val LOGIN            = "login"
    const val REGISTER         = "register"
    const val FORGOT_PASSWORD  = "forgot_password"
    const val VERIFY_CODE      = "verify_code"
    const val HOME             = "home"
    const val CREATE_NOTE      = "create_note"
    const val EDIT_NOTE        = "edit_note/{noteId}"
    const val SHARE_NOTE       = "share_note/{noteId}"
    const val STATISTICS       = "statistics"
    const val SETTINGS         = "settings"
    const val PROFILE          = "profile"

    fun editNote(id: String)  = "edit_note/$id"
    fun shareNote(id: String) = "share_note/$id"
}

@Composable
fun TasklyApp() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        NotificationHelper.createChannel(context)
    }
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid ?: ""

    val authDataSource = remember {
        FirebaseAuthDataSource(firebaseAuth)
    }
    val authRepository = remember {
        AuthRepositoryImpl(authDataSource)
    }

    val database = FirebaseDatabase.getInstance().reference
    val activity  = context as Activity
    val startRoute = activity.intent.getStringExtra("start_route") ?: Routes.LOGIN
    val noteRepo     = remember { NoteRepository(database) }
    val settingsRepo = remember { SettingsRepository(context) }
    val settings by settingsRepo.settings.collectAsState()

    TasklyTheme(darkTheme = settings.darkMode) {

        val navController = rememberNavController()
        val currentEntry  by navController.currentBackStackEntryAsState()
        val currentRoute  = currentEntry?.destination?.route ?: Routes.LOGIN

        NavHost(
            navController = navController,
            startDestination = startRoute
        ) {

            // ── LOGIN ────────────────────────────────────────
            composable(Routes.LOGIN) {
                val vm = remember { ViewModelLR(authRepository) }

                LoginScreen(
                    viewModel = vm,
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(Routes.FORGOT_PASSWORD)
                    }
                )
            }

            // ── REGISTER ─────────────────────────────────────
            composable(Routes.REGISTER) {

                val vm = remember { ViewModelLR(authRepository) }
                val state by vm.uiState.collectAsState()
                val context = LocalContext.current

                LaunchedEffect(state.isLoggedIn) {
                    if (state.isLoggedIn) {
                        NotificationHelper.showWelcomeNotification(context)

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                }

                RegisterScreen(
                    viewModel = vm,
                    onRegisterSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // ── FORGOT PASSWORD ──────────────────────────────
            composable(Routes.FORGOT_PASSWORD) {
                ForgotPasswordScreen(
                    onBack = { navController.popBackStack() },
                    onCodeSent = {
                        navController.navigate(Routes.VERIFY_CODE)
                    }
                )
            }

            // ── VERIFY CODE ──────────────────────────────────
            composable(Routes.VERIFY_CODE) {
                VerifyCodeScreen(
                    onBack = { navController.popBackStack() },
                    onSuccess = {
                        // Puedes llevarlo a login o reset password
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            // ── HOME ─────────────────────────────────────────
            composable(Routes.HOME) {
                val vm = remember { ViewModelHome(noteRepo, settingsRepo, userId) }

                HomeScreen(
                    viewModel = vm,
                    currentRoute = Routes.HOME,
                    onNavigate = { navController.navigate(it) },
                    onCreateNote = { navController.navigate(Routes.CREATE_NOTE) },
                    onEditNote = { id -> navController.navigate(Routes.editNote(id)) },
                    onShareNote = { id -> navController.navigate(Routes.shareNote(id)) },
                )
            }

            // ── CREATE NOTE ──────────────────────────────────
            composable(Routes.CREATE_NOTE) {
                val vm = remember { ViewmodelCE(noteRepo,userId) }

                CreateEditNoteScreen(
                    viewModel = vm,
                    editingId = null,
                    onBack = { navController.popBackStack() },
                )
            }

            // ── EDIT NOTE ────────────────────────────────────
            composable(
                route = Routes.EDIT_NOTE,
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) { backStack ->

                val id = backStack.arguments?.getString("noteId") ?: return@composable
                val vm = remember { ViewmodelCE(noteRepo,userId) }

                CreateEditNoteScreen(
                    viewModel = vm,
                    editingId = id,
                    onBack = { navController.popBackStack() }
                )
            }

            // ── SHARE NOTE ───────────────────────────────────
            composable(
                route = Routes.SHARE_NOTE,
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) { backStack ->

                val id = backStack.arguments?.getString("noteId") ?: return@composable
                val vm = remember { ViewModelSN(noteRepo) }

                ShareNoteScreen(
                    viewModel = vm,
                    noteId = id,
                    onBack = { navController.popBackStack() }
                )
            }

            // ── STATISTICS ───────────────────────────────────
            composable(Routes.STATISTICS) {
                val vm = remember {
                    ViewModelStatics(
                        noteRepo = noteRepo,
                        userId = userId
                    )
                }

                StatisticsScreen(
                    viewModel = vm,
                    currentRoute = Routes.STATISTICS,
                    onNavigate = { navController.navigate(it) }
                )
            }

            // ── PROFILE ──────────────────────────────────────
            composable(Routes.PROFILE) {
                val vm = remember { ProfileViewModel() }

                ProfileScreen(
                    currentRoute = Routes.PROFILE,
                    onNavigate = { navController.navigate(it) },
                    viewModel = vm
                )
            }

            // ── SETTINGS ─────────────────────────────────────
            composable(Routes.SETTINGS) {
                val vm = remember { ViewModelSettings(settingsRepo) }

                SettingsScreen(
                    viewModel = vm,
                    currentRoute = Routes.SETTINGS,
                    onNavigate = { navController.navigate(it) }
                )
            }
        }
    }
}
