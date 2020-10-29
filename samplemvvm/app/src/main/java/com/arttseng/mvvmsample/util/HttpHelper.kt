package com.arttseng.mvvmsample.util

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class HttpHelper private constructor() {

    private fun connect(jsonURL: String): Any {
        try {
            val url = URL(jsonURL)
            val con = url.openConnection() as HttpURLConnection

            con.requestMethod = "GET"
            con.connectTimeout = 15000
            con.readTimeout = 15000
            con.doInput = true

            return con

        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return "URL ERROR " + e.message

        } catch (e: IOException) {
            e.printStackTrace()
            return "CONNECT ERROR " + e.message
        }
    }

    fun downlaod(jsonURL:String): String {
        val connection = connect(jsonURL)
        if (connection.toString().startsWith("Error")) {
            return connection.toString()
        }
        //DOWNLOAD
        try {
            val con = connection as HttpURLConnection
            //if response is HTTP OK
            if (con.responseCode == 200) {
                //GET INPUT FROM STREAM
                val ins = BufferedInputStream(con.inputStream)
                ins.use {
                    val br = BufferedReader(InputStreamReader(ins))
                    br.use {
                        val jsonData = StringBuffer()
                        var line: String?
                        do {
                            line = br.readLine()
                            if (line == null){ break}
                            jsonData.append(line);
                        } while (true)

                        return jsonData.toString()
                    }
                }
            } else {
                return "Error " + con.responseMessage
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return "Error " + e.message
        }
    }


    companion object {
        var helper: HttpHelper? = null
        val instance: HttpHelper
            get() {
                if (helper == null) {
                    helper = HttpHelper()
                }
                return helper as HttpHelper
            }
    }
}

