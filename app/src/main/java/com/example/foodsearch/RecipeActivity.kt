// RecipeActivity.kt
package com.example.foodsearch

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
    private var rating: Int = 0

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
                            // Populate UI with recipe data
                            titleTextView.text = recipe.title
                            descriptionTextView.text = recipe.description
                            authorTextView.text = recipe.authorName
                            // Load image using Picasso or Glide
                            Picasso.get().load(recipe.imageUrl).into(imageView)
                            // Set initial rating
                            rating = recipe.rating
                            updateRating()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error fetching recipe document", exception)
                }
        } else {
            Log.e(TAG, "Recipe document ID not provided")
        }

        // Set onClick listeners for stars
        star1.setOnClickListener { onStarClicked(1) }
        star2.setOnClickListener { onStarClicked(2) }
        star3.setOnClickListener { onStarClicked(3) }
        star4.setOnClickListener { onStarClicked(4) }
        star5.setOnClickListener { onStarClicked(5) }
    }

    private fun onStarClicked(clickedRating: Int) {
        rating = clickedRating
        updateRating()

        // Update rating in Firestore
        val recipeDocumentId = intent.getStringExtra("recipeDocumentId")
        if (recipeDocumentId != null) {
            val recipeRef = FirebaseFirestore.getInstance().collection("recipes").document(recipeDocumentId)
            recipeRef
                .update("rating", rating)
                .addOnSuccessListener {
                    Log.d(TAG, "Rating updated successfully")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error updating rating", e)
                }
        } else {
            Log.e(TAG, "Recipe document ID not provided")
        }
    }


    private fun updateRating() {
        val starResources = arrayOf(
            R.drawable.black_star,
            R.drawable.gold_star
        )
        val starImageViews = arrayOf(star1, star2, star3, star4, star5)

        for (i in 0 until rating) {
            starImageViews[i].setImageResource(starResources[1]) // Gold star
        }
        for (i in rating until starImageViews.size) {
            starImageViews[i].setImageResource(starResources[0]) // Black star
        }
    }

    companion object {
        private const val TAG = "RecipeActivity"
    }
}
