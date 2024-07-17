package com.example.anivault.ui.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.anivault.R
import com.example.anivault.ui.home.mainactivitylayout.AnimeSearch
import com.example.anivault.ui.home.mainactivitylayout.animeseason
import com.example.anivault.ui.home.mainactivitylayout.library
import com.example.anivault.ui.home.mainactivitylayout.profile
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadFragment(animeseason())
        bottomNav = findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home->{
                    loadFragment(animeseason())
                    true
                }
                R.id.search->{
                    loadFragment(AnimeSearch())
                    true
                }
                R.id.library->{
                    loadFragment(library())
                    true
                }
                R.id.profile->{
                    loadFragment(profile())
                    true
                }

                else -> {false}
            }
        }
    }

    private fun loadFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_main_activity,fragment)
        fragmentTransaction.commit()
    }

}