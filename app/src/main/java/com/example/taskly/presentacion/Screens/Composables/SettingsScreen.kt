package com.example.taskly.presentacion.Screens.Composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskly.presentacion.Config.OrangeLight
import com.example.taskly.presentacion.Config.OrangePrimary
import com.example.taskly.presentacion.ViewModel.ViewModelSettings
import android.app.Activity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.example.taskly.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: ViewModelSettings,
    currentRoute: String,
    onNavigate: (String) -> Unit,
) {
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.restartEvent.collect {
            val intent = (context as Activity).intent.putExtra("start_route", currentRoute)
            context.finish()
            context.startActivity(intent)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                    text = stringResource(R.string.settings_title),
                    color = OrangePrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        bottomBar = {
            TasklyBottomNav(currentRoute = currentRoute, onNavigate = onNavigate)
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // ── Dark mode card ────────────────────────────────
            SettingCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    SettingIconBox(Icons.Outlined.DarkMode)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = stringResource(R.string.dark_mode), style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium)
                        Text(
                            text = stringResource(
                            if (settings.darkMode) R.string.dark_mode_on
                            else R.string.dark_mode_off
                        ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked  = settings.darkMode,
                        onCheckedChange = { viewModel.toggleDarkMode() },
                        colors = SwitchDefaults.colors(checkedThumbColor = OrangePrimary,
                            checkedTrackColor = OrangeLight
                        ),
                    )
                }
            }

            // ── Language card ─────────────────────────────────
            SettingCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    SettingIconBox(Icons.Outlined.Language)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = stringResource(R.string.language), style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium)
                        Text(text = stringResource(
                            if (settings.language == "es") R.string.lang_es
                            else R.string.lang_en
                        ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    listOf("es" to stringResource(R.string.lang_es),
                        "en" to stringResource(R.string.lang_en)
                    ).forEach { (code, label) ->
                        val selected = settings.language == code
                        Surface(
                            onClick  = { viewModel.setLanguage(code) },
                            shape    = RoundedCornerShape(12.dp),
                            color    = if (selected) OrangePrimary else MaterialTheme.colorScheme.surface,
                            border   = if (!selected) ButtonDefaults.outlinedButtonBorder else null,
                            modifier = Modifier.weight(1f).height(48.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(label,
                                    color = if (selected) Color.White
                                    else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}