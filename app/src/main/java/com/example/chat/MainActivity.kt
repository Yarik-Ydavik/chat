package com.example.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.chat.data.Message
import com.example.chat.data.MessageState
import com.example.chat.ui.theme.ChatTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var db: FirebaseDatabase
    val viewModel : MessagesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatTheme {
                db = Firebase.database
                auth = Firebase.auth
                if (auth.currentUser == null){
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black))
                }
                else {
                    MessagesScreen( viewModel )
                }
            }
        }
    }
    
}

@Composable
fun MessagesScreen(viewModel: MessagesViewModel) {

    when (val result = viewModel.response.value){
        is MessageState.Failure -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "Ошибка загрузки сообщений: " + result.message, color = Color.Black)
            }
        }
        is MessageState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        is MessageState.Success -> {
            MessageList(result.data)
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "Ошибка загрузки сообщений", color = Color.Black)
            }
        }
    }
    /*Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        LazyColumn {
            items(updatedMessages.value) { message ->
                Text(text = "${message.name}: ${message.text}", color = Color.Black)
            }
        }
    }*/

}

@Composable
fun MessageList(data: MutableList<Message>) {
    LazyColumn{
        items(data){ message ->
            CardMessage(message)
        }
    }
}

@Composable
fun CardMessage(message: Message) {
    Text(text = message.text, color = Color.Black)
}






