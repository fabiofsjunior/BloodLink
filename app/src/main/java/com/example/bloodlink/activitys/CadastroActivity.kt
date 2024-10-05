package com.example.bloodlink.activitys

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bloodlink.R

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        findViewById<Button>(R.id.btnCadastrar).setOnClickListener {
            val username = findViewById<TextView>(R.id.etUsername).toString()
            val password = findViewById<TextView>(R.id.etPassword).toString()
            val tipoSangue = findViewById<TextView>(R.id.etTipoSangue).toString()
            val dataNascimento = findViewById<TextView>(R.id.etDataNascimento).toString()
            val email = findViewById<TextView>(R.id.etEmail).toString()
            val celular = findViewById<TextView>(R.id.etCelular).toString()
            val endereco = findViewById<TextView>(R.id.etEndereco).toString()

            if (username.isEmpty() || password.isEmpty() || tipoSangue.isEmpty() || dataNascimento.isEmpty() ||
                email.isEmpty() || celular.isEmpty() || endereco.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                // Aqui você pode salvar os dados ou processar o cadastro.
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}