package com.example.speck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.webview.CredentialDbHelper
import com.example.webview.MyDatabaseHelper
import java.nio.charset.Charset

class Login : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val toolbar = findViewById<Toolbar>(R.id.toolbarLogin)
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false)
        }
        val overflowIcon = toolbar.overflowIcon
        if (overflowIcon != null) {
            val newIcon = DrawableCompat.wrap(overflowIcon).mutate()
            DrawableCompat.setTint(newIcon, ContextCompat.getColor(this, R.color.white))
            toolbar.overflowIcon = newIcon
        }
        webView=findViewById(R.id.webView)
        val login=findViewById<Button>(R.id.login)
        val username=findViewById<EditText>(R.id.username)
        val password=findViewById<EditText>(R.id.password)
        val creddbHelper = CredentialDbHelper(this)
        val dbHelper=MyDatabaseHelper(this)
        val dataList = dbHelper.queryData()
        fun moveToMain() {
            creddbHelper.insertData(credential(username.text.toString(),password.text.toString()))
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        login.setOnClickListener{
            val ipaddres = dataList[0].name
            val port = dataList[0].age
            webView.getSettings().setJavaScriptEnabled(true)
            webView.setWebChromeClient(WebChromeClient())
            val url = "$ipaddres:$port/product_info/login.php"
            val postData = "username=${username.text}&password=${password.text}&submit=Login"
            webView.postUrl(url, postData.toByteArray(Charset.forName("UTF-8")));
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {

                    if (url == "$ipaddres:$port/product_info/search.php") {
                        moveToMain()
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }



            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.loginmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item1 -> {
                // Handle menu item 1 click
                val intent = Intent(this, Update::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}