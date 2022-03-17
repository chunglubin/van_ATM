package com.lubin.atm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel:ViewModel() {
    val chatRooms=MutableLiveData<List<Lightyear>>()

}