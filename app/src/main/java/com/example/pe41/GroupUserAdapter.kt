package com.example.pe41

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupUserAdapter(private val context: Context, private val group: Group, private val userList: List<User>, private val userToAddList: ArrayList<User>, private val onUserClick: (Int) -> Unit) :
    RecyclerView.Adapter<GroupUserAdapter.GroupUserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupUserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.group_user_item_layout, parent, false)
        return GroupUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupUserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)

        holder.checkBox.isChecked = group.usersIds.contains(user.userId)
        //Toggle the checkbox when pressed on the item
        holder.itemLayout.setOnClickListener {
            holder.checkBox.isChecked = !holder.checkBox.isChecked
            if (holder.checkBox.isChecked){
                userToAddList.add(user)
            } else {
                val ids = userToAddList.map {it.userId}
                if (ids.contains(user.userId)){
                    var i = 0
                    while (userToAddList[i].userId != user.userId){
                        i++
                    }
                    userToAddList.removeAt(i)
                }
            }
            onUserClick(group.usersIds.size)
        }
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    inner class GroupUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.text_username)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_user)
        val itemLayout: LinearLayout = itemView.findViewById(R.id.user_item_layout)

        fun bind(user: User) {
            nameTextView.text = user.username
        }
    }
}