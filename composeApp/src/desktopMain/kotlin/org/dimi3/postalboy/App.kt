package org.dimi3.postalboy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Status(status: String) {

}

@Composable
@Preview
fun App() {
    val tabs = listOf("Tab", "+")
    var selectedTabIndex2 by remember { mutableStateOf(0) }
    var tabIndex2 by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex2) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, fontSize = 12.sp) },
                    selected = tabIndex2 == index,
                    onClick = { tabIndex2 = index; selectedTabIndex2 = index },
                )
            }
        }
        when (tabIndex2) {
            0 -> CallTab()
            1 -> CallTab()
        }
    }
}

@Composable
fun CallTab() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var apiResponse by remember { mutableStateOf(ApiResponse.Initial) }
        val scope = rememberCoroutineScope()
        var url by remember { mutableStateOf("https://jsonplaceholder.typicode.com/todos/1") }
        var attrs by remember { mutableStateOf("") }
        var bearer by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        var expandedEnvironment by remember { mutableStateOf(false) }
        var method by remember { mutableStateOf("GET") }
        var environment by remember { mutableStateOf("No environment") }
        val tabs = listOf("Authorization", "Headers", "Body")
        val responseTabs = listOf("Body", "Headers")
        var tabIndex by remember { mutableStateOf(0) }
        var responseTabIndex by remember { mutableStateOf(0) }
        var visiblePreloader by remember { mutableStateOf(false) }
        var selectedTabIndex by remember { mutableStateOf(0) }
        var selectedResponseTabIndex by remember { mutableStateOf(0) }
        val scroll = rememberScrollState(0)

        Column(Modifier.fillMaxWidth().padding(0.dp, 40.dp, 0.dp, 0.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth().padding(2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Box {
                    Button(onClick = { expandedEnvironment = !expandedEnvironment }) {
                        Text(environment)
                    }
                    DropdownMenu(
                        expanded = expandedEnvironment,
                        onDismissRequest = { expandedEnvironment = false }
                    ) {
                        DropdownMenuItem(
                            content = { Text("No environment") },
                            onClick = {
                                environment = "No environment"
                                expandedEnvironment = false
                            }
                        )
                    }
                }

                Box {
                    Button(onClick = { expanded = !expanded }) {
                        Text(method)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            content = { Text("GET") },
                            onClick = {
                                method = "GET"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            content = { Text("POST") },
                            onClick = {
                                method = "POST"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            content = { Text("PUT") },
                            onClick = {
                                method = "PUT"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            content = { Text("DELETE") },
                            onClick = {
                                method = "DELETE"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            content = { Text("PATCH") },
                            onClick = {
                                method = "PATCH"
                                expanded = false
                            }
                        )
                    }
                }

                TextField(
                    modifier = Modifier.weight(1f),
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Host: https://jsonplaceholder.typicode.com/todos/1") },
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )

                Button(onClick = {
                    scope.launch {
                        try {
                            visiblePreloader = !visiblePreloader
                            apiResponse = ApiService().fetchData(url, method, attrs, bearer)
                        } finally {
                            visiblePreloader = !visiblePreloader
                        }
                    }
                }) {
                    Text("Go")
                }

                CircularProgressIndicator(modifier = Modifier.alpha(if (visiblePreloader) 1f else 0f))
            }

            Row(modifier = Modifier.padding(2.dp)) {
                Column {
                    TabRow(selectedTabIndex) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title, fontSize = 12.sp) },
                                selected = tabIndex == index,
                                onClick = { tabIndex = index; selectedTabIndex = index },
                            )
                        }
                    }
                    when (tabIndex) {
                        0 -> TextField(
                            value = bearer,
                            onValueChange = { bearer = it },
                            label = { Text("Bearer") },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 100.dp)
                                .padding(horizontal = 16.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                        )

                        1 -> TextField(
                            value = attrs,
                            onValueChange = { attrs = it },
                            label = { Text("Headers") },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 100.dp)
                                .padding(horizontal = 16.dp),
                            singleLine = false,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                        )

                        2 -> TextField(
                            value = attrs,
                            onValueChange = { attrs = it },
                            label = { Text("Body") },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 100.dp)
                                .padding(horizontal = 16.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.padding(2.dp).background(Color.White)) {
                Column {
                    TabRow(selectedResponseTabIndex, backgroundColor = MaterialTheme.colors.primaryVariant) {
                        responseTabs.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title, fontSize = 12.sp) },
                                selected = responseTabIndex == index,
                                onClick = { responseTabIndex = index; selectedResponseTabIndex = index }
                            )
                        }
                    }
                    when (responseTabIndex) {
                        0 -> Row(Modifier.border(1.dp, MaterialTheme.colors.primary).fillMaxSize().padding(16.dp)) {
                            Column {
                                Row {
                                    Text(text = "Status: " + apiResponse.status.toString(), fontSize = 12.sp)
                                }
                                Row {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        SelectionContainer {
                                            Text(
                                                text = apiResponse.body,
                                                modifier = Modifier.verticalScroll(scroll),
                                                fontSize = 12.sp,
                                                lineHeight = 15.sp
                                            )
                                        }

                                        VerticalScrollbar(
                                            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                                            adapter = rememberScrollbarAdapter(scroll)
                                        )
                                    }
                                }
                            }
                        }

                        1 -> Row {
                            apiResponse.headers.forEach { (key, value) ->
                                Text("$key: ${value.joinToString(", ")}", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }


//            Row {
//                Button(onClick = { showContent = !showContent }) {
//                    Text("Click me!")
//                }
//
//                AnimatedVisibility(showContent) {
//                    val greeting = remember { Greeting().greet() }
//
//                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                        Image(painterResource(Res.drawable.compose_multiplatform), null)
//                        Text("Compose: $greeting")
//                    }
//                }
//            }
        }
    }
}