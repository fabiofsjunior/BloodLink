package com.example.bloodlink.activitys

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bloodlink.MainActivity
import com.example.bloodlink.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream

class CadastroActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private var tipoSanguineo: String? = null
    private val IMAGE_PICK_CODE = 1000
    private lateinit var imageBtnFoto: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        // Inicializa Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Referenciar campos do layout
        val botaoVoltar = findViewById<ImageView>(R.id.btnVoltar)
        botaoVoltar.setOnClickListener(this)

        val emailUsuario = findViewById<EditText>(R.id.emailUsuario)
        val senhaUsuario = findViewById<EditText>(R.id.senhaUsuario)
        val nomeUsuario = findViewById<EditText>(R.id.nomeUsuario)
        val dataNascimento = findViewById<EditText>(R.id.etDataNascimento)
        val celularUsuario = findViewById<EditText>(R.id.celularUsuario)

        val spinnerTipoSanguineo: Spinner = findViewById(R.id.spinnerTipoSangue)
        val spinnerCidadeUf: Spinner = findViewById(R.id.spinnerCidadeUf)

        // Preencher spinner com tipos sanguíneos
        val tiposSanguineos = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, tiposSanguineos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoSanguineo.adapter = adapter

        // Preencher spinner com cidades
        val cidadesMetropolitanaRecife = arrayOf(
            "Recife - PE", "Olinda - PE", "Jaboatão dos Guararapes - PE",
            "Paulista - PE", "Camaragibe - PE", "São Lourenço da Mata - PE",
            "Abreu e Lima - PE", "Igarassu - PE", "Cabo de Santo Agostinho - PE", "Ipojuca - PE"
        )
        val adapterCidadeUf = ArrayAdapter(this, R.layout.spinner_item, cidadesMetropolitanaRecife)
        adapterCidadeUf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCidadeUf.adapter = adapterCidadeUf

        // Configurando clickListeners
        val buttonCadastrar: Button = findViewById(R.id.botaoCadastro)
        buttonCadastrar.setOnClickListener {
            val email = emailUsuario.text.toString()
            val senha = senhaUsuario.text.toString()
            val nome = nomeUsuario.text.toString()
            val dataNasc = dataNascimento.text.toString()
            val celular = celularUsuario.text.toString()
            val tipoSangue = spinnerTipoSanguineo.selectedItem.toString()
            val cidadeUf = spinnerCidadeUf.selectedItem.toString()

            if (email.isNotEmpty() && senha.isNotEmpty() && nome.isNotEmpty() &&
                dataNasc.isNotEmpty() && celular.isNotEmpty()
            ) {

                cadastrarUsuario(email, senha, nome, dataNasc, celular, tipoSangue, cidadeUf)
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }

        imageBtnFoto = findViewById(R.id.imageBtnFoto)
        imageBtnFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1001
                )
            } else {
                pickImageFromGallery()
            }
        }

    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imageUri: Uri? = data?.data
            imageBtnFoto.setImageURI(imageUri) // Exibe a imagem selecionada
            imageUri?.let { saveImageToStorage(it) }
        }
    }

    private fun saveImageToStorage(imageUri: Uri) {
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "user_photo.jpg")

        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        }
        Toast.makeText(this, "Imagem salva em: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
    }


    private fun cadastrarUsuario(
        email: String,
        senha: String,
        nome: String,
        dataNasc: String,
        celular: String,
        tipoSangue: String,
        cidadeUf: String
    ) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    salvarDadosNoDatabase(userId, nome, dataNasc, celular, tipoSangue, cidadeUf)
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                    // Redirecionar após o cadastro
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Erro ao realizar o cadastro: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun salvarDadosNoDatabase(
        userId: String?,
        nome: String,
        dataNasc: String,
        celular: String,
        tipoSangue: String,
        cidadeUf: String
    ) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Usuarios")

        val usuarioMap = mapOf(
            "nome" to nome,
            "dataNascimento" to dataNasc,
            "celular" to celular,
            "tipoSanguineo" to tipoSangue,
            "cidadeUf" to cidadeUf
        )

        userId?.let {
            reference.child(it).setValue(usuarioMap).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Cadastro", "Dados salvos no Firebase Realtime Database.")
                } else {
                    Log.d("Cadastro", "Erro ao salvar os dados: ${task.exception?.message}")
                }
            }
        }


    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnVoltar -> {
                val homeMain = Intent(this, MainActivity::class.java)
                startActivity(homeMain)
            }
        }
    }
}
