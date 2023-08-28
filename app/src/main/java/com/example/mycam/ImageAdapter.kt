package com.example.mycam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageAdapter(private val imageList: MutableList<ImageItem>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val timestampText: TextView = itemView.findViewById(R.id.timestampText)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = imageList[position]
        holder.imageView.setImageBitmap(currentItem.bitmap)
        holder.timestampText.text = getFormattedTimestamp(currentItem.timestamp)
        holder.favoriteIcon.setImageResource(if (currentItem.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border)

        holder.itemView.setOnClickListener {
            // Handle opening individual images
        }

        holder.favoriteIcon.setOnClickListener {
            currentItem.isFavorite = !currentItem.isFavorite
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = imageList.size

    private fun getFormattedTimestamp(timestamp: Long): String {
        // Format the timestamp as needed
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
    }
}
