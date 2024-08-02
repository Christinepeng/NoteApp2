package com.example.noteapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteapp2.data.Note
import com.example.noteapp2.data.NoteDatabase
import com.example.noteapp2.data.NoteRepository
import com.example.noteapp2.ui.theme.NoteApp2Theme


class MainActivity : ComponentActivity() {
//    private val viewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the database and dao (this is just an example, you should provide your actual database)
        val database = NoteDatabase.getDatabase(applicationContext)
        val noteDao = database.noteDao()

        // Create the repository
        val repository = NoteRepository(noteDao)

        // Create the ViewModel using the factory
        val viewModelFactory = NoteViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)


        setContent {
            NoteApp2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "note_list_screen") {
                        composable("note_list_screen") {
                            NoteListScreen(navController, viewModel)
                        }
                        composable("add_new_item_screen") {
                            AddNewItemScreen(navController, viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteApp(viewModel: NoteViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "note_list_screen") {
        composable("note_list_screen") {
            NoteListScreen(navController, viewModel)
        }
        composable("add_new_item_screen") {
            AddNewItemScreen(navController, viewModel)
        }
    }
}

@Composable
fun NoteListScreen(navController: NavController, viewModel: NoteViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        val itemsList = Array(20) { "" }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            items(itemsList) { item ->
                ListItem(item)
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate("add_new_item_screen")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(25.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")

        }
    }

}

@Composable
fun ListItem(item: String) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "title")
            Text(text = "description")
            Spacer(modifier = Modifier.height(30.dp))

        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}

@Composable
fun AddNewItemScreen(navController: NavController, viewModel: NoteViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)

    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Enter Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Enter Description") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Button(
                onClick = {
                    viewModel.addNotes(
                        Note(
                            title = title,
                            description = description
                        )
                    )
                    title = ""
                    description = ""
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun ItemDetailScreen(viewModel: NoteViewModel = viewModel()) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)

    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Title", modifier = Modifier.padding(15.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(50.dp)
        ) {
            Text(text = "Description", modifier = Modifier.padding(15.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Row {
                Button(
                    onClick = {
                        viewModel.addNotes(
                            Note(
                                title = title,
                                description = description
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Edit")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        viewModel.deleteNotes(
                            Note(
                                title = title,
                                description = description
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Delete")
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NoteApp2Theme {
        NoteApp(viewModel = viewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun AddNewItemScreenPreView() {
    NoteApp2Theme {
        AddNewItemScreen(navController = rememberNavController(), viewModel = viewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailScreenPreview() {
    NoteApp2Theme {
        ItemDetailScreen(viewModel = viewModel())
    }
}