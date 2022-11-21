package com.example.kadaituti


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.kadaituti.databinding.ActivityMainBinding
import io.realm.Realm

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)
        val grid: GridView = binding.dailyGridView
        val adapter = GridAdapter(this)
        grid.adapter = adapter

        binding.intentButton.setOnClickListener{
            val intent = Intent(this, IntentActivity::class.java)
            startActivity(intent)
        }
    }
}