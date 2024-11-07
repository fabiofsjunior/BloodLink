package com.example.bloodlink.activitys

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bloodlink.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class LocalDoacaoActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var mapView: MapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_local_doacao)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        setContentView(R.layout.activity_local_doacao)

        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        mapView.setMultiTouchControls(true)

        val recifeLocation = GeoPoint(-8.0476, -34.8770)
        mapView.controller.setZoom(20.0)
        mapView.controller.setCenter(recifeLocation)

        // Adiciona um marcador no mapa
        val marker = Marker(mapView)
        marker.position = recifeLocation
        marker.title = "Recife"
        marker.icon = resources.getDrawable(R.drawable.marker_icon)
        mapView.overlays.add(marker)

        // Atualiza o mapa
        mapView.invalidate()


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
