package com.example.cubetime.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.dataStore
import androidx.navigation.NavController
import com.example.cubetime.R
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.utils.ChangeLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsDataManager: SettingsDataManager,
    viewModel: SharedViewModel,
    navController: NavController
) {
    val isDarkMode by settingsDataManager.getTheme().collectAsState(initial = false)
    val language by settingsDataManager.getLanguage().collectAsState(initial = "en")
    val isInspection by settingsDataManager.getInspection().collectAsState(initial = false)
    val timehidden by settingsDataManager.getTimeHidden().collectAsState(initial = false)
    val delayEnabled by settingsDataManager.getDelay().collectAsState(initial = false)

    var showLanguageSheet by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()




    Column(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxSize(),
    ) {
        //вверх настроек
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                navController.popBackStack()
                viewModel.changeSettingsVisibility()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrowback),
                    contentDescription = "Back"
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(end = 40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        //БЛОК ЯЗЫКА
        Text(
            text = stringResource(R.string.language),
            modifier = Modifier.padding(start = 70.dp),
        )
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Иконка
                Icon(
                    painter = painterResource(id = R.drawable.globealt),
                    contentDescription = "Generate scramble",
                    modifier = Modifier.padding(start = 10.dp, end = 20.dp).size(20.dp)
                )

                // Текст
                Text(
                    text = stringResource(R.string.russia),
                    fontSize = 25.sp
                )
            }

            IconButton(
                onClick = { showLanguageSheet = true },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp, top = 13.dp)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chevrondown),
                    contentDescription = "Select language",
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))


        //БЛОК ТЕМЫ
        Text(
            text = stringResource(R.string.theme),
            modifier = Modifier.padding(start = 70.dp),

            )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Иконка
                Icon(
                    painter = painterResource(id = R.drawable.switchthemes),
                    contentDescription = "Generate scramble",
                    modifier = Modifier.padding(start = 10.dp, end = 20.dp).size(20.dp)
                )

                // Текст
                Text(
                    text = if (isDarkMode) stringResource(R.string.Dark) else stringResource(R.string.Light),
                    fontSize = 25.sp
                )
            }
            //кнопка
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp, top = 13.dp)) {
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isChecked ->
                        coroutine.launch {
                            settingsDataManager.setTheme(isChecked)
                        }
                    }
                )
            }


        }


        Spacer(modifier = Modifier.height(10.dp))


        //БЛОК ИНСПЕКЦИИ
        Text(
            text = stringResource(R.string.settinstimer),
            modifier = Modifier.padding(start = 70.dp),

            )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Иконка
                Icon(
                    painter = painterResource(id = R.drawable.inspection),
                    contentDescription = "Generate scramble",
                    modifier = Modifier.padding(start = 10.dp, end = 20.dp).size(20.dp)
                )

                // Текст
                Text(
                    text = stringResource(R.string.INSPECTION),
                    fontSize = 25.sp
                )
            }
            //кнопка
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp, top = 13.dp)) {
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isInspection,
                    onCheckedChange = { isChecked ->
                        coroutine.launch {
                            settingsDataManager.setInspection(isChecked)
                        }
                    }
                )
            }


        }

        //блок задержки
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Иконка
                Icon(
                    painter = painterResource(id = R.drawable.hand),
                    contentDescription = "Generate scramble",
                    modifier = Modifier.padding(start = 10.dp, end = 20.dp).size(20.dp)
                )

                // Текст
                Text(
                    text = stringResource(R.string.delay),
                    fontSize = 25.sp
                )
            }
            //кнопка
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp, top = 13.dp)) {
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = delayEnabled,
                    onCheckedChange = { isChecked ->
                        coroutine.launch {
                            settingsDataManager.setDelay(isChecked)
                        }
                    }
                )
            }


        }

        //блок скрытия таймера
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Иконка
                Icon(
                    painter = painterResource(id = R.drawable.hide),
                    contentDescription = "Generate scramble",
                    modifier = Modifier.padding(start = 10.dp, end = 20.dp).size(20.dp)
                )

                // Текст
                Text(
                    text = stringResource(R.string.TIMEEVENT),
                    fontSize = 25.sp
                )
            }
            //кнопка
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp, top = 13.dp)) {
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = timehidden,
                    onCheckedChange = { isChecked ->
                        coroutine.launch {
                            settingsDataManager.setTimeIsHidden(isChecked)
                        }
                    }
                )
            }


        }

    }


//проверка открыто ли окно с выбором языка
    if (showLanguageSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLanguageSheet = false }
        ) {
            LanguageSelectionSheet(
                onLanguageSelected = { selectedLanguage ->
                    coroutine.launch {
                        settingsDataManager.setLanguage(selectedLanguage)
                        ChangeLanguage(settingsDataManager.context, selectedLanguage)
                        showLanguageSheet = false
                    }
                }
            )
        }
    }

}
//выбор языка
@Composable
fun LanguageSelectionSheet(
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf("Русский" to "ru", "English" to "en") // Список языков

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(languages) { (languageName, languageCode) ->
            LanguageItem(
                language = languageName,
                onClick = { onLanguageSelected(languageCode) }
            )
        }
    }
}

//для списка языков
@Composable
fun LanguageItem(
    language: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }, // Обработка нажатия
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = language,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            if (language =="Русский") {
                Icon(
                    painter = painterResource(id = R.drawable.flagforrussia),
                    contentDescription = null,
                    tint = Color.Unspecified

                )
            }
            else{
                Icon(
                    painter = painterResource(id = R.drawable.flagforunitedstates),
                    contentDescription = null,
                    tint = Color.Unspecified

                )
            }
        }
    }
}

