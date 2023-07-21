package com.omtorney.snapcase.data.remote.database

data class FirestoreUser(
    val id: String? = null,
    val name: String? = null,
    val cases: String? = null
) {
    fun toMap(): Map<String, String?> {
        return mapOf("name" to name, "cases" to cases)
    }
}
