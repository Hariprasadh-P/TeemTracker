package com.example.teemtracker
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class MemberAdapter(private val list: List<Member>) :
    RecyclerView.Adapter<MemberAdapter.VH>() {
    class VH(item: View) : RecyclerView.ViewHolder(item) {
        val tvName: TextView = item.findViewById(R.id.tvName)
        val tvRole: TextView = item.findViewById(R.id.tvRole)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return VH(v)
    }
    override fun onBindViewHolder(holder: VH, pos: Int) {
        val m = list[pos]
        holder.tvName.text = m.name
        holder.tvRole.text = m.role
    }
    override fun getItemCount(): Int = list.size
}
