package com.example.bloodlink

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bloodlink.activitys.LoginActivity
import com.example.bloodlink.activitys.ReceptoresActivity
import com.example.bloodlink.classes.Receptor
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var textView: TextView // Declare o TextView


    override fun onCreate(savedInstanceState: Bundle?) {

        // Inicializar o Firebase Database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("mensagem")

        // Escrever dados no Realtime Database
        myRef.setValue("Olá, Firebase!")

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Alterar este ID para o ID do seu TextView no layout
        textView = findViewById(R.id.tapToLogin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Animação de aparecer
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        // Animação de desaparecer
        val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        // Configurar animações para serem executadas em sequência
        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Código a ser executado quando a animação começa (opcional)
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Inicia a animação de desaparecer quando a animação de aparecer termina
                textView.startAnimation(fadeOutAnimation)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Não é necessário implementar
            }
        })

        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Código a ser executado quando a animação começa (opcional)
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Inicia a animação de aparecer quando a animação de desaparecer termina
                textView.startAnimation(fadeInAnimation)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Não é necessário implementar
            }
        })

        // Inicia a animação de aparecer
        textView.startAnimation(fadeInAnimation)

        var tapToLogin: TextView = findViewById(R.id.tapToLogin)
        tapToLogin.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        val login = Intent(this, ReceptoresActivity::class.java)
        startActivity(login)
    }


}
