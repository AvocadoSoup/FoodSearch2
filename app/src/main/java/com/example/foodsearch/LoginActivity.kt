package com.example.foodsearch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.button)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    baseContext, "Please enter both email and password.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        db.collection("users")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty()) {
                                    Toast.makeText(
                                        baseContext, "User data not found.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    baseContext, "Error checking user data: $e",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }
}
