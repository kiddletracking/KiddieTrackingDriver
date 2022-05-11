package com.wadachirebandi.kiddietrackingadmin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.wadachirebandi.kiddietrackingadmin.adapter.ChildrenAdapter
import com.wadachirebandi.kiddietrackingadmin.daos.UserDao
import com.wadachirebandi.kiddietrackingadmin.databinding.FragmentChildDetailsBinding
import com.wadachirebandi.kiddietrackingadmin.models.User

class ChildDetailsFragment : Fragment() {

    private var _binding: FragmentChildDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: ChildrenAdapter

    private val userDao = UserDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildDetailsBinding.inflate(layoutInflater, container, false)

        setAdapter()

        return binding.root
    }

    private fun setAdapter() {
        val query = userDao.userCollection

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