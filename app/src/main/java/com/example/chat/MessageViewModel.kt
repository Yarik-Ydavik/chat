package com.example.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chat.data.Message
import com.example.chat.data.MessageState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MessagesViewModel: ViewModel(){
    val response : MutableState<MessageState> = mutableStateOf(MessageState.Empty)

    init {
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        val messageList = mutableListOf<Message>()
        response.value = MessageState.Loading
        FirebaseDatabase.getInstance().getReference("message")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (mess in snapshot.children){
                        val messItem = mess.getValue(Message::class.java)
                        if (messItem != null) messageList.add(messItem)
                    }
                    response.value = MessageState.Success(messageList)
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = MessageState.Failure(error.message)
                }

            })
    }

    private val database = Firebase.database("https://hackaton2023-a7e87-default-rtdb.firebaseio.com")

    var _messages = mutableStateOf<List<Message>>(emptyList())
    var messages : State<List<Message>> = _messages
}