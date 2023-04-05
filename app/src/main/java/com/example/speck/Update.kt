package com.example.speck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.webview.MyData
import com.example.webview.MyDatabaseHelper

class Update : AppCompatActivity() {
    private lateinit var ipaddres: EditText
    private lateinit var port: EditText
    private lateinit var submit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        val toolbar = findViewById<Toolbar>(R.id.toolbarUpdate)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)


        val dbHelper = MyDatabaseHelper(this)
        ipaddres=findViewById(R.id.ipaddress)
        port=findViewById(R.id.port)
        submit=findViewById(R.id.submit)
        submit.setOnClickListener {
            AlertDialog.Builder(this,R.style.AlertDialogTheme)
                .setMessage("Are Sure the IP and Port are Correct")
                .setPositiveButton("Continue") { dialog, which ->
                    val ipAddress = ipaddres.text.toString()
                    val portString = port.text.toString()
                    val data = MyData(ipAddress, portString.toInt())
                    dbHelper.updateData(data)

                    val intent = Intent(this, MainActivity::class.java)

                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .show()

        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}