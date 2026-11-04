package com.nova.animelibrary
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
class MainActivity:Activity(){ private lateinit var web:WebView; private var selected="video"; private var current=""
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);web=WebView(this);web.settings.javaScriptEnabled=true;web.settings.domStorageEnabled=true;web.webViewClient=WebViewClient();web.webChromeClient=WebChromeClient();web.addJavascriptInterface(Bridge(),"Nova");setContentView(web);web.loadUrl("file:///android_asset/index.html")}
 inner class Bridge { @JavascriptInterface fun importVideo(name:String){selected=name;runOnUiThread{startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).setType("video/*").addCategory(Intent.CATEGORY_OPENABLE),701)}}
 @JavascriptInterface fun playExternal(name:String){runOnUiThread{val f=File(File(filesDir,"videos"),name);if(!f.exists()){Toast.makeText(this@MainActivity,"Video not found",Toast.LENGTH_SHORT).show();return@runOnUiThread};val u=FileProvider.getUriForFile(this@MainActivity,"com.nova.animelibrary.provider",f);val i=Intent(Intent.ACTION_VIEW).setDataAndType(u,"video/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);try{startActivity(Intent.createChooser(i,"Open with"))}catch(e:Exception){Toast.makeText(this@MainActivity,"No video player installed",Toast.LENGTH_LONG).show()}}}
 }
 @Deprecated("Deprecated") override fun onActivityResult(req:Int,res:Int,data:Intent?){super.onActivityResult(req,res,data);if(req==701&&res==RESULT_OK){val u:Uri=data?.data?:return;try{val dir=File(filesDir,"videos").apply{mkdirs()};val n=selected.replace(Regex("[^A-Za-z0-9_-]"),"_")+"_"+System.currentTimeMillis()+".mp4";contentResolver.openInputStream(u)?.use{input->File(dir,n).outputStream().use{input.copyTo(it)}};web.evaluateJavascript("window.videoImported && window.videoImported("+org.json.JSONObject.quote(n)+")",null)}catch(e:Exception){Toast.makeText(this,"Import failed",Toast.LENGTH_LONG).show()}}}
}
