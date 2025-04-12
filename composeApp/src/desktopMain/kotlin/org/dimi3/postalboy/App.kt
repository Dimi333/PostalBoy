package org.dimi3.postalboy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import postalboy.composeapp.generated.resources.Res
import postalboy.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var apiResponse by remember { mutableStateOf("Čakám...") }
        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("") }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }

            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Host: https://jsonplaceholder.typicode.com/todos/1") },
            )

            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                scope.launch {
                    apiResponse = ApiService().fetchData(text)
                }
            }) {
                Text("Click me!")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = apiResponse)

            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }

                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}