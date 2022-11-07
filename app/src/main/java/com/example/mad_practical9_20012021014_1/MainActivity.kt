package com.example.mad_practical9_20012021014_1

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_practical9_20012021014_1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val SMS_PERMISSION_CODE = 110
    private lateinit var binding: ActivityMainBinding
    private lateinit var SMSReceiver: SMSBroadcastReceiver
    private lateinit var al:ArrayList<SMSView>
    private lateinit var lv:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        al= ArrayList()
        lv=binding.recyclerView
        lv.layoutManager = LinearLayoutManager(this)


        binding.phoneNo.setOnClickListener{
            binding.scrollView.fullScroll(View.FOCUS_DOWN);
//            binding.scrollView.scrollTo(0, binding.scrollView.bottom);

        }

        binding.msg.setOnClickListener{
            binding.scrollView.fullScroll(View.FOCUS_DOWN);
//            binding.scrollView.scrollTo(0, binding.scrollView.bottom);
        }

        if(checkRequestPermission()){
            loadSMSInbox()
        }

        SMSReceiver= SMSBroadcastReceiver()
        registerReceiver(SMSReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        SMSReceiver.listner=ListenerImplement()
        binding.sendButton.setOnClickListener{
            val phone = binding.phoneNo.text.toString()
            val msg = binding.msg.text.toString()
            sendSMS(phone,msg)
        }
    }

    fun sendSMS(sPhoneNo: String, sMsg: String){
        //on on set on click listern of button
        if(!checkRequestPermission()){
            //toast message
            return
        }

        val smsmanager=SmsManager.getDefault()
        if(smsmanager!=null){
            smsmanager.sendTextMessage(sPhoneNo, null, sMsg, null, null)
            val builder=AlertDialog.Builder(this@MainActivity)
            builder.setTitle("SMS Send")
            builder.setMessage("$sPhoneNo\n$sMsg")
            builder.setCancelable(true)
            builder.show()
        }
    }

    inner class ListenerImplement: SMSBroadcastReceiver.Listner{
        override fun onTextReceived(sPhoneNo: String?, sMsg: String?) {
            //alertdialog
            val builder=AlertDialog.Builder(this@MainActivity)
            builder.setTitle("New SMS Received")
            builder.setMessage("$sPhoneNo\n$sMsg")
            builder.setCancelable(true)
            builder.show()
            loadSMSInbox()
        }
    }

    private val isSMSReadPermission: Boolean
        get()= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)== PackageManager.PERMISSION_GRANTED

    private val isSMSWritePermission: Boolean
        get()= ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED

    private fun requestSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_SMS)) {

        }

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.RECEIVE_SMS),
            SMS_PERMISSION_CODE)
    }

    private fun loadSMSInbox(){
        if(!checkRequestPermission()) return
        val uriSMS = Uri.parse("content://sms/inbox")
        val c= contentResolver.query(uriSMS, null, null, null, null)
        al.clear()
        while(c!!.moveToNext()){
            al.add(SMSView(c.getString(2),c.getString(12)))
        }
        lv.adapter=SMSViewAdapter(al)
    }

    private fun checkRequestPermission(): Boolean {
//        return isSMSReadPermission && isSMSWritePermission
        return if (!isSMSReadPermission || !isSMSWritePermission) {
            requestSMSPermission()
            false
        } else true
    }

    override fun onDestroy() {
        unregisterReceiver(SMSReceiver)
        super.onDestroy()
    }
}