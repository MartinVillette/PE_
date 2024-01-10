package com.example.pe41

class User (
    var userId:String? = null,
    var username: String? = null,
    var name: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var teacher: Boolean = false,
    var groupIds: ArrayList<String> = arrayListOf(),
) {
    fun addGroup(groupId:String){
        if (!groupIds.contains(groupId)){
            groupIds.add(groupId)
        }
    }
}
