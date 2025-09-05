package com.example.teemtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TmAd(private var itms: MutableList<Tm>, private val onClk: (Tm) -> Unit)
    : RecyclerView.Adapter<TmAd.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tv: TextView = v.findViewById(R.id.tvN)
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int): VH =
        VH(LayoutInflater.from(p.context).inflate(R.layout.row_team, p, false))

    override fun getItemCount() = itms.size

    override fun onBindViewHolder(h: VH, i: Int) {
        val tm = itms[i]
        h.tv.text = tm.name
        h.itemView.setOnClickListener { onClk(tm) }
    }

    fun setData(n: List<Tm>) { itms.clear(); itms.addAll(n); notifyDataSetChanged() }
}
