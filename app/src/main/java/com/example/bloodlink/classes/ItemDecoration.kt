package com.example.bloodlink.classes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodlink.R

class ItemDecoration(
    context: Context,
    private val spacing: Int,
    borderWidth: Float
) : RecyclerView.ItemDecoration() {

    // Cor da borda a partir do colors.xml
    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.offwhite) // Cor da borda personalizada
        strokeWidth = borderWidth // Espessura da borda
        style = Paint.Style.STROKE
    }

    // Cor de fundo a partir do colors.xml
    private val backgroundPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.offwhite) // Fundo personalizado
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            // Calcula o retângulo de fundo e borda
            val left = child.left.toFloat() + spacing / 2
            val top = child.top.toFloat() + spacing / 2
            val right = child.right.toFloat() - spacing / 2
            val bottom = child.bottom.toFloat() - spacing / 2

            // Desenha o fundo personalizado
            canvas.drawRect(left, top, right, bottom, backgroundPaint)

            // Desenha a borda
            canvas.drawRect(left, top, right, bottom, paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // Define o espaçamento ao redor de cada item no RecyclerView
        outRect.left = spacing
        outRect.right = spacing
        outRect.top = spacing
        outRect.bottom = spacing
    }
}
