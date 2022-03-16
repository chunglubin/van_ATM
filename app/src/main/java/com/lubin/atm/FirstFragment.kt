package com.lubin.atm

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.lubin.atm.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    var remember=false
    private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    /*val selectPictureFromGallery=
        registerForActivityResult(ActivityResultContracts.GetContent()){uri->
            uri.let {
                binding.imageView.setImageURI(it)
                uri.toString()//content://ccc.ddd/sss/aaa 2022.3.16
            }
        }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
        /*
        val s="iiiiiii"
        val uri=Uri.parse(s)
        return binding.root2022.3.16
        */
    }
    /*private fun pickFromGallery(){
        selectPictureFromGallery.launch("image/*")2022.3.16
    }*/*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireContext().getSharedPreferences("atm", Context.MODE_PRIVATE)
        val checked=pref.getBoolean("Rem_user0name",remember)
        checked.also {
            binding.idRemember.isChecked = it
        }
        binding.idRemember.setOnCheckedChangeListener { compoundButton, checked ->//是否確認已勾
            remember=checked
            pref.edit().putBoolean("rem_username",remember).apply()
            if(!checked){
                pref.edit().putString("USER","").apply()
            }
        }
        val prefuser=pref.getString("User","")
        //val prefpassword=pref.getString("Password","")
        if(prefuser!="")binding.idInputName.setText(prefuser)
        //else if(prefpassword!="")binding.idInputPassword.setText(prefpassword)
        //if(prefuser!="")binding.idInputName.text.toString()

        binding.buttonFirst.setOnClickListener {
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            //login stuff
            val username=binding.idInputName.text.toString()
            val password=binding.idInputPassword.text.toString()
            if(username== "lubin" && password== "1234" ){//save username to preferences
                //val pref = requireContext().getSharedPreferences("atm", Context.MODE_PRIVATE)
                //var user = pref.getString("User","")
                if(remember) {
                    pref.edit()//要不要記
                        .putString("User", username)
                        .putInt("Level", 3)//第三關
                        .apply()//有空馬上寫入.commit
                }
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }else{
                //error
                AlertDialog.Builder(requireContext())
                    .setTitle("Login")
                    .setMessage("Login Failed")
                    .setPositiveButton("OK",null)
                    .show()
            }
        }
        /*binding.buttonFirst.setOnClickListener {
            val intent=Intent(context,SecondFragment::class.java)
            startActivity(intent)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}