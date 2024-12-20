package com.example.bloodlink.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bloodlink.MainActivity
import com.example.bloodlink.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UsuariosActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var fotoPerfil: ImageView
    private lateinit var textUserId: TextView
    private lateinit var textNomeUsuario: TextView
    private lateinit var textTipoSanguineo: TextView
    private lateinit var textDataNascimento: TextView
    private lateinit var textCidadeUf: TextView
    private lateinit var buttonLogout: Button
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var tipoSanguineo: String

    @SuppressLint("MissingInflatedId")
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

        fotoPerfil = findViewById(R.id.fotoPerfil)
        textUserId = findViewById(R.id.textUserId)
        textNomeUsuario = findViewById(R.id.textNomeUsuario)
        textTipoSanguineo = findViewById(R.id.textTipoSanguineo)
        textDataNascimento = findViewById(R.id.textDataNascimento)
        textCidadeUf = findViewById(R.id.textCidadeUf)
        buttonLogout = findViewById<Button>(R.id.btnSair)
        tipoSanguineo = textTipoSanguineo.toString()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    if (this::class != ReceptoresActivity::class) {
                        val intent = Intent(this, ReceptoresActivity::class.java)
                        intent.putExtra(
                            "TIPO_SANGUINEO",
                            tipoSanguineo
                        )
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_donation -> {
                    if (this::class != DoeAgoraActivity::class) {
                        val intent = Intent(this, DoeAgoraActivity::class.java)
                        intent.putExtra(
                            "TIPO_SANGUINEO",
                            tipoSanguineo
                        )
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
                        intent.putExtra("cidadeUf", textCidadeUf.text.toString())
                        startActivity(intent)
                    }
                    true
                }

                else -> false
            }
        }

        buttonLogout.setOnClickListener(this)
        carregarDadosUsuario()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnSair -> logout()
        }
    }

    private fun logout() {
        auth.signOut()
        Toast.makeText(this, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun carregarDadosUsuario() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Usuarios").child(userId)

            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userId = snapshot.key
                        val idUsuarioSplit = userId?.split(" ")
                        val codigoUsuario = idUsuarioSplit?.get(0)?.substring(0,10)
                        val nome = snapshot.child("nome").getValue(String::class.java)
                        tipoSanguineo =
                            snapshot.child("tipoSanguineo").getValue(String::class.java)
                                .toString()
                        val dataNascimento =
                            snapshot.child("dataNascimento").getValue(String::class.java)
                        val cidadeUf = snapshot.child("cidadeUf").getValue(String::class.java)
                        val fotoPerfilUrl =
                            snapshot.child("fotoPerfil").getValue(String::class.java)

                        textUserId.text = "Id: " + codigoUsuario
                        textNomeUsuario.text = "Nome: " + nome
                        textTipoSanguineo.text = "Fator RH: " + tipoSanguineo
                        textDataNascimento.text = "Idade: " + calcularIdade(dataNascimento.toString())
                        textCidadeUf.text = "Cidade/UF: " + cidadeUf

                        Glide.with(this@UsuariosActivity)
                            .load(fotoPerfilUrl)
                            .into(fotoPerfil)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@UsuariosActivity,
                        "Erro ao carregar dados: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun calcularIdade(dataNascimento: String): String {
        return try {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthDate: Date = format.parse(dataNascimento) ?: return "N/A"
            val currentCalendar = Calendar.getInstance()
            val birthCalendar = Calendar.getInstance().apply { time = birthDate }
            var age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

            if (currentCalendar.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
                (currentCalendar.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) &&
                        currentCalendar.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))
            ) {
                age--
            }

            "$age anos."
        } catch (e: Exception) {
            "N/A"
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.selectedItemId = when (javaClass) {
            ReceptoresActivity::class.java -> R.id.nav_home
            AgendamentoActivity::class.java -> R.id.nav_donation
            UsuariosActivity::class.java -> R.id.nav_user
            LocalDoacaoActivity::class.java -> R.id.nav_location
            else -> R.id.nav_home
        }
    }
}
