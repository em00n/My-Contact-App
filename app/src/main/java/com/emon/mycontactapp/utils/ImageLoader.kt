package com.emon.mycontactapp.utils

import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


fun CircleImageView.loadImage(url : String) {
    Glide.with(this.context)
        .load(url)
        .into(this)
        .clearOnDetach()
}

fun CircleImageView.loadImageWithoutCache(url: String) {
    val request = ImageRequest.Builder(context)
        .data(url)
        .diskCachePolicy(CachePolicy.DISABLED) // Disable disk caching
        .memoryCachePolicy(CachePolicy.DISABLED) // Disable memory caching
        .target(this)
        .build()
    context.imageLoader.enqueue(request)
}
