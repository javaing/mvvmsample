package com.arttseng.mvvmsample

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.arttseng.mvvmsample.model.PhotoData
import com.arttseng.mvvmsample.util.toast
import com.arttseng.mvvmsample.view.GridAdapter
import com.arttseng.mvvmsample.viewmodel.ViewModelGrid
import kotlinx.android.synthetic.main.activity_main.*


class PhotoListActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_PERMISSION = 1
    }

    private lateinit var dealVM : ViewModelGrid
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        dealVM = ViewModelProviders.of(this).get(ViewModelGrid::class.java)
        initObserve()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            askPermissions()
        } else {
            init()
        }

    }

    private fun init() {
        dealVM.init()
        dialog = ProgressDialog.show(
            this, "",
            getString(R.string.loading), true
        )
        dialog.show()
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

    private fun initObserve() {
        dealVM.livePhotoData.observe(this, Observer { it ->
            dialog.dismiss()

            val gridLayoutManager = GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
            recycle1.layoutManager = gridLayoutManager

            val adapter = GridAdapter(this, it, View.OnClickListener {
                val intent = Intent(this, PhotoDetailActivity::class.java)
                val data = it.tag as PhotoData
                intent.putExtra("DATA", data)
                startActivity(intent)
            })
            recycle1.adapter = adapter

        })
    }

    private fun askPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION)
        } else {
            init()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toast("未同意权限无法下載圖檔")
                } else {
                    init()
                }
            }

        }
    }
}
