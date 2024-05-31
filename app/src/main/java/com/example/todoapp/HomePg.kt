package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomePg : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RvAdapter
    private val itemList: MutableList<Model> = ArrayList()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_pg)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val plus = findViewById<ImageView>(R.id.plus)
        plus.setOnClickListener {
            val intent = Intent(this, TaskAdd::class.java)
            startActivity(intent)
        }
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dataList = intent.getStringArrayListExtra("dataList")
        if (dataList != null) {
            for (data in dataList) {
                itemList.add(Model(data))
            }
        }
        adapter = RvAdapter(this, itemList)
        recyclerView.adapter = adapter
    }
}