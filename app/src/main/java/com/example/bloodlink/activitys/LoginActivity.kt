package com.example.bloodlink.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bloodlink.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var buttonLogin: Button = findViewById(R.id.botaoLogin)
        var buttonCadastrar: Button = findViewById(R.id.botaoCadastrar)

        buttonLogin.setOnClickListener(this)
        buttonCadastrar.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.botaoLogin -> {
                Log.d("LoginActivity", "Botão de login clicado") // Exibe no Logcat
            }
            R.id.botaoCadastrar -> {
                Log.d("LoginActivity", "Botão de cadastro clicado") // Exibe no Logcat
                val cadastro = Intent(this, CadastroActivity::class.java)
                startActivity(cadastro)
            }
        }
    }
}
