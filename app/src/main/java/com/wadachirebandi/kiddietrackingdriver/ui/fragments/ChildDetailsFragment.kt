package com.wadachirebandi.kiddietrackingdriver.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wadachirebandi.kiddietrackingdriver.adapter.ChildrenAdapter
import com.wadachirebandi.kiddietrackingdriver.daos.UserDao
import com.wadachirebandi.kiddietrackingdriver.databinding.FragmentChildDetailsBinding
import com.wadachirebandi.kiddietrackingdriver.models.User

class ChildDetailsFragment : Fragment() {

    private var _binding: FragmentChildDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: ChildrenAdapter

    private val userDao = UserDao()

    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildDetailsBinding.inflate(layoutInflater, container, false)

        setAdapter()

        return binding.root
    }

    private fun setAdapter() {
        val query = userDao.userCollection.whereEqualTo("driverUid", auth.uid!!)

        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java)
                .build()

        adapter = ChildrenAdapter(recyclerViewOptions, binding.root)

        binding.rvChildren.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}