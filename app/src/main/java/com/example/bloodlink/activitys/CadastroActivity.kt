package com.example.bloodlink.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bloodlink.R

class CadastroActivity : AppCompatActivity(), View.OnClickListener {
    private var tipoSanguineo: String? =
        null  // Variável para armazenar o tipo sanguíneo selecionado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        var tapToLogin: ImageView = findViewById(R.id.botaoVoltar)
        tapToLogin.setOnClickListener(this)

        // Referenciando o Spinner e preenchendo com tipos sanguíneos
        val spinnerTipoSanguineo: Spinner = findViewById(R.id.spinnerTipoSangue)

        // Criando uma lista de tipos sanguíneos
        val tiposSanguineos = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

        // Criando um ArrayAdapter com layout personalizado
        val adapter = ArrayAdapter(this, R.layout.spinner_item, tiposSanguineos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Definindo o adapter no Spinner
        spinnerTipoSanguineo.adapter = adapter

        // Configurando um listener para capturar a seleção
        spinnerTipoSanguineo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                tipoSanguineo = tiposSanguineos[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                tipoSanguineo = null // Se nada for selecionado
            }
        }

// Referenciando o Spinner e preenchendo com as opções de cidades da região metropolitana do Recife
        val spinnerCidadeUf: Spinner = findViewById(R.id.spinnerCidadeUf)

// Criando uma lista de cidades da região metropolitana do Recife - PE
        val cidadesMetropolitanaRecife = arrayOf(
            "Recife - PE",
            "Olinda - PE",
            "Jaboatão dos Guararapes - PE",
            "Paulista - PE",
            "Camaragibe - PE",
            "São Lourenço da Mata - PE",
            "Abreu e Lima - PE",
            "Igarassu - PE",
            "Cabo de Santo Agostinho - PE",
            "Ipojuca - PE"
        )

// Criando um ArrayAdapter com layout personalizado
        val adapterCidadeUf = ArrayAdapter(this, R.layout.spinner_item, cidadesMetropolitanaRecife)
        adapterCidadeUf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// Definindo o adapter no Spinner
        spinnerCidadeUf.adapter = adapterCidadeUf

// Configurando um listener para capturar a seleção
        spinnerCidadeUf.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Capturar a cidade selecionada
                val cidadeUfSelecionada = cidadesMetropolitanaRecife[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Lidar com o caso de nenhuma cidade selecionada, se necessário
            }
        }


        val nomeUsuario = findViewById<TextView>(R.id.nomeUsuario).text.toString()
        val emailUsuario = findViewById<TextView>(R.id.emailUsuario).text.toString()
        val password = findViewById<TextView>(R.id.senhaUsuario).text.toString()
        val fatorRH = findViewById<Spinner>(R.id.spinnerTipoSangue)
        val dataNascimento = findViewById<TextView>(R.id.etDataNascimento).text.toString()
        val celular = findViewById<TextView>(R.id.celularUsuario).text.toString()
        val cidadeUf = findViewById<Spinner>(R.id.spinnerCidadeUf).toString()



        if (nomeUsuario.isEmpty() || password.isEmpty() || tipoSanguineo == null || dataNascimento.isEmpty() ||
            emailUsuario.isEmpty() || celular.isEmpty() || cidadeUf.isEmpty()
        ) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
        } else {
            // Aqui você pode salvar os dados ou processar o cadastro.
            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
        }


        var botaoVoltar: ImageView = findViewById(R.id.botaoVoltar)
        var buttonCadastrar: Button = findViewById(R.id.botaoCadastro)
        var signWithGoogle: Button = findViewById(R.id.botaoSignInGoogle)


        botaoVoltar.setOnClickListener(this)
        signWithGoogle.setOnClickListener(this)
        buttonCadastrar.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.botaoVoltar -> {
                val login = Intent(this, LoginActivity::class.java)
                startActivity(login)
            }

            R.id.botaoCadastrar -> {
                val cadastro = Intent(this, CadastroActivity::class.java)
                Log.d("teste","Testando botão Cadastro")
                startActivity(cadastro)
            }

            R.id.botaoSignInGoogle -> {
                val cadastro = Intent(this, CadastroActivity::class.java)
                Log.d("teste","Testando botão Google")
                startActivity(cadastro)
            }

        }
    }


}
