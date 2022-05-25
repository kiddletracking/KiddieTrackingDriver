package com.wadachirebandi.kiddietrackingdriver.daos

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LocationDao {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    val locationCollection =
        db.collection("Driver").document(auth.uid!!).collection("Location")
            .document("ap9IhGYNgDtqUkw8nus1")
    val locationCollection2 = db.collection("Driver").document(auth.uid!!)
        .collection("Location")
        .document("72AJHIPdt2cVaFDQ2V5T")
}