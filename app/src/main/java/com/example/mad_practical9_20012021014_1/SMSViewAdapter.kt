package com.example.mad_practical9_20012021014_1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.DecorContentParent
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_practical9_20012021014_1.databinding.SmsItemViewBinding

//class SMSViewAdapter(context: Context, private val array: ArrayList<SMSView>):
//    ArrayAdapter<SMSView>(context, array.size,array){
//
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View{
//            val currentSms: SMSView?= getItem(position)
//            val binding= SmsItemViewBinding.inflate(LayoutInflater.from(context))
//            binding.textViewPhoneNo.text=currentSms!!.PhoneNo
//            binding.textViewMessage.text=currentSms.Message
//            return  binding.root
//        }
//    }

class SMSViewAdapter(private  val mList: List<SMSView>):
    RecyclerView.Adapter<SMSViewAdapter.SMSViewHolder>() {
    inner class SMSViewHolder(val binding: SmsItemViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
        val binding=SmsItemViewBinding.inflate(LayoutInflater.from(parent.context))
        return SMSViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
        with(holder){
            kotlin.with(mList[position]){
                binding.textViewPhoneNo.text = this.PhoneNo
                binding.textViewMessage.text = this.Message
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}