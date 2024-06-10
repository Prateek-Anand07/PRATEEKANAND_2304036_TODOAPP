package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginPage : AppCompatActivity() {
    private val binding:ActivityLoginPageBinding by lazy {
        ActivityLoginPageBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    override fun onStart() {
        super.onStart()

        // check if user is already loged in
        val currentUser: FirebaseUser? = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(this, HomePg::class.java))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding.SignUpButton.setOnClickListener {
            val intent = Intent(this, SignUpPg::class.java)
            startActivity(intent)
            finish()
        }
        binding.LoginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this, "Sign-In successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, HomePg::class.java))
                            finish()
                        }
                        else {
                            Toast.makeText(this, "Sign-In failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}