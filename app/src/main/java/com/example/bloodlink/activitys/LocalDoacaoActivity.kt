package com.example.bloodlink.activitys

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bloodlink.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class LocalDoacaoActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var mapView: MapView
    private lateinit var database: FirebaseDatabase
    private lateinit var userCityUf: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_local_doacao)

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        database = FirebaseDatabase.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val userRef = database.getReference("Usuarios").child(userId)
            userRef.child("cidadeUf").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userCityUf = snapshot.getValue(String::class.java).orEmpty()
                    carregarLocaisDoacao()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@LocalDoacaoActivity,
                        "Erro ao carregar dados: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
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
                        startActivity(Intent(this, DoeAgoraActivity::class.java))
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

    private fun carregarLocaisDoacao() {
        val locaisRef = database.getReference("LocaisDoacao")
        locaisRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var localMaisProximo: GeoPoint? = null
                var menorDistancia = Float.MAX_VALUE

                for (localSnapshot in snapshot.children) {
                    val cidadeUf =
                        localSnapshot.child("Cidade_UF").getValue(String::class.java).orEmpty()
                    val latitude =
                        localSnapshot.child("Latitude").getValue(Double::class.java) ?: 0.0
                    val longitude =
                        localSnapshot.child("Longitude").getValue(Double::class.java) ?: 0.0
                    val ponto = GeoPoint(latitude, longitude)

                    // Verifica a proximidade com a cidade do usuário
                    if (cidadeUf == userCityUf) {
                        val resultado = FloatArray(1)
                        Location.distanceBetween(
                            latitude,
                            longitude,
                            ponto.latitude,
                            ponto.longitude,
                            resultado
                        )
                        if (resultado[0] < menorDistancia) {
                            menorDistancia = resultado[0]
                            localMaisProximo = ponto
                        }
                    }

                    // Adiciona marcador para cada local
                    val marker = Marker(mapView)
                    marker.position = ponto
                    marker.title = cidadeUf
                    mapView.overlays.add(marker)
                }

                // Centraliza o mapa no local mais próximo, se encontrado
                if (localMaisProximo != null) {
                    mapView.controller.setCenter(localMaisProximo)
                    mapView.controller.setZoom(15.0)
                    Toast.makeText(
                        this@LocalDoacaoActivity,
                        "Local mais próximo: ${userCityUf}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@LocalDoacaoActivity,
                        "Nenhum local próximo encontrado.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@LocalDoacaoActivity,
                    "Erro ao carregar locais: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.selectedItemId = when (javaClass) {
            ReceptoresActivity::class.java -> R.id.nav_home
            DoeAgoraActivity::class.java -> R.id.nav_donation
            UsuariosActivity::class.java -> R.id.nav_user
            LocalDoacaoActivity::class.java -> R.id.nav_location
            else -> R.id.nav_home
        }
    }
}
