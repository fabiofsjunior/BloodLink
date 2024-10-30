package com.example.bloodlink.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bloodlink.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AgendamentoActivity : AppCompatActivity(), View.OnClickListener {

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
        dataNascimentoTextView.text = calcularIdade(receptorDataNascimento.toString())
        motivoDoacaoTextView.text = receptorMotivoDoacao


        // Se você quiser carregar a imagem usando Glide
        Glide.with(this)
            .load(receptorFotoUrl)
            .placeholder(R.drawable.icone_foto) // Imagem de placeholder
            .error(R.drawable.icone_foto) // Imagem em caso de erro
            .into(fotoImageView)

        var botaoCancelarAgendamento: ImageButton = findViewById(R.id.btnCancelar)
        var botaoConfirmarAgendamento: ImageButton = findViewById(R.id.btnConfirmar)
        botaoCancelarAgendamento.setOnClickListener(this)
        botaoConfirmarAgendamento.setOnClickListener(this)

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
