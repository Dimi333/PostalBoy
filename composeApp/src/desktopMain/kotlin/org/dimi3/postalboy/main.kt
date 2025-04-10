package org.dimi3.postalboy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import postalboy.composeapp.generated.resources.Res
import postalboy.composeapp.generated.resources.compose_multiplatform

fun main() = application {
    var secondWindowOpened by remember { mutableStateOf(true) }
    var apiResponse by remember { mutableStateOf("Čakám...") }
    val scope = rememberCoroutineScope()

    Window(
        onCloseRequest = ::exitApplication,
        title = "PostalBoy",
    ) {
        App()

        if (secondWindowOpened) {
            Window(
                onCloseRequest = { secondWindowOpened = false },
                title = "PostalBoy",
                state = WindowState(width = 800.dp, height = 800.dp),
                resizable = false
            ) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = {
                        scope.launch {
                            apiResponse = ApiService().fetchData()
                        }
                    }) {
                        Text("Click me!")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = apiResponse)
                }
            }
        }
    }
}