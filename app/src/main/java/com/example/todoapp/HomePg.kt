package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ActivityHomePgBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.ref.Reference

class HomePg : AppCompatActivity() {
    private val binding:ActivityHomePgBinding by lazy {
        ActivityHomePgBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = binding.taskRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        binding.plus.setOnClickListener {
            startActivity(Intent(this, TaskAdd::class.java))
        }
        binding.logoutButton.setOnClickListener {
            signOut()
        }
        // Show the progress bar
        binding.progressBar.visibility = View.VISIBLE
        currentUser?.let { user ->
            val taskReference = databaseReference.child("users").child(user.uid).child("tasks")
            taskReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val taskList = mutableListOf<TaskItem>()
                    for(taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(TaskItem::class.java)
                        task?.let {
                            taskList.add(it)
                        }
                    }
                    val adapter = TaskAdapter(taskList)
                    recyclerView.adapter = adapter
                    // Hide the progress bar after loading data
                    binding.progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    // Hide the progress bar in case of error
                    binding.progressBar.visibility = View.GONE
                }
            })
        }
    }

    private fun signOut() {
        auth.signOut()
        // Redirect to login page
        val intent = Intent(this, LoginPage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}