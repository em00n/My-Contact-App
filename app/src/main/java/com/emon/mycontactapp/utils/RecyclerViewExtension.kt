package com.emon.mycontactapp.utils

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun<viewHolder,T:RecyclerView.Adapter<viewHolder>> Activity.setUpGridRecyclerView(view: RecyclerView,viewAdapter:T,column:Int){
    view.layoutManager = GridLayoutManager(this,column)
    view.adapter = viewAdapter
}

fun<viewHolder,T:RecyclerView.Adapter<viewHolder>> Activity.setUpHorizontalReverseRecyclerView(view: RecyclerView,viewAdapter:T){
    view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    view.adapter = viewAdapter
}

fun<viewHolder,T:RecyclerView.Adapter<viewHolder>> Activity.setUpVerticalRecyclerView(view: RecyclerView,viewAdapter:T){
    view.layoutManager = LinearLayoutManager(this)
    view.adapter = viewAdapter
}

fun<viewHolder,T:RecyclerView.Adapter<viewHolder>> Context.setUpVerticalRecyclerView(view: RecyclerView,viewAdapter:T){
    view.layoutManager = LinearLayoutManager(this)
    view.adapter = viewAdapter
}
