package com.wadachirebandi.kiddietrackingadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.wadachirebandi.kiddietrackingadmin.databinding.ChildItemViewBinding
import com.wadachirebandi.kiddietrackingadmin.models.User
import com.wadachirebandi.kiddietrackingadmin.notification.NotificationData
import com.wadachirebandi.kiddietrackingadmin.notification.PushNotification
import com.wadachirebandi.kiddietrackingadmin.notification.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChildrenAdapter(options: FirestoreRecyclerOptions<User>, private val view: View) :
    FirestoreRecyclerAdapter<User, ChildrenAdapter.ChildrenViewHolder>(options) {

    class ChildrenViewHolder(val binding: ChildItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildrenViewHolder {
        return ChildrenViewHolder(
            ChildItemViewBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChildrenViewHolder, position: Int, model: User) {
        holder.binding.apply {
            Glide.with(view).load(model.image).circleCrop().into(childImage)
            childName.text = model.name
            checkInChildButton.setOnClickListener {
                if (model.token == "") {
                    Toast.makeText(
                        view.context,
                        "This child parents have not Login into the parental app",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    model.token?.let { it1 ->
                        PushNotification(
                            NotificationData("Child Check-In", "Your Child have been check-In safely"),
                            it1
                        ).also {
                            sendNotification(it)
                        }
                        Toast.makeText(
                            view.context,
                            "Make as Check-In successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            absentChildButton.setOnClickListener {
                if (model.token == "") {
                    Toast.makeText(
                        view.context,
                        "This child parents have not Login into the parental app",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    model.token?.let { it1 ->
                        PushNotification(
                            NotificationData("Child Absent", "Your Child is absent today"),
                            it1
                        ).also {
                            sendNotification(it)
                        }
                        Toast.makeText(
                            view.context,
                            "Make absent successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitInstance.api.postNotification(notification)
        }

}