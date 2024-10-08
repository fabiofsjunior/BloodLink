package com.example.bloodlink

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bloodlink.activitys.CadastroActivity
import com.example.bloodlink.activitys.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



//       Chama a tela de Login
//       val login = Intent(this, LoginActivity::class.java)
//        startActivity(login)


//       Chama a tela de Cadastro
//       val cadastro = Intent(this, CadastroActivity::class.java)
//        startActivity(cadastro)
}
}