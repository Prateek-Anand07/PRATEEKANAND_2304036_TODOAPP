package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class TaskAdd : AppCompatActivity() {
    private lateinit var edtTask: TextInputEditText
    private lateinit var btnSubmit: Button
    companion object {
        val dataList: ArrayList<String> = ArrayList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_add)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        edtTask = findViewById(R.id.edtTask)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val userInput = edtTask.text.toString()
            if(userInput.isNotEmpty()) {
                dataList.add(userInput)
                val intent = Intent(this, HomePg::class.java)
                intent.putStringArrayListExtra("dataList", dataList)
                startActivity(intent)
            }
        }
    }
}