package com.example.bloodlink.activitys

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bloodlink.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class LocalDoacaoActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_local_doacao)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    if (javaClass != ReceptoresActivity::class.java) {
                        startActivity(Intent(this, ReceptoresActivity::class.java))
                    }
                    true
                }

                R.id.nav_donation -> {
                    if (javaClass != AgendamentoActivity::class.java) {
                        startActivity(Intent(this, AgendamentoActivity::class.java))
                    }
                    true
                }

                R.id.nav_user -> {
                    if (javaClass != UsuariosActivity::class.java) {
                        startActivity(Intent(this, UsuariosActivity::class.java))
                    }
                    true
                }

                R.id.nav_location -> {
                    if (javaClass != LocalDoacaoActivity::class.java) {
                        startActivity(Intent(this, LocalDoacaoActivity::class.java))
                    }
                    true
                }

                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.selectedItemId = when (javaClass) {
            ReceptoresActivity::class.java -> R.id.nav_home
            AgendamentoActivity::class.java -> R.id.nav_donation
            UsuariosActivity::class.java -> R.id.nav_user
            LocalDoacaoActivity::class.java -> R.id.nav_location
            else -> R.id.nav_home // Um fallback, se necessário
        }
    }
}
