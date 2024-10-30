package com.example.bloodlink.activitys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bloodlink.MainActivity
import com.example.bloodlink.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CadastroActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private var tipoSanguineo: String? = null
    private val IMAGE_PICK_CODE = 1000

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(this, "Permissão de acesso à galeria negada", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private lateinit var buttonImagemUploadedParaPefil: ImageButton
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val botaoVoltar = findViewById<ImageView>(R.id.btnVoltar)
        botaoVoltar.setOnClickListener(this)

        val emailUsuario = findViewById<EditText>(R.id.emailUsuario)
        val senhaUsuario = findViewById<EditText>(R.id.senhaUsuario)
        val nomeUsuario = findViewById<EditText>(R.id.nomeUsuario)
        val dataNascimento = findViewById<EditText>(R.id.etDataNascimento)
        val celularUsuario = findViewById<EditText>(R.id.celularUsuario)
        val spinnerTipoSanguineo: Spinner = findViewById(R.id.spinnerTipoSangue)
        val spinnerCidadeUf: Spinner = findViewById(R.id.spinnerCidadeUf)
        buttonImagemUploadedParaPefil = findViewById(R.id.imageBtnFotoPerfil)

        val tiposSanguineos = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, tiposSanguineos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoSanguineo.adapter = adapter

        val cidadesMetropolitanaRecife = arrayOf(
            "Recife - PE", "Olinda - PE", "Jaboatão dos Guararapes - PE",
            "Paulista - PE", "Camaragibe - PE", "São Lourenço da Mata - PE",
            "Abreu e Lima - PE", "Igarassu - PE", "Cabo de Santo Agostinho - PE", "Ipojuca - PE"
        )
        val adapterCidadeUf = ArrayAdapter(this, R.layout.spinner_item, cidadesMetropolitanaRecife)
        adapterCidadeUf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCidadeUf.adapter = adapterCidadeUf

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

        buttonImagemUploadedParaPefil.setOnClickListener {
            checkAndRequestPermission()
        }
    }

    private fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }

            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imageUri = data?.data
            imageUri?.let {
                selectedImageUri = it
                setProfileImage(it)
            }
        }
    }

    private fun setProfileImage(imageUri: Uri) {
        try {
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            buttonImagemUploadedParaPefil.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao carregar a imagem.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToStorage(imageUri: Uri, onSuccess: (String) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef = storageReference.child("usuarios/$userId/perfil.jpg")
        storageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }.addOnFailureListener {
                    Toast.makeText(this, "Falha ao obter link da imagem", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Falha ao fazer upload: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun cadastrarUsuario(
        email: String, senha: String, nome: String, dataNasc: String,
        celular: String, tipoSangue: String, cidadeUf: String
    ) {
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (selectedImageUri != null) {
                    saveImageToStorage(selectedImageUri!!) { fotoPerfilUrl ->
                        salvarDadosNoDatabase(
                            userId,
                            nome,
                            dataNasc,
                            celular,
                            tipoSangue,
                            cidadeUf,
                            fotoPerfilUrl
                        )
                    }
                } else {
                    salvarDadosNoDatabase(userId, nome, dataNasc, celular, tipoSangue, cidadeUf, "")
                }
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Erro ao cadastrar: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun salvarDadosNoDatabase(
        userId: String?, nome: String, dataNasc: String, celular: String,
        tipoSangue: String, cidadeUf: String, fotoPerfilUrl: String
    ) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Usuarios")

        val usuarioMap = mapOf(
            "nome" to nome, "dataNascimento" to dataNasc, "celular" to celular,
            "tipoSanguineo" to tipoSangue, "cidadeUf" to cidadeUf, "fotoPerfil" to fotoPerfilUrl
        )

        userId?.let {
            reference.child(it).setValue(usuarioMap).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Cadastro", "Dados salvos com sucesso no Database.")
                } else {
                    Log.d("Cadastro", "Erro ao salvar dados: ${task.exception?.message}")
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
