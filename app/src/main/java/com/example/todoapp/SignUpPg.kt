package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpPg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_pg)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnSignUp = findViewById<Button>(R.id.SignUpBtn)
        val tVLogin = findViewById<TextView>(R.id.tVLogin)
        btnSignUp.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
        tVLogin.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }
}