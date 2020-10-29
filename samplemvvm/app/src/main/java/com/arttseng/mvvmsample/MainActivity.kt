package com.arttseng.mvvmsample

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arttseng.mvvmsample.util.toast
import kotlinx.android.synthetic.main.activity_launch.*

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        tv1.setOnClickListener {
            if(isNetworkConnected(this)) {
                val intent = Intent(this, PhotoListActivity::class.java)
                startActivity(intent)
            } else {
               toast("無網路連線")
            }
        }
    }

    private fun isNetworkConnected(ctx: Context): Boolean {
        val cm = ctx.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n) ?: return false
            return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        }
        return false
    }

}