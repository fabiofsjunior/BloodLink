package com.example.bloodlink.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bloodlink.MainActivity
import com.example.bloodlink.R
import com.google.firebase.auth.FirebaseAuth

class UsuariosActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth


    private lateinit var fotoPerfil: ImageView
    private lateinit var textNomeUsuario: TextView
    private lateinit var textTipoSanguineo: TextView
    private lateinit var textDataNascimento: TextView
    private lateinit var textCidadeUf: TextView
    private lateinit var buttonLogout: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_usuarios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val logoutButton = findViewById<ImageView>(R.id.buttonLogout)
        logoutButton.setOnClickListener(this)


    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonLogout -> {
                logout()
            }
        }
    }

    private fun logout() {
        auth.signOut() // Faz o logout
        // Você pode redirecionar para a tela de login ou mostrar uma mensagem
        Toast.makeText(this, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show()
        // Redirecionar para a tela de login, por exemplo:
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza a MainActivity
    }
}