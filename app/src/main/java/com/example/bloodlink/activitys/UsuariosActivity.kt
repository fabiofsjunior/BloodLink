package com.example.bloodlink.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
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
    private lateinit var textNomeUsuario: TextView
    private lateinit var textTipoSanguineo: TextView
    private lateinit var textDataNascimento: TextView
    private lateinit var textCidadeUf: TextView
    private lateinit var buttonLogout: ImageButton
    private lateinit var buttonHome: Button
    private lateinit var buttonDoarSangue: Button
    private lateinit var buttonPerfil: Button

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

        // Referências para os componentes da UI
        fotoPerfil = findViewById(R.id.fotoPerfil) // Adicione este ID no seu layout
        textNomeUsuario = findViewById(R.id.textNomeUsuario) // Adicione este ID no seu layout
        textTipoSanguineo = findViewById(R.id.textTipoSanguineo) // Adicione este ID no seu layout
        textDataNascimento = findViewById(R.id.textDataNascimento) // Adicione este ID no seu layout
        textCidadeUf = findViewById(R.id.textCidadeUf) // Adicione este ID no seu layout
        buttonLogout = findViewById<ImageButton>(R.id.buttonLogout)
        buttonHome = findViewById(R.id.botaoHome)
        buttonDoarSangue = findViewById(R.id.botaoDoeSangue)
        buttonPerfil = findViewById(R.id.botaoPerfil)


        buttonLogout.setOnClickListener(this)
        buttonHome.setOnClickListener(this)
        buttonDoarSangue.setOnClickListener(this)
        buttonPerfil.setOnClickListener(this)

        carregarDadosUsuario() // Carregar os dados do usuário ao iniciar a atividade
    }

    private fun carregarDadosUsuario() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Usuarios").child(userId)

            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val nome = snapshot.child("nome").getValue(String::class.java)
                        val tipoSanguineo =
                            snapshot.child("tipoSanguineo").getValue(String::class.java)
                        val dataNascimento =
                            snapshot.child("dataNascimento").getValue(String::class.java)
                        val cidadeUf = snapshot.child("cidadeUf").getValue(String::class.java)
                        val fotoPerfilUrl =
                            snapshot.child("fotoPerfil").getValue(String::class.java)

                        textNomeUsuario.text = nome
                        textTipoSanguineo.text = tipoSanguineo
                        textDataNascimento.text = calcularIdade(dataNascimento.toString())


                        textCidadeUf.text = cidadeUf

                        // Carregar a imagem do URL usando Glide
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

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonLogout -> {
                logout()
            }

            R.id.botaoHome -> {
                val intent = Intent(this, ReceptoresActivity::class.java)
                startActivity(intent)
            }

            R.id.botaoDoeSangue -> {
                val intent = Intent(this, AgendamentoActivity::class.java)
                startActivity(intent)
            }

            R.id.botaoPerfil -> {
                val intent = Intent(this, UsuariosActivity::class.java)
                startActivity(intent)

            }
        }
    }

    private fun logout() {
        auth.signOut() // Faz o logout
        Toast.makeText(this, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza a MainActivity
    }

    // Função para calcular idade com base na data de nascimento (exemplo)
    private fun calcularIdade(dataNascimento: String): String {
        return try {
            // Definindo o formato da data
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            // Convertendo a string para um objeto Date
            val birthDate: Date = format.parse(dataNascimento) ?: return "N/A"

            // Obtendo a data atual
            val currentCalendar = Calendar.getInstance()

            // Calculando a idade
            val birthCalendar = Calendar.getInstance().apply {
                time = birthDate
            }
            var age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

            // Ajustando a idade caso o aniversário ainda não tenha ocorrido no ano atual
            if (currentCalendar.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
                (currentCalendar.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) &&
                        currentCalendar.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))
            ) {
                age--
            }

            age.toString() + " anos."
        } catch (e: Exception) {
            "N/A"
        }
    }
}
