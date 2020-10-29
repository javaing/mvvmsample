package com.arttseng.mvvmsample.util

class API {
    private val photoUrl = "https://jsonplaceholder.typicode.com/photos"

    fun getPhotos():String {
        return HttpHelper.instance.downlaod(photoUrl)
    }

    companion object {

        var api: API? = null
        val instance: API
            get() {
                if (api == null) {
                    api = API()
                }
                return api as API
            }
    }
}