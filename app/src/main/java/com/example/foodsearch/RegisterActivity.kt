package com.example.foodsearch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText: EditText = findViewById(R.id.email2)
        val passwordEditText: EditText = findViewById(R.id.password2)
        val registerButton: Button = findViewById(R.id.button2)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    baseContext, "Please enter both email and password.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = hashMapOf(
                            "email" to email,
                            "password" to password
                        )

                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    baseContext, "Error adding user data to Firestore: $e",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            baseContext, "Registration failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }
}
