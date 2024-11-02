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

        // Inicializa as TextViews
        nomeTextView = findViewById(R.id.textViewNome)
        fatorSanguineoTextView = findViewById(R.id.textViewFatorSanguineo)
        cidadeUfTextView = findViewById(R.id.textViewCidadeUf)
        dataNascimentoTextView = findViewById(R.id.textViewDataNascimento)
        motivoDoacaoTextView = findViewById(R.id.textViewMotivoDoacao)
        fotoImageView = findViewById(R.id.imageViewFoto)

        receptores = mutableListOf()
        receptorAdapter = ReceptorAdapter(receptores)


        // Recebendo os dados passados
        val receptorNome = intent.getStringExtra("NOME")
        val receptorFatorSanguineo = intent.getStringExtra("TIPO_SANGUINEO")
        val receptorCidadeUf = intent.getStringExtra("CIDADE_UF")
        val receptorDataNascimento = intent.getStringExtra("DATA_NASCIMENTO")
        val receptorMotivoDoacao = intent.getStringExtra("MOTIVO_DOACAO")
        val receptorFotoUrl = intent.getStringExtra("FOTO_URL") // Se estiver usando imagem

        // Exibindo os dados nas TextViews
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


        val userTipoSanguineo = intent.getStringExtra("TIPO_SANGUINEO").toString()
        buscarCompatibilidade(userTipoSanguineo)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCancelar -> {
                val intent = Intent(this, ReceptoresActivity::class.java)
                startActivity(intent)
            }

            R.id.btnConfirmar -> {
                val intent = Intent(this, LocalDoacaoActivity::class.java)
                startActivity(intent)
            }
        }
    }

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

    private fun buscarCompatibilidade(fatorSanguineoUsuario: String) {
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
                val usuarios = intent.getStringExtra("usuarios") ?: ""

                for (receptor in receptores) {
                    if (receptor.fatorSanguineoRH == fatorSanguineoUsuario) {
                        Log.d("Encontrado", "Receptor encontrado: ${receptor.nome}")
                        nomeTextView.text = receptor.nome
                        cidadeUfTextView.text = receptor.cidadeUF
                        fatorSanguineoTextView.text = receptor.fatorSanguineoRH
                        dataNascimentoTextView.text =
                            calcularIdade(receptor.dataNascimento.toString())
                        motivoDoacaoTextView.text = receptor.motivoDoacao


                        receptor.fotoUrl?.let { fotoUrl ->
                            Glide.with(this@DoeAgoraActivity)
                                .load(fotoUrl)
                                .placeholder(R.drawable.icone_foto)
                                .error(R.drawable.icone_foto)
                                .into(fotoImageView)
                        }

                        break
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                Log.d("Deu ERROR", "LASCOU")
            }

        })

    }

}

