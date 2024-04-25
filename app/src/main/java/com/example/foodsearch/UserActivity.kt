package com.example.foodsearch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserActivity : AppCompatActivity() {

    private lateinit var emailTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var showPasswordButton: Button
    private lateinit var logoutButton: Button
    private lateinit var favouritesButton: Button

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        emailTextView = findViewById(R.id.emailTextView)
        passwordTextView = findViewById(R.id.passwordTextView)
        logoutButton = findViewById(R.id.logoutButton)
        favouritesButton = findViewById(R.id.favouritesButton)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        emailTextView.text = currentUser?.email ?: "Email not found"

        logoutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        favouritesButton.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
        }
    }
}
