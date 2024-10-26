package com.example.bloodlink.activitys

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bloodlink.R
import com.google.firebase.storage.FirebaseStorage

class ReceptoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_receptores)

        // Ajuste para sistema de barras
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val card1 = findViewById<ImageView>(R.id.card1)
//        // URL da imagem da internet
//        val imageUrl =
//            "https://firebasestorage.googleapis.com/v0/b/bloodlink-462f7.appspot.com/o/foto1.jpg?alt=media&token=85999992-085b-4611-9862-4db2e89a91f7"
//
//        // Usar Glide para carregar a imagem na ImageView
//        Glide.with(this)
//            .load(imageUrl)
//            .into(card1) // Substitua 'imageView' pelo ID da sua ImageView

        // Referência ao Firebase Storage (FUNCIONANDO DESTA FORMA)
        val storageReference = FirebaseStorage.getInstance().reference.child("foto1.jpg")

        // Obter a URL de download
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            // Usar Glide para carregar a imagem na ImageView
            Glide.with(this)
                .load(uri) // Carrega a URL da imagem
                .into(card1) // Substitua pelo ID da sua ImageView
        }.addOnFailureListener { exception ->
            // Tratar erro se houver falha ao obter a URL
            exception.printStackTrace()
        }

    }
}
