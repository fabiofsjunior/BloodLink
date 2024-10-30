package com.example.bloodlink.activitys

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bloodlink.R

class AgendamentoActivity : AppCompatActivity() {

    private lateinit var nomeTextView: TextView
    private lateinit var fatorSanguineoTextView: TextView
    private lateinit var cidadeUfTextView: TextView
    private lateinit var dataNascimentoTextView: TextView
    private lateinit var motivoDoacaoTextView: TextView
    private lateinit var fotoImageView: ImageView // Caso queira exibir a imagem

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agendamento)

        // Configurando padding para a tela
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
        fotoImageView = findViewById(R.id.imageViewFoto) // Caso queira exibir a imagem

        // Recebendo os dados passados
        val receptorNome = intent.getStringExtra("NOME")
        val receptorFatorSanguineo = intent.getStringExtra("FACTOR_SANGUINEO")
        val receptorCidadeUf = intent.getStringExtra("CIDADE_UF")
        val receptorDataNascimento = intent.getStringExtra("DATA_NASCIMENTO")
        val receptorMotivoDoacao = intent.getStringExtra("MOTIVO_DOACAO")
        val receptorFotoUrl = intent.getStringExtra("FOTO_URL") // Se estiver usando imagem

        // Exibindo os dados nas TextViews
        nomeTextView.text = receptorNome
        fatorSanguineoTextView.text = receptorFatorSanguineo
        cidadeUfTextView.text = receptorCidadeUf
        dataNascimentoTextView.text = receptorDataNascimento
        motivoDoacaoTextView.text = receptorMotivoDoacao

        // Se você quiser carregar a imagem usando Glide
        Glide.with(this)
            .load(receptorFotoUrl)
            .placeholder(R.drawable.icone_foto) // Imagem de placeholder
            .error(R.drawable.icone_foto) // Imagem em caso de erro
            .into(fotoImageView)
    }
}
