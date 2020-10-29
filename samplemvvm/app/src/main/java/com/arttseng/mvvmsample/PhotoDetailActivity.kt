package com.arttseng.mvvmsample

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.arttseng.mvvmsample.model.PhotoData
import com.arttseng.mvvmsample.util.toast
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File


class PhotoDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val data :PhotoData? = intent.getParcelableExtra("DATA")
        if(data!=null) {
            init(data)
        } else {
            toast("無資料顯示")
        }

    }

    private fun init(data: PhotoData) {
        tv_id.text = data.id.toString()
        tv_title.text = data.title
        downloadImage(data.url, img_thumb)
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

    private lateinit var dialog: ProgressDialog
    private var msg: String? = ""
    private var lastMsg = ""
    private var filename = "temp.png"

    private fun statusMessage(filename: String, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $filename"

            else -> "There's nothing to download"
        }
        return msg
    }
    private fun downloadImage(url: String, img: ImageView) {
        val directory = File("/storage/emulated/0/" + Environment.DIRECTORY_PICTURES)
        filename = url.substring(url.lastIndexOf("/")+1)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val imgFile = File(directory, filename )
        if (imgFile.exists()) {
            Log.e("TEST", "Bingo! cache")
            loadImage(imgFile, img)
            return
        }

        dialog = ProgressDialog.show(
            this, "",
            getString(R.string.loading), true
        )
        dialog.show()

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    filename
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        MainScope().launch(Dispatchers.IO) {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(imgFile.path, status)
                if (msg != lastMsg) {
                    //Log.e("TEST", msg)
                    if(msg!=null && msg!!.contains("successfully")) {
                        MainScope().launch(Dispatchers.Main) {
                            loadImage(imgFile, img)
                        }
                    }

                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
            dialog.cancel()
        }
    }

    private fun loadImage(imgFile: File , img: ImageView) {
        //Log.e("TEST", imgFile.absolutePath + " exists? "+imgFile.exists())
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            img.setImageBitmap(myBitmap)
        }
    }
}
