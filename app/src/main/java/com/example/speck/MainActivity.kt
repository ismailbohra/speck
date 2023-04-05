package com.example.speck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.webview.CredentialDbHelper
import com.example.webview.MyDatabaseHelper
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var title:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
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

        webView = findViewById(R.id.webView)
        title=findViewById(R.id.titleTextView)


        val padding = resources.getDimensionPixelSize(R.dimen.padding_16dp)
        webView.setPadding(padding, padding, padding, padding)

        val dbHelper = MyDatabaseHelper(this)
        val dataList = dbHelper.queryData()

        val creddbHelper = CredentialDbHelper(this)
        val creddataList = creddbHelper.queryData()
        if (dataList.isEmpty()) {
            val intent = Intent(this, Setting::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else if (creddataList.isEmpty()){
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }else{
            val ipaddres = dataList[0].name
            val port = dataList[0].age
            val username=creddataList[0].username
            val password =creddataList[0].password
            webView.getSettings().setJavaScriptEnabled(true)
            webView.setWebChromeClient(WebChromeClient())
            val url = "$ipaddres:$port/product_info/login.php"
            val postData = "username=$username&password=$password&submit=Login"
            webView.postUrl(url, postData.toByteArray(Charset.forName("UTF-8")));
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
            title.setOnClickListener {
                val homeurl = "$ipaddres:$port/product_info/search.php"
                webView.loadUrl(homeurl)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
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
            R.id.menu_item2 -> {
                val dbHelper = MyDatabaseHelper(this)
                val dataList = dbHelper.queryData()
                val ipaddres = dataList[0].name
                val port = dataList[0].age
                val credentialDbHelper=CredentialDbHelper(this)
                credentialDbHelper.clearData()
                webView.clearHistory()
                val intent = Intent(this,Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                true
            }
//            R.id.menu_item3 -> {
//                webView.reload()
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}