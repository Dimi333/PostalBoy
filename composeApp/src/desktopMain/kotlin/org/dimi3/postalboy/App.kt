package org.dimi3.postalboy

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.github.vooft.compose.treeview.core.TreeView
import io.github.vooft.compose.treeview.core.node.Branch
import io.github.vooft.compose.treeview.core.node.Leaf
import io.github.vooft.compose.treeview.core.tree.Tree
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Composable
fun TreeViewExample() {
    // build tree structure
    val tree = Tree<String> {
        Branch("Mammalia") {
            Branch("Carnivora") {
                Branch("Canidae") {
                    Branch("Canis") {
                        Leaf("Wolf")
                        Leaf("Dog")
                    }
                }
                Branch("Felidae") {
                    Branch("Felis") {
                        Leaf("Cat")
                    }
                    Branch("Panthera") {
                        Leaf("Lion")
                    }
                }
            }
        }
    }

    // render the tree
    TreeView(tree)
}

@Composable
@Preview
fun App() {
    val tabs = remember { mutableStateListOf("Tab") }
    var selectedTabIndex2 by remember { mutableStateOf(0) }

    val viewModels = remember {
        mutableStateListOf<TabViewModel>().apply {
            add(TabViewModel())
        }
    }

    DisposableEffect(tabs.size) {
        onDispose { }
    }

    Row {
        Box(modifier = Modifier.width(300.dp)) {
            TreeViewExample()
        }
        Box(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex2, backgroundColor = Color.White) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, fontSize = 12.sp) },
                        selected = selectedTabIndex2 == index,
                        onClick = { selectedTabIndex2 = index },
                    )
                }
                Tab(
                    text = { Text("+", fontSize = 12.sp) },
                    selected = false,
                    onClick = {
                        tabs.add("Tab")
                        viewModels.add(TabViewModel())
                        selectedTabIndex2 = tabs.size - 1
                    }
                )
            }
            CallTab(viewModel = viewModels[selectedTabIndex2])
        }
    }
}

data class TabUiState(
    val url: String = "",
    val bearer: String = "",
    val apiResponse: ApiResponse = ApiResponse.Initial,
)

class TabViewModel : ViewModel() {
    private val _uiState =
        MutableStateFlow(TabUiState(url = "https://jsonplaceholder.typicode.com/todos/1", bearer = ""))
    val uiState: StateFlow<TabUiState> = _uiState.asStateFlow()

    fun updateUrl(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                url = value,
            )
        }
    }

    fun updateBearer(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                bearer = value,
            )
        }
    }

    fun updateApiResponse(value: ApiResponse) {
        _uiState.update { currentState ->
            currentState.copy(
                apiResponse = value,
            )
        }
    }
}

@Composable
fun CallTab(viewModel: TabViewModel = viewModel { TabViewModel() }) {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        var attrs by remember { mutableStateOf("") }
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
        val uiState by viewModel.uiState.collectAsState()


        Column(
            Modifier.fillMaxWidth().padding(0.dp, 40.dp, 0.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth().padding(2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { expanded = !expanded },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
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

                TextField(
                    modifier = Modifier.weight(1f),
                    value = uiState.url,
                    onValueChange = { viewModel.updateUrl(it) },
//                    label = { Text("Host: https://jsonplaceholder.typicode.com/todos/1") },
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )

                Button(onClick = {
                    scope.launch {
                        try {
                            visiblePreloader = !visiblePreloader
                            viewModel.updateApiResponse(
                                ApiService().fetchData(
                                    uiState.url,
                                    method,
                                    attrs,
                                    uiState.bearer
                                )
                            )
                        } finally {
                            visiblePreloader = !visiblePreloader
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                    Text("Go", modifier = Modifier.alpha(if (visiblePreloader) 0f else 1f))
                    CircularProgressIndicator(
                        modifier = Modifier.alpha(if (visiblePreloader) 1f else 0f)
                    )
                }

                Button(
                    onClick = { expandedEnvironment = !expandedEnvironment },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
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

            Row(modifier = Modifier.padding(2.dp)) {
                Column {
                    TabRow(selectedTabIndex, backgroundColor = Color.White) {
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
                            value = uiState.bearer,
                            onValueChange = { viewModel.updateBearer(it) },
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
                    TabRow(selectedResponseTabIndex, backgroundColor = Color.White) {
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
                                    Text(text = "Status: " + uiState.apiResponse.status.toString(), fontSize = 12.sp)
                                }
                                Row {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        SelectionContainer {
                                            Text(
                                                text = uiState.apiResponse.body,
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
                            uiState.apiResponse.headers.forEach { (key, value) ->
                                Text("$key: ${value.joinToString(", ")}", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}