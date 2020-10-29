package com.arttseng.mvvmsample.view

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.arttseng.mvvmsample.R
import com.arttseng.mvvmsample.model.PhotoData
import com.arttseng.mvvmsample.util.rgbBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*


internal class GridAdapter(
    val ctx: Context,
    data: ArrayList<PhotoData>,
    val listener: View.OnClickListener
) :
    ListAdapter<PhotoData, GridAdapter.MyViewHolder>(DiffCallback()) {
    private val dataSet: ArrayList<PhotoData> = data

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var ll:FrameLayout = itemView.findViewById<View>(R.id.layout_item_photo) as FrameLayout
        var tvId: TextView = itemView.findViewById<View>(R.id.tv_id) as TextView
        var tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        var imgThumb: ImageView = itemView.findViewById<View>(R.id.img_thumb) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {
        val ll = holder.ll
        val tvId = holder.tvId
        val tvTitle = holder.tvTitle

        tvId.text = dataSet[listPosition].id.toString()
        tvTitle.text = dataSet[listPosition].title
        holder.imgThumb.rgbBackground(dataSet[listPosition].rgbHex)

        with(ll) {
            tag = dataSet[listPosition]
            setOnClickListener {
                listener.onClick(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


}

class DiffCallback : DiffUtil.ItemCallback<PhotoData>() {
    override fun areItemsTheSame(oldItem: PhotoData, newItem: PhotoData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PhotoData, newItem: PhotoData): Boolean {
        return oldItem == newItem
    }
}

