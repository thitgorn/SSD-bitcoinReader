package com.example.thitiwat.bxreader101

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var exchangeJsonData: String = ""

    private final val PREFS_FILENAME = "bx_pref_name"
    private final val EXCHANGE_JSON_KEY = "exchange_json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        restoreExchangeData()
    }

    override fun onStop() {
        super.onStop()
        saveExchangeData()
    }

    private fun saveExchangeData() {
        val pref = getSharedPreferences(PREFS_FILENAME , Context.MODE_PRIVATE)
//        val editor = pref.edit()
//        editor.putString(EXCHANGE_JSON_KEY , exchangeJsonData)
//        editor.commit()
        with(pref.edit()){
            putString(EXCHANGE_JSON_KEY, exchangeJsonData)
            commit()
        }
    }

    private fun restoreExchangeData() {
        val pref = getSharedPreferences(PREFS_FILENAME , Context.MODE_PRIVATE)
        var jsonData = pref.getString(EXCHANGE_JSON_KEY , null)
        if(jsonData != null ){
            exchangeJsonData = jsonData;
            updateExchangeData()
        }
    }

    private fun updateExchangeData() {
        exchangeDataTextView.text = this.exchangeJsonData;
    }

    fun refreshButtonClicked(view: View) {
        exchangeDataTextView.text = "Refreshing"
        val task = BxLoaderTask()
        task.execute()
    }

    inner class BxLoaderTask : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            return URL("https://bx.in.th/api/").readText()
        }

        override fun onPostExecute(result: String?) {
            if(result!=null) {
                exchangeJsonData = result
                exchangeDataTextView.text = result
                updateExchangeData()
            }
        }

    }
}
