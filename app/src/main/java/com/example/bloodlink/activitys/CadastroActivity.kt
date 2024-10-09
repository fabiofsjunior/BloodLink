package com.example.bloodlink.activitys

import android.content.Intent
import android.os.Bundle
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

        findViewById<Button>(R.id.btnCadastrar).setOnClickListener {
            val username = findViewById<TextView>(R.id.etUsername).text.toString()
            val password = findViewById<TextView>(R.id.etPassword).text.toString()
            val dataNascimento = findViewById<TextView>(R.id.etDataNascimento).text.toString()
            val email = findViewById<TextView>(R.id.etEmail).text.toString()
            val celular = findViewById<TextView>(R.id.etCelular).text.toString()
            val endereco = findViewById<TextView>(R.id.etEndereco).text.toString()

            if (username.isEmpty() || password.isEmpty() || tipoSanguineo == null || dataNascimento.isEmpty() ||
                email.isEmpty() || celular.isEmpty() || endereco.isEmpty()
            ) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Aqui você pode salvar os dados ou processar o cadastro.
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }
        var tapToLogin: ImageView = findViewById(R.id.botaoVoltar)
        tapToLogin.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        val login = Intent(this, LoginActivity::class.java)
        startActivity(login)
    }

}
