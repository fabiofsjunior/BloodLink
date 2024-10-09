package com.example.bloodlink.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.bloodlink.MainActivity
import com.example.bloodlink.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var buttonLogin: Button = findViewById(R.id.botaoLogin)
        var buttonCadastrar: Button = findViewById(R.id.botaoCadastrar)
        var botaoVoltar: ImageView = findViewById(R.id.botaoVoltar)

        buttonLogin.setOnClickListener(this)
        buttonCadastrar.setOnClickListener(this)
        botaoVoltar.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.botaoVoltar -> {
                val homeMain = Intent(this, MainActivity::class.java)
                startActivity(homeMain)
            }

            R.id.botaoLogin -> {
            }

            R.id.botaoCadastrar -> {
                val cadastro = Intent(this, CadastroActivity::class.java)
                startActivity(cadastro)
            }


        }
    }
}
