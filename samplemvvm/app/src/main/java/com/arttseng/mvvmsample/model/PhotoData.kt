package com.arttseng.mvvmsample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
"albumId": 1,
"id": 1,
"title": "accusamus beatae ad facilis cum similique qui sunt",
"url": "https://via.placeholder.com/600/92c952",
"thumbnailUrl": "https://via.placeholder.com/150/92c952"
 */
@Parcelize
data class PhotoData(val albumId:Int, val id:Int, val title:String, val url:String, val thumbnailUrl:String , val rgbHex:String):
    Parcelable

