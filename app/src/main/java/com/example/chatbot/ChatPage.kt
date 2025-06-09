package com.example.chatbot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AppHeader()

        MessageList(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            messageList = viewModel.messageList
        )

        MessageInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            onMessageSent = {
                viewModel.sendMessage(it)
            }
        )
    }
}


@Composable
fun MessageList(modifier: Modifier= Modifier, messageList : List<MessageModel>){
    val listState = rememberLazyListState()
    LaunchedEffect(messageList.size) {
        if (messageList.isNotEmpty()) {
            listState.animateScrollToItem(messageList.size - 1)
        }
    }
    if (messageList.isEmpty()){
        Column(modifier = modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Icon(painter = painterResource(id =R.drawable.question), contentDescription = "Icon",
                tint = Color.DarkGray,
                modifier = Modifier.size(60.dp))
            Text(text = "Ask me anything...", fontSize = 22.sp)
        }


    }else{
        LazyColumn ( state = listState,
            reverseLayout = true,
            modifier = modifier
        ){
            items(messageList.reversed()) {
                MessageRow(messageModel = it)
            }
        }

    }
}

@Composable
fun MessageRow(messageModel: MessageModel){
    val isModel = messageModel.model == "model"
    Row (verticalAlignment = Alignment.CenterVertically){
        Box(modifier = Modifier.fillMaxWidth()){
            Box(
                modifier = Modifier.align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd )
                    .padding(start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp)
                    .clip(RoundedCornerShape(48f))
                    .background(if (isModel) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary)
                    .padding(16.dp)

            ) {
                SelectionContainer {
                    Text(text = messageModel.message,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInput(modifier: Modifier=Modifier,onMessageSent:(String)->Unit){
    var message by remember { mutableStateOf("") }
    Row(modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField( modifier = Modifier.weight(1f),
            value = message, onValueChange = {message=it})
        IconButton(onClick = {
            if (message.isNotEmpty()){
                onMessageSent(message)
                message = ""
            }

        }) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send"
            )
        }
    }
}

@Composable
fun AppHeader(){
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ){
        Text(text = "Easy Bot",
            modifier = Modifier.padding(16.dp),
            fontSize = 22.sp,
            color = Color.White)
    }
}
