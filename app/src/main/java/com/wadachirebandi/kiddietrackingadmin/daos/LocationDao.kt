package com.wadachirebandi.kiddietrackingadmin.daos

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LocationDao {
    private val db = Firebase.firestore
    val locationCollection = db.collection("Location").document("La361pxI3tekRV89qMLw")
    val locationCollection2 = db.collection("Location").document("p9tK77lGXvYuOrSdX9gy")
}