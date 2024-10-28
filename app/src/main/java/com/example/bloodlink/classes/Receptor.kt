package com.example.bloodlink.classes

import com.google.firebase.database.PropertyName

data class Receptor(
    @get:PropertyName("Nome") @set:PropertyName("Nome")
    var nome: String = "",

    @get:PropertyName("Email") @set:PropertyName("Email")
    var email: String = "",

    @get:PropertyName("Telefone") @set:PropertyName("Telefone")
    var telefone: String = "",

    @get:PropertyName("Cidade_UF") @set:PropertyName("Cidade_UF")
    var cidadeUF: String = "",

    @get:PropertyName("Data_Nascimento") @set:PropertyName("Data_Nascimento")
    var dataNascimento: String = "",

    @get:PropertyName("Foto_Perfil") @set:PropertyName("Foto_Perfil")
    var fotoUrl: String = "",

    @get:PropertyName("Fator_Sanguíneo_RH") @set:PropertyName("Fator_Sanguíneo_RH")
    var fatorSanguineoRH: String = ""
)
