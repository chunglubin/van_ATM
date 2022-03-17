package com.lubin.atm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.lubin.atm.databinding.FragmentSecondBinding
import com.lubin.atm.databinding.RowChatroomBinding
import okhttp3.*
import okio.ByteString
import org.w3c.dom.Text
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private lateinit var adapter:ChatRoomAdapter
    val chatRooms = listOf<ChatRoom>(
        ChatRoom("101101", "MIau", "welcome1"),//remember "," in listOf class of the last sentence.
        ChatRoom("101102", "MIau2", "welcome2"),
        ChatRoom("101103", "MIau3", "welcome3"),
        ChatRoom("101104", "MIau4", "welcome4"),
        ChatRoom("101105", "MIau5", "welcome5"),
        ChatRoom("101106", "Miau6", "welcome6"),
        ChatRoom("101107", "Miau7", "welcome7"),
        ChatRoom("101108", "Miau8", "welcome8"),
        ChatRoom("101109", "Miau9", "welcome9"),
    )
    val rooms= mutableListOf<Lightyear>()
    private var _binding: FragmentSecondBinding? = null
    lateinit var webSocket: WebSocket
    val TAG = SecondFragment::class.java.simpleName

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
            .readTimeout(3, TimeUnit.SECONDS)//3秒連線//java
            .build()
        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=Lubin")
            .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
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
        binding.bSend.setOnClickListener {
            val message = binding.edMessage.text.toString()
            //val json = "{\"action\": \"N\", \"content\": \"$message\" }"
            //websocket.
            //val j=Gson().toJson(Message("N"),message)
            webSocket.send(Gson().toJson(MessageSend("N", message)))
            //webSocket.send(Gson().toJson(Message("N",message)))
            //webSocket.send()
            //recyckerview's adapter

        }
        binding.recycler.setHasFixedSize(true)
        //binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.layoutManager=GridLayoutManager(requireContext(),2)
        adapter=ChatRoomAdapter()
        //binding.idRecycler.adapter=""
        binding.recycler.adapter = adapter
        thread {//text message
            val json=URL("https://api.jsonserve.com/xioD4j").readText()
            val msg=Gson().fromJson(json,Message::class.java)
            Log.d(TAG, "msg:${msg.body.text}")
        }
        thread {//test chatroom list
            val json=URL("https://api.jsonserve.com/Q4AhSu").readText()
            val chatRooms=Gson().fromJson(json,ChatRooms::class.java)
            Log.d(TAG, "rooms:${chatRooms.result.lightyear_list}")
            //fill list with new coming data
            rooms.clear()
            rooms.addAll(chatRooms.result.lightyear_list)
            //List<LightYear>
            activity?.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    inner class ChatRoomAdapter:RecyclerView.Adapter<ChatRoomViewHoloder>(){
        //inner class ChatRoomAdapter:RecyclerView.Adapter<ChatRoomViewHoloder>(){//特殊內部型態 專門在內部做使用
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHoloder {
            val view = layoutInflater.inflate(
                R.layout.row_chatroom,parent,false)
            return ChatRoomViewHoloder(view)
        //val binding=RowChatroomBinding.inflate(layoutInflater,parent,false)
        //return BindingViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ChatRoomViewHoloder, position: Int) {
            val lightyear=rooms[position]
            holder.host.setText(lightyear.stream_title)
            holder.title.setText(lightyear.nickname)
            //SecondFragments.this
            Glide.with(this@SecondFragment)
                .load(lightyear.head_photo)
                .into(holder.headShot)
            /*holder.itemView.setOnClickListener {
                chatroomClicked(lightyear)
            }*/


        }//在這裡取得元件的控制(每個item內的控制)

        override fun getItemCount(): Int {
            return rooms.size
        }//return一個int，通常都會return陣列長度(arrayList.size)
    }
    inner class ChatRoomViewHoloder(view: View):RecyclerView.ViewHolder(view){
        val host=view.findViewById<TextView>(R.id.id_chatroom_hostname)
        val title=view.findViewById<TextView>(R.id.id_chatroom_title)
        val headShot=view.findViewById<ImageView>(R.id.head_shot)
    }
    /*inner class ChatRoomViewHoloder(val binding: RowChatroomBinding):
        RecyclerView.ViewHolder(binding.root){
            val host=binding.idChatroomHostname
            val title=binding.idChatroomTitle
        }
        */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //second fragment
    fun chatroomClicked(lightyear:Lightyear){

    }

}