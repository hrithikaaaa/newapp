package com.example.newapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newapp.ui.theme.NewappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNav(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val tasks = remember { mutableStateListOf<String>() }

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            TodoScreen(
                tasks = tasks,
                onOpenSummary = { navController.navigate("summary") }
            )
        }
        composable("summary") {
            SummaryScreen(
                count = tasks.size,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun TodoScreen(
    tasks: MutableList<String>,
    onOpenSummary: () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("New task") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.padding(top = 8.dp)) {
            Button(onClick = {
                if (text.isNotBlank()) {
                    tasks.add(text)
                    text = ""
                }
            }) {
                Text("Add")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onOpenSummary) {
                Text("Summary")
            }
        }

        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(tasks) { task ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = task)
                    TextButton(onClick = { tasks.remove(task) }) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryScreen(
    count: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "You have $count tasks")
        Button(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) {
            Text("Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    NewappTheme {
        AppNav()
    }
}
