package com.wadachirebandi.kiddietrackingadmin.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserDao {

    private val db = Firebase.firestore
    val userCollection = db.collection("Users")
}