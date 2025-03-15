package com.example.cubetime.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.example.cubetime.utils.ChangeLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    settingsDataManager: SettingsDataManager,
) {
    val isDarkMode by settingsDataManager.getTheme().collectAsState(initial = false)
    val language by settingsDataManager.getLanguage().collectAsState(initial = "en")

    val coroutine = rememberCoroutineScope()
    Column {
        Text("${language}")

        Button(
            onClick = {
                coroutine.launch {
                    settingsDataManager.saveSettings(
                        SettingsData(
                            isDarkMode = isDarkMode,
                            language = "ru"
                        )
                    )
                    ChangeLanguage(settingsDataManager.context, "ru")
                }
            }
        ) {
            Text(text = "Русский")
        }

        Button(
            onClick = {
                coroutine.launch {
                    settingsDataManager.saveSettings(
                        SettingsData(
                            isDarkMode = isDarkMode,
                            language = "en"
                        )
                    )
                    ChangeLanguage(settingsDataManager.context, "en")
                }
            }
        ) {
            Text(text = "English")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = if (isDarkMode) "Темная тема" else "Светлая тема")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isDarkMode,
                onCheckedChange = { isChecked ->
                    coroutine.launch {
                        settingsDataManager.saveSettings(
                            SettingsData(
                                isDarkMode = isChecked,
                                language = language
                            )
                        )
                    }
                }
            )
        }
    }
}


