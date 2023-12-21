package com.example.recyclerview

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val list= ArrayList<video>()
    private val viewModel by viewModels<MainModelView> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView=findViewById(R.id.vv_video)
        recyclerView.setHasFixedSize(true)
        list.addAll(getList())
        showRecyclerList()
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, Login::class.java))
                finish()
            }
        }
        setupView()
    }


    private fun getList():ArrayList<video>{
        val gambar=resources.obtainTypedArray(R.array.data_gambar)
        val dataName=resources.getStringArray(R.array.judul_video)
        val dataDesripsi=resources.getStringArray(R.array.data_dekripsi)
        val videoId=resources.obtainTypedArray(R.array.video_id)
        val listvideo=ArrayList<video>()
        for (i in dataName.indices){
            val video=video(gambar.getResourceId(i,-1),dataName[i],dataDesripsi[i],videoId.getResourceId(i,-1))
            listvideo.add(video)
        }
        return listvideo

    }
    private fun showRecyclerList(){
        recyclerView.layoutManager=LinearLayoutManager(this)
        val listadapter=ListAdapter(list)
        recyclerView.adapter=listadapter
    }
    private fun performLogout() {
        viewModel.logout()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                performLogout()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

}