package com.example.foodsearch

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class RecipeActivity : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var star1: ImageView
    private lateinit var star2: ImageView
    private lateinit var star3: ImageView
    private lateinit var star4: ImageView
    private lateinit var star5: ImageView
    private lateinit var currentUserEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        titleTextView = findViewById(R.id.titleTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        authorTextView = findViewById(R.id.authorNameTextView)
        imageView = findViewById(R.id.recipeImageView)
        star1 = findViewById(R.id.star1)
        star2 = findViewById(R.id.star2)
        star3 = findViewById(R.id.star3)
        star4 = findViewById(R.id.star4)
        star5 = findViewById(R.id.star5)

        val recipeDocumentId = intent.getStringExtra("recipeDocumentId")
        if (recipeDocumentId != null) {
            FirebaseFirestore.getInstance().collection("recipes").document(recipeDocumentId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val recipe = document.toObject(Recipe::class.java)
                        if (recipe != null) {
                            titleTextView.text = recipe.title
                            descriptionTextView.text = recipe.description
                            authorTextView.text = recipe.authorName
                            Picasso.get().load(recipe.imageUrl).into(imageView)
                            currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
                            checkCurrentUserEmail(recipe.uemail)
                            setupRatingBar(recipeDocumentId)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    showToast("Error fetching recipe data: ${exception.message}")
                }
        } else {
            showToast("Recipe document ID not provided")
        }
    }

    private fun checkCurrentUserEmail(recipeAuthorEmail: String) {
        if (currentUserEmail == recipeAuthorEmail) {
            showToast("You cannot rate your own recipe")
            star1.isEnabled = false
            star2.isEnabled = false
            star3.isEnabled = false
            star4.isEnabled = false
            star5.isEnabled = false
        }
    }

    private fun setupRatingBar(recipeDocumentId: String) {
        val starImageViews = arrayOf(star1, star2, star3, star4, star5)
        for (i in 0 until starImageViews.size) {
            starImageViews[i].setOnClickListener { onStarClicked(recipeDocumentId, i + 1) }
        }
    }

    private fun onStarClicked(recipeDocumentId: String, clickedRating: Int) {
        updateRating(recipeDocumentId, clickedRating)
    }

    private fun updateRating(recipeDocumentId: String, newRating: Int) {
        FirebaseFirestore.getInstance().collection("recipes").document(recipeDocumentId)
            .update("rating", newRating)
            .addOnSuccessListener {
                showToast("Rating updated successfully")
                updateRatingUI(newRating)
            }
            .addOnFailureListener { e ->
                showToast("Failed to update rating: ${e.message}")
            }
    }

    private fun updateRatingUI(rating: Int) {
        val starResources = arrayOf(
            R.drawable.black_star,
            R.drawable.gold_star
        )
        val starImageViews = arrayOf(star1, star2, star3, star4, star5)

        for (i in 0 until rating) {
            starImageViews[i].setImageResource(starResources[1])
        }
        for (i in rating until starImageViews.size) {
            starImageViews[i].setImageResource(starResources[0])
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
