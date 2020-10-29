package com.arttseng.mvvmsample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arttseng.mvvmsample.model.PhotoData
import com.arttseng.mvvmsample.util.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ViewModelGrid : ViewModel() {

    val livePhotoData: MutableLiveData<ArrayList<PhotoData>> =
        MutableLiveData<ArrayList<PhotoData>>()

    private var dataList: ArrayList<PhotoData> = ArrayList<PhotoData>()

    fun init() {
        if (dataList.size != 0) {
            livePhotoData.value = dataList
            return
        }

        MainScope().launch(Dispatchers.IO) {
            val str = API.instance.getPhotos()
            dataList = parse(str)
            MainScope().launch(Dispatchers.Main) {
                livePhotoData.setValue(dataList)
            }
        }
    }

    private fun parse(jsonData:String): ArrayList<PhotoData> {
        var data= ArrayList<PhotoData>()
        try {
            val ja = JSONArray(jsonData)
            var jo: JSONObject
            var user: PhotoData
            for (i in 0 until ja.length()) {
                    jo = ja.getJSONObject(i)
                    val albumId = jo.getInt("albumId")
                    val id = jo.getInt("id")
                    val title = jo.getString("title")
                    val url = jo.getString("url")
                    val thumbnailUrl = jo.getString("thumbnailUrl")
                    val rgbHex = thumbnailUrl.substring(thumbnailUrl.lastIndexOf("/")+1)

                user = PhotoData(albumId, id, title, url, thumbnailUrl, rgbHex)
                    data.add(user)
                }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return data
    }
}