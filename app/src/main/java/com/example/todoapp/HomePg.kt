package com.example.todoapp

import android.annotation.SuppressLint
import android.content.ClipDescription
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ActivityDialogUpdateTaskBinding
import com.example.todoapp.databinding.ActivityHomePgBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.ref.Reference

class HomePg : AppCompatActivity(), TaskAdapter.OnItemClickListener {
    private val binding: ActivityHomePgBinding by lazy {
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
                    for (taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(TaskItem::class.java)
                        task?.let {
                            taskList.add(it)
                        }
                    }
                    val adapter = TaskAdapter(taskList, this@HomePg)
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

    override fun onDeleteClick(taskId: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Delete Task")
        dialog.setMessage("Are you sure you want to delete this task?")
        dialog.setIcon(R.drawable.delete)
        dialog.setPositiveButton("Yes") {DialogInterface,which ->
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                val taskReference = databaseReference.child("users").child(user.uid).child("tasks")
                taskReference.child(taskId).removeValue()
            }
            Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
        }
        dialog.setNegativeButton("No") {DialogInterface,which ->
            Toast.makeText(this, "Task not deleted", Toast.LENGTH_SHORT).show()
        }
        val alertDialog: AlertDialog = dialog.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onUpdateClick(taskId: String, currentTitle: String, currentDescription: String) {
        val dialogBinding = ActivityDialogUpdateTaskBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Update Tasks")
            .setPositiveButton("Update") { dialog, _ ->
                val newTitle = dialogBinding.editTitle.text.toString()
                val newDescription = dialogBinding.editDescription.text.toString()
                updateTaskDatabase(taskId, newTitle, newDescription)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)  // Make the dialog uncancelable
            .create()
        dialogBinding.editTitle.setText(currentTitle)
        dialogBinding.editDescription.setText(currentDescription)
        dialog.show()
    }

    override fun onTaskStatusChange(task: TaskItem) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val taskReference = databaseReference.child("users").child(user.uid).child("tasks")
            taskReference.child(task.taskId).setValue(task)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Task status updated successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Failed to update task status", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun updateTaskDatabase(taskId: String, newTitle: String, newDescription: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val taskReference = databaseReference.child("users").child(user.uid).child("tasks")
            val updateTask = TaskItem(newTitle, newDescription, taskId)
            taskReference.child(taskId).setValue(updateTask)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Task updated successfuly", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}