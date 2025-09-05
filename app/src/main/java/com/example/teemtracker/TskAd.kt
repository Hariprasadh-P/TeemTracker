package com.example.teemtracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class TskAd(private var itms: MutableList<Tsk>, private val onChk: (String) -> Unit)
    : RecyclerView.Adapter<TskAd.VH>() {

    inner class VH(val cb: CheckBox) : RecyclerView.ViewHolder(cb)

    override fun onCreateViewHolder(p: ViewGroup, t: Int): VH =
        VH(LayoutInflater.from(p.context).inflate(R.layout.row_task, p, false) as CheckBox)

    override fun getItemCount() = itms.size

    override fun onBindViewHolder(h: VH, i: Int) {
        val x = itms[i]
        h.cb.setOnCheckedChangeListener(null)
        h.cb.isChecked = false
        h.cb.text = "${x.txt} — ${x.memName}"
        h.cb.setOnCheckedChangeListener { _, b ->
            if (b) onChk(x.id)
        }
    }

    fun setData(n: List<Tsk>) { itms.clear(); itms.addAll(n); notifyDataSetChanged() }
}
