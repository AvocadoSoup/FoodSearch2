package com.example.foodsearch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val addButton: Button = findViewById(R.id.button4)
        addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        db = FirebaseFirestore.getInstance()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recipeAdapter = RecipeAdapter { recipeDocumentId ->
            val intent = Intent(this, RecipeActivity::class.java).apply {
                putExtra("recipeDocumentId", recipeDocumentId)
            }
            startActivity(intent)
        }

        recyclerView.adapter = recipeAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        db.collection("recipes")
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

        val userImageView: ImageView = findViewById(R.id.imageView)
        userImageView.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        val searchButton: Button = findViewById(R.id.searchButton)
        searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}
