package com.emon.mycontactapp.base

import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * A generic RecyclerView adapter that uses Data Binding & DiffUtil.
 *
 * @param <T> Type of the items in the list
 * @param <V> The type of the ViewDataBinding
</V></T> */
 abstract class BaseListAdapter<T,V : ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
 ) : ListAdapter<T, ListAdapterViewHolder<V>>(diffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapterViewHolder<V> {
        val binding = createBinding(parent)
        return ListAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListAdapterViewHolder<V>, position: Int) {

        bind(holder.binding, getItem(position), position)
    }

    protected abstract fun createBinding(parent: ViewGroup): V
    protected abstract fun bind(binding: V, item: T, position: Int)
}

/**
 * A generic ViewHolder that works with a [ViewBinding].
 * @param <T> The type of the ViewDataBinding.
</T> */
class ListAdapterViewHolder<out T : ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)

