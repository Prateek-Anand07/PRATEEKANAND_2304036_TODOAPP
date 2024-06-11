package com.example.todoapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.databinding.ActivityTaskAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TaskAdd : AppCompatActivity() {
    private val binding: ActivityTaskAddBinding by lazy {
        ActivityTaskAddBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        binding.btnSubmit.setOnClickListener {
            val title = binding.task.text.toString()
            val description = binding.taskDescription.text.toString()
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please give all details", Toast.LENGTH_SHORT).show()
            } else {
                val currentUser = auth.currentUser
                currentUser?.let { user ->
//                Generate a unique key for the task
                    val taskKey = databaseReference.child("users").child("tasks").push().key
//                task item instance
                    val taskItem = TaskItem(title, description, taskKey ?: "")
                    if (taskKey != null) {
                        // add tasks to user task
                        databaseReference.child("users").child(user.uid).child("tasks")
                            .child(taskKey).setValue(taskItem)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Task saved successfuly",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Failed to save your task",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
            }
        }
    }
}