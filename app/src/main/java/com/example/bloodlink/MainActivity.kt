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
import com.example.bloodlink.activitys.UsuariosActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var textView: TextView

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.tapToLogin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configs animação
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                textView.startAnimation(fadeOutAnimation)
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })

        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                textView.startAnimation(fadeInAnimation)
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })

        textView.startAnimation(fadeInAnimation)

        var tapToLogin: TextView = findViewById(R.id.tapToLogin)
        tapToLogin.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        if (currentUser != null) {
            val email = currentUser.email
            val logado = Intent(this, UsuariosActivity::class.java)
            startActivity(logado)
        } else {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }
    }

}
