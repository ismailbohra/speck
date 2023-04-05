package com.example.speck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.webview.MyData
import com.example.webview.MyDatabaseHelper

class Setting : AppCompatActivity() {
    private lateinit var ipaddres: EditText
    private lateinit var port: EditText
    private lateinit var submit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val dbHelper = MyDatabaseHelper(this)
        val toolbar = findViewById<Toolbar>(R.id.toolbarSetting)
        setSupportActionBar(toolbar)
        ipaddres=findViewById(R.id.ipaddress)
        port=findViewById(R.id.port)
        submit=findViewById(R.id.submit)
        submit.setOnClickListener {
            val ipAddress = ipaddres.text.toString()
            val portString = port.text.toString()
            val data = MyData(ipAddress, portString.toInt())
            dbHelper.insertData(data)

            val intent = Intent(this, MainActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}