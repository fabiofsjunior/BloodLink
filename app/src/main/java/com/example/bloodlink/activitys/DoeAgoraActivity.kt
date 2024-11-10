package com.example.bloodlink.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bloodlink.R
import com.example.bloodlink.classes.Receptor
import com.example.bloodlink.classes.ReceptorAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DoeAgoraActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var nomeTextView: TextView
    private lateinit var fatorSanguineoTextView: TextView
    private lateinit var cidadeUfTextView: TextView
    private lateinit var dataNascimentoTextView: TextView
    private lateinit var motivoDoacaoTextView: TextView
    private lateinit var fotoImageView: ImageView
    private lateinit var receptores: MutableList<Receptor>
    private lateinit var receptorAdapter: ReceptorAdapter
    private lateinit var userTipoSanguineo: String


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agendamento)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nomeTextView = findViewById(R.id.textViewNome)
        fatorSanguineoTextView = findViewById(R.id.textViewFatorSanguineo)
        cidadeUfTextView = findViewById(R.id.textViewCidadeUf)
        dataNascimentoTextView = findViewById(R.id.textViewDataNascimento)
        motivoDoacaoTextView = findViewById(R.id.textViewMotivoDoacao)
        fotoImageView = findViewById(R.id.imageViewFoto)

        receptores = mutableListOf()
        receptorAdapter = ReceptorAdapter(receptores)

        userTipoSanguineo = intent.getStringExtra("TIPO_SANGUINEO").toString()


        val receptorNome = intent.getStringExtra("NOME")
        val receptorFatorSanguineo = intent.getStringExtra("TIPO_SANGUINEO")
        val receptorCidadeUf = intent.getStringExtra("CIDADE_UF")
        val receptorDataNascimento = intent.getStringExtra("DATA_NASCIMENTO")
        val receptorMotivoDoacao = intent.getStringExtra("MOTIVO_DOACAO")
        val receptorFotoUrl = intent.getStringExtra("FOTO_URL")

        nomeTextView.text = receptorNome
        fatorSanguineoTextView.text = receptorFatorSanguineo
        cidadeUfTextView.text = receptorCidadeUf
        dataNascimentoTextView.text = calcularIdade(receptorDataNascimento.toString())
        motivoDoacaoTextView.text = receptorMotivoDoacao


        Glide.with(this)
            .load(receptorFotoUrl)
            .placeholder(R.drawable.icone_foto)
            .error(R.drawable.icone_foto)
            .into(fotoImageView)

        var botaoCancelarAgendamento: ImageButton = findViewById(R.id.btnCancelar)
        var botaoConfirmarAgendamento: ImageButton = findViewById(R.id.btnConfirmar)
        botaoCancelarAgendamento.setOnClickListener(this)
        botaoConfirmarAgendamento.setOnClickListener(this)


        carregarTipoSangueUsuario()

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCancelar -> {
                val intent = Intent(this, ReceptoresActivity::class.java)
                intent.putExtra(
                    "TIPO_SANGUINEO",
                    userTipoSanguineo
                )
                startActivity(intent)
            }

            R.id.btnConfirmar -> {
                val intent = Intent(this, LocalDoacaoActivity::class.java)
                intent.putExtra(
                    "TIPO_SANGUINEO",
                    userTipoSanguineo
                )
                startActivity(intent)
            }
        }
    }

    private fun calcularIdade(dataNascimento: String): String {
        return try {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthDate: Date = format.parse(dataNascimento) ?: return "N/A"
            val currentCalendar = Calendar.getInstance()
            val birthCalendar = Calendar.getInstance().apply {
                time = birthDate
            }
            var age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

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

    private fun carregarTipoSangueUsuario() {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Usuarios").child(userId)

            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val tipoSanguineo =
                            snapshot.child("tipoSanguineo").getValue(String::class.java).orEmpty()
                        val cidadeUf =
                            snapshot.child("cidadeUf").getValue(String::class.java).orEmpty()
                        val dataNascimento =
                            snapshot.child("dataNascimento").getValue(String::class.java).orEmpty()
                        val fatorSanguineoRh =
                            snapshot.child("tipoSanguineo").getValue(String::class.java).orEmpty()
                        val fotoPerfil =
                            snapshot.child("fotoPerfil").getValue(String::class.java).orEmpty()
                        val nome = snapshot.child("nome").getValue(String::class.java).orEmpty()
                        val telefone =
                            snapshot.child("celular").getValue(String::class.java).orEmpty()

                        // Exibindo os valores no log para verificação
                        Log.d("DadosUsuario", "Tipo Sanguíneo: $tipoSanguineo")
                        Log.d("DadosUsuario", "Cidade_UF: $cidadeUf")
                        Log.d("DadosUsuario", "Data de Nascimento: $dataNascimento")
                        Log.d("DadosUsuario", "Fator Sanguíneo RH: $fatorSanguineoRh")
                        Log.d("DadosUsuario", "Foto Perfil: $fotoPerfil")
                        Log.d("DadosUsuario", "Nome: $nome")
                        Log.d("DadosUsuario", "Telefone: $telefone")

                        // Chama a função buscarCompatibilidade usando o tipo sanguíneo
                        buscarCompatibilidade(tipoSanguineo)
                    } else {
                        Log.d("DadosUsuario", "Snapshot vazio para o usuário.")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@DoeAgoraActivity,
                        "Erro ao carregar dados: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Log.d("DadosUsuario", "Usuário não logado.")
        }
    }


    private fun buscarCompatibilidade(fatorSanguineoUsuario: String) {
        val database = FirebaseDatabase.getInstance().getReference("Receptores")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                receptores.clear()
                for (data in snapshot.children) {
                    val receptor = data.getValue(Receptor::class.java)
                    receptor?.let { receptores.add(it) }
                }
                receptorAdapter.notifyDataSetChanged()
                val receptoresCompatíveis =
                    receptores.filter { it.fatorSanguineoRH == fatorSanguineoUsuario }
                val receptorAleatorio = receptoresCompatíveis.randomOrNull()

                receptorAleatorio?.let { receptor ->
                    Log.d("Encontrado", "Receptor encontrado: ${receptor.nome}")
                    nomeTextView.text = receptor.nome
                    cidadeUfTextView.text = receptor.cidadeUF
                    fatorSanguineoTextView.text = receptor.fatorSanguineoRH
                    dataNascimentoTextView.text = calcularIdade(receptor.dataNascimento.toString())
                    motivoDoacaoTextView.text = receptor.motivoDoacao

                    receptor.fotoUrl?.let { fotoUrl ->
                        Glide.with(this@DoeAgoraActivity)
                            .load(fotoUrl)
                            .placeholder(R.drawable.icone_foto)
                            .error(R.drawable.icone_foto)
                            .into(fotoImageView)
                    }
                } ?: run {
                    nomeTextView.text = "Nenhum receptor compatível encontrado."
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Deu ERROR", "LASCOU")
            }

        })

    }

}

