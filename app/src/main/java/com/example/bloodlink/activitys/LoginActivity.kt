package com.example.bloodlink.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bloodlink.MainActivity
import com.example.bloodlink.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById<EditText>(R.id.emailUserLogin)
        passwordEditText = findViewById<EditText>(R.id.senhaUserLogin)

        val buttonLogin: Button = findViewById(R.id.botaoLogin)
        val buttonCadastrar: Button = findViewById(R.id.botaoCadastrar)
        val botaoVoltar: ImageView = findViewById(R.id.botaoVoltar)

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
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Login realizado com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Direciona para a UsuariosActivity após o login
                                startActivity(Intent(this, UsuariosActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Erro ao fazer login: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Por favor, insira e-mail e senha.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            R.id.botaoCadastrar -> {
                val cadastro = Intent(this, CadastroActivity::class.java)
                startActivity(cadastro)
            }
        }
    }
}
