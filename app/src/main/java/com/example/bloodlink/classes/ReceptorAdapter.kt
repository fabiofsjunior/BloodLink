package com.example.bloodlink.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bloodlink.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReceptorAdapter(private val receptores: List<Receptor>) :
    RecyclerView.Adapter<ReceptorAdapter.ReceptorViewHolder>() {

    inner class ReceptorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.itemNome)
        val fatorSanguineo: TextView = view.findViewById(R.id.itemFatorSanguineo)
        val cidadeUf: TextView = view.findViewById(R.id.itemCidadeUf)
        val dataNascimento: TextView = view.findViewById(R.id.itemDataNascimento)
        val foto: ImageView = view.findViewById(R.id.itemFoto)
        val motivoDoacao: TextView = view.findViewById(R.id.itemMotivoDoacao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceptorViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_receptor, parent, false)
        return ReceptorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceptorViewHolder, position: Int) {
        val receptor = receptores[position]
        holder.nome.text = receptor.nome
        holder.fatorSanguineo.text = receptor.fatorSanguineoRH
        holder.cidadeUf.text = receptor.cidadeUF
        holder.dataNascimento.text = calcularIdade(receptor.dataNascimento)
        holder.motivoDoacao.text = receptor.motivoDoacao

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
        return try {
            // Definindo o formato da data
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Corrigido para dd/MM/yyyy
            // Convertendo a string para um objeto Date
            val birthDate: Date = format.parse(dataNascimento) ?: return "N/A"

            // Obtendo a data atual
            val currentCalendar = Calendar.getInstance()

            // Calculando a idade
            val birthCalendar = Calendar.getInstance().apply {
                time = birthDate
            }
            var age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

            // Ajustando a idade caso o aniversário ainda não tenha ocorrido no ano atual
            if (currentCalendar.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
                (currentCalendar.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) &&
                        currentCalendar.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))
            ) {
                age--
            }

            age.toString()+ " anos."
        } catch (e: Exception) {
            "N/A"
        }
    }
}
