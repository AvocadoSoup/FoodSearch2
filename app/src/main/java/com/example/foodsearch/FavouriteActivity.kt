package com.example.foodsearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodsearch.RecipeAdapter
import com.example.foodsearch.Recipe
import com.google.firebase.firestore.FirebaseFirestore

class FavouriteActivity : AppCompatActivity() {

    private lateinit var recyclerViewFavourite: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        recyclerViewFavourite = findViewById(R.id.recyclerViewFavourite)

        recipeAdapter = RecipeAdapter { recipeDocumentId ->
            val intent = Intent(this, RecipeActivity::class.java).apply {
                putExtra("recipeDocumentId", recipeDocumentId)
            }
            startActivity(intent)
        }

        recyclerViewFavourite.apply {
            layoutManager = LinearLayoutManager(this@FavouriteActivity)
            adapter = recipeAdapter
        }

        fetchFavouriteRecipes()
    }

    private fun fetchFavouriteRecipes() {
        val db = FirebaseFirestore.getInstance()

        db.collection("recipes")
            .whereEqualTo("rating", 5)
            .get()
            .addOnSuccessListener { result ->
                val recipes = mutableListOf<Recipe>()
                for (document in result) {
                    val recipe = document.toObject(Recipe::class.java)
                    recipes.add(recipe)
                }
                recipeAdapter.submitList(recipes)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch recipes: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val TAG = "FavouriteActivity"
    }
}
