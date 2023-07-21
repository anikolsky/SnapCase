package com.omtorney.snapcase.data.remote.database

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreDatabase@Inject constructor(
    private val firestoreReference: CollectionReference
) {

    fun getFirestoreUser(userName: String): Flow<FirestoreResult<List<FirestoreUser>>> = callbackFlow {
        trySend(FirestoreResult.Loading)
        val listener = firestoreReference.whereEqualTo("name", userName).addSnapshotListener { value, error ->
            if (error != null) trySend(FirestoreResult.Error(Throwable(error.message)))
            if (value != null) {
                val users = value.map { documentSnapshot ->
                    documentSnapshot.toObject<FirestoreUser>()
                }
                trySend(FirestoreResult.Success(users))
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun createFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        callbackFlow {
            val documentID = firestoreReference.document().id
            firestoreReference.document(documentID).set(firestoreUser.copy(id = documentID))
                .addOnSuccessListener { trySend(FirestoreResult.Success("Backup created")) }
                .addOnFailureListener { trySend(FirestoreResult.Error(Throwable(it.message))) }
            awaitClose { close() }
        }

    suspend fun updateFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        callbackFlow {
            if (firestoreUser.id != null) {
                firestoreReference.document(firestoreUser.id).update(firestoreUser.toMap())
                    .addOnSuccessListener { trySend(FirestoreResult.Success("Backup updated")) }
                    .addOnFailureListener { trySend(FirestoreResult.Error(Throwable(it.message))) }
            }
            awaitClose { close() }
        }

    suspend fun deleteFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        callbackFlow {
            if (firestoreUser.id != null) {
                firestoreReference.document(firestoreUser.id).delete()
                    .addOnSuccessListener { trySend(FirestoreResult.Success("Backup deleted")) }
                    .addOnFailureListener { trySend(FirestoreResult.Error(Throwable(it.message))) }
            }
            awaitClose { close() }
        }
}