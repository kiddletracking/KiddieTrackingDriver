package com.wadachirebandi.kiddietrackingadmin.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wadachirebandi.kiddietrackingadmin.daos.UserDao
import com.wadachirebandi.kiddietrackingadmin.databinding.FragmentRegisterChildBinding
import com.wadachirebandi.kiddietrackingadmin.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterChildFragment : Fragment() {

    private var _binding: FragmentRegisterChildBinding? = null

    private val binding get() = _binding!!

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var childUri: Uri? = null

    private var storageRef = Firebase.storage.reference

    val userDao = UserDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterChildBinding.inflate(layoutInflater, container, false)

        setChildImage()

        binding.createChildButton.setOnClickListener {
            uploadChildImage()
        }

        return binding.root
    }

    private fun uploadChildImage() {

        if (childUri != null && binding.parentName.text.toString() != ""
            && binding.email.text.toString() != "" &&
            binding.password.text.toString() != "" &&
            binding.childName.text.toString() != "" &&
            binding.parentsContact.text.toString() != "" &&
            binding.childClass.text.toString() != ""
        ) {
            binding.pg.visibility = View.VISIBLE
            binding.createChildButton.visibility = View.GONE
            val imageRef = storageRef
                .child("Child/ + ${childUri!!.lastPathSegment}")
            val uploadTask = imageRef.putFile(childUri!!)
            uploadTask.addOnSuccessListener {
                val url = imageRef.downloadUrl
                url.addOnSuccessListener {
                    createAccount(it)
                }
            }
            uploadTask.addOnFailureListener {
                binding.pg.visibility = View.GONE
                binding.createChildButton.visibility = View.VISIBLE
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(
                requireContext(), "Please enter all information properly", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setChildImage() {

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data.let {
                        childUri = it
                        binding.childImage.setImageURI(it)
                    }
                }
            }

        binding.childImage.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                resultLauncher.launch(it)
            }
        }
    }


    private fun createAccount(uri: Uri) {

        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result!!.user!!
                    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                        val newChild = User(
                            uid = firebaseUser.uid,
                            name = binding.childName.text.toString(),
                            image = uri.toString(),
                            classStd = binding.childClass.text.toString(),
                            parentName = binding.parentName.text.toString(),
                            parentContact = binding.parentsContact.text.toString()
                        )
                        userDao.userCollection.add(newChild).addOnSuccessListener {
                            Toast.makeText(
                                requireContext(), "Child Register successful", Toast.LENGTH_SHORT
                            ).show()
                            binding.pg.visibility = View.GONE
                            findNavController().navigateUp()
                        }.addOnFailureListener {
                            Toast.makeText(
                                requireContext(), "Registration failed please try again", Toast.LENGTH_SHORT
                            ).show()
                            binding.createChildButton.visibility = View.VISIBLE
                        }
                    }
                    val user = User(uid = firebaseUser.uid)

                } else {
                    Toast.makeText(
                        requireContext(), "Registration failed due to " +
                                task.exception.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}