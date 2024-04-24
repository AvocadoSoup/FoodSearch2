package com.example.foodsearch

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        db = FirebaseFirestore.getInstance()

        val recipeNameEditText: EditText = findViewById(R.id.editTextText)
        val descriptionEditText: EditText = findViewById(R.id.editTextText2)
        val imageUrlEditText: EditText = findViewById(R.id.editTextText3)
        val authorNameEditText: EditText = findViewById(R.id.editTextText4)
        val genreEditText: EditText = findViewById(R.id.genreEditText)
        val addButton: Button = findViewById(R.id.button5)

        addButton.setOnClickListener {
            val recipeName = recipeNameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val imageUrl = imageUrlEditText.text.toString().trim()
            val authorName = authorNameEditText.text.toString().trim()
            val genre = genreEditText.text.toString().trim()

            if (recipeName.isNotEmpty() && description.isNotEmpty() && imageUrl.isNotEmpty() && authorName.isNotEmpty() && genre.isNotEmpty()) {
                val recipe = mapOf(
                    "title" to recipeName,
                    "description" to description,
                    "imageUrl" to imageUrl,
                    "authorName" to authorName,
                    "genre" to genre
                )

                db.collection("recipes")
                    .add(recipe)
                    .addOnSuccessListener {
                        showToast("Recipe added successfully!")
                        finish()
                    }
                    .addOnFailureListener { e ->
                        showToast("Failed to add recipe. Error: ${e.message}")
                    }
            } else {
                showToast("Please fill in all fields, including the genre")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
