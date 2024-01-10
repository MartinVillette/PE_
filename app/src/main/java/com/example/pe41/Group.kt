package com.example.pe41

class Group (
    var groupId: String? = null,
    var groupName: String? = null,
    var teacherId: String? = null,
    var usersIds: ArrayList<String> = arrayListOf(),
) {
    fun addUserToGroup(user: User){
        if (!usersIds.contains(user.userId)){
            usersIds.add(user.userId!!)
        }
    }
}