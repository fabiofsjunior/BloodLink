package com.example.bloodlink.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodlink.R
import com.example.bloodlink.classes.ItemDecoration
import com.example.bloodlink.classes.Receptor
import com.example.bloodlink.classes.ReceptorAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReceptoresActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var receptorAdapter: ReceptorAdapter
    private lateinit var receptores: MutableList<Receptor>
    private lateinit var bottomNavigationView: BottomNavigationView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receptores)

        recyclerView = findViewById(R.id.recyclerViewReceptores)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adiciona o ItemDecoration ao RecyclerView
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_item_spacing)
        recyclerView.addItemDecoration(
            ItemDecoration(
                context = this,
                spacing = spacingInPixels,
                borderWidth = resources.getDimension(R.dimen.border_width) // Espessura da borda
            )
        )

        receptores = mutableListOf()
        receptorAdapter = ReceptorAdapter(receptores)
        recyclerView.adapter = receptorAdapter

        val database = FirebaseDatabase.getInstance().getReference("Receptores")

        // Carregar dados do Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                receptores.clear()
                for (data in snapshot.children) {
                    val receptor = data.getValue(Receptor::class.java)
                    receptor?.let { receptores.add(it) }
                }
                receptorAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ReceptoresActivity,
                    "Erro ao carregar dados",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        // Configurar o BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    if (this::class != ReceptoresActivity::class) {
                        val intent = Intent(this, ReceptoresActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_donation -> {
                    if (this::class != AgendamentoActivity::class) {
                        val intent = Intent(this, AgendamentoActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_user -> {
                    if (this::class != UsuariosActivity::class) {
                        val intent = Intent(this, UsuariosActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_location -> {
                    if (this::class != LocalDoacaoActivity::class) {
                        val intent = Intent(this, LocalDoacaoActivity::class.java)
                        startActivity(intent)
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
