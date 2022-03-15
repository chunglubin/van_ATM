package com.lubin.atm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lubin.atm.databinding.FragmentSecondBinding
import com.lubin.atm.databinding.RowChatroomBinding
import okhttp3.*
import okio.ByteString
import org.w3c.dom.Text
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    val chatRooms= listOf<ChatRoom>(
            ChatRoom("101101","MIau","welcome1"),//remember "," in listOf class of the last sentence.
            ChatRoom("101102","MIau2","welcome2"),
            ChatRoom("101103","MIau3","welcome3"),
            ChatRoom("101104","MIau4","welcome4"),
            ChatRoom("101105","MIau5","welcome5"),
            ChatRoom("101106","Miau6","welcome6"),
            ChatRoom("101107","Miau7","welcome7"),
            ChatRoom("101108","Miau8","welcome8"),
            ChatRoom("101109","Miau9","welcome9"),
    )
    private var _binding: FragmentSecondBinding? = null
    lateinit var webSocket: WebSocket
    val TAG=SecondFragment::class.java.simpleName
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        //web socket
        val client = OkHttpClient.Builder()
            .readTimeout(3,TimeUnit.SECONDS)//3秒連線//java
            .build()
        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=Lubin")
            .build()
        webSocket=client.newWebSocket(request, object :WebSocketListener() {
            //傾聽者
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(TAG, "onClosed: ")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(TAG, "onClosing: ")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d(TAG, "onFailure: ")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(TAG, "onMessgae: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d(TAG, "onMessgae: ${bytes.hex()}")
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, "onOpen: ")
            }
        })
        binding.idButton2.setOnClickListener{
            val message = binding.idMessage2.text.toString()
            //val json = "{\"action\": \"N\", \"content\": \"$message\" }"
            //websocket.
            //val j=Gson().toJson(Message("N"),message)
            webSocket.send(Gson().toJson(Message("N",message)))
            //webSocket.send(Gson().toJson(Message("N",message)))
        //webSocket.send()
            //recyckerview's adapter
            binding.recycler.hasFixedSize()
            binding.recycler.layoutManager=LinearLayoutManager(requireContext())
            //binding.idRecycler.adapter=""
            binding.recycler.adapter=ChatRoomAdapter()
        }
    }
    inner class ChatRoomAdapter:RecyclerView.Adapter<BindingViewHolder>(){
        //inner class ChatRoomAdapter:RecyclerView.Adapter<ChatRoomViewHoloder>(){//特殊內部型態 專門在內部做使用
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
            /*val view = layoutInflater.inflate(
                R.layout.row_chatroom,parent,false)
            return ChatRoomViewHoloder(view)*/
        val binding=RowChatroomBinding.inflate(layoutInflater,parent,false)
        return BindingViewHolder(binding)
        }

        override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
            val room=chatRooms[position]
            holder.host.setText(room.hostName)
            holder.title.setText(room.title)
        }

        override fun getItemCount(): Int {
            return chatRooms.size
        }

    }
    inner class ChatRoomViewHoloder(view: View):RecyclerView.ViewHolder(view){
        val host=view.findViewById<TextView>(R.id.id_chatroom_hostname)
        val title=view.findViewById<TextView>(R.id.id_chatroom_title)
    }

    inner class BindingViewHolder(val binding: RowChatroomBinding):
        RecyclerView.ViewHolder(binding.root){
            val host=binding.idChatroomHostname
            val title=binding.idChatroomTitle
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class Message(val action: String,val content:String)