package com.example.bloodlink.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bloodlink.R

class ReceptorAdapter(private val receptores: List<Receptor>) : RecyclerView.Adapter<ReceptorAdapter.ReceptorViewHolder>() {

    inner class ReceptorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.itemNome)
        val fatorSanguineo: TextView = view.findViewById(R.id.itemFatorSanguineo)
        val cidadeUf: TextView = view.findViewById(R.id.itemCidadeUf)
        val idade: TextView = view.findViewById(R.id.itemIdade)
        val foto: ImageView = view.findViewById(R.id.itemFoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceptorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_receptor, parent, false)
        return ReceptorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceptorViewHolder, position: Int) {
        val receptor = receptores[position]
        holder.nome.text = receptor.nome
        holder.fatorSanguineo.text = receptor.fatorSanguineoRH
        holder.cidadeUf.text = receptor.cidadeUF
        holder.idade.text = calcularIdade(receptor.dataNascimento)

        // Carregar imagem com Glide
        Glide.with(holder.foto.context)
            .load(receptor.fotoUrl)
            .placeholder(R.drawable.icone_foto) // Imagem de placeholder enquanto carrega
            .error(R.drawable.icone_foto) // Imagem em caso de erro no carregamento
            .into(holder.foto)
    }

    override fun getItemCount(): Int = receptores.size

    // Função para calcular idade com base na data de nascimento (exemplo)
    private fun calcularIdade(dataNascimento: String): String {
        // Implementar cálculo baseado na data fornecida ou retornar "N/A" caso falhe
        return "N/A"
    }
}
