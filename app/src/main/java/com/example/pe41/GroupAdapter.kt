package com.example.pe41

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class GroupAdapter(private val context: Context, private val groups: List<Group>) :
    RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.group_item_layout, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.bind(group)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent =  Intent(context, GroupActivity::class.java)
            intent.putExtra("groupId", group.groupId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textGroupName: TextView = itemView.findViewById(R.id.text_group_name)

        fun bind(group: Group) {
            textGroupName.text = group.groupName
        }
    }
}