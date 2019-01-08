package com.example.capybara.findimage

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.capybara.findimage.databinding.ItemImageBinding
import com.example.capybara.findimage.network.repo.ImageRepo
import com.example.capybara.findimage.network.repo.ImageResultRepo
import kotlinx.android.synthetic.main.item_image.view.*

class ImageResultAdapter(
    private val imageResultRepo: ImageResultRepo, private val searchNextPage: View.OnClickListener
) :
    RecyclerView.Adapter<ImageResultAdapter.ImageViewHolder>() {

    var page = 1

    inner class ImageViewHolder
        (view: View, private val viewType: Int) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private var imageRepo: ImageRepo? = null

        fun bind(objects: Any?) {
            when (viewType) {
                ITEM_VIEW_BODY -> {
                    objects?.let {
                        imageRepo = objects as ImageRepo
                        imageRepo?.image_url?.let { url ->
                            itemView.image.setImageURI(Uri.parse(url))
                            itemView.image.setOnClickListener(this)
                        }
                    }
                }
                ITEM_VIEW_FOOTER -> {
                    if (imageResultRepo.meta.is_end) itemView.image.visibility = View.GONE
                    else itemView.image.setOnClickListener(searchNextPage)
                }
            }
        }

        override fun onClick(v: View?) {
            v?.let {
                imageRepo?.let { imageRepo ->
                    val intent = Intent(v.context, DetailActivity::class.java)
                    intent.putExtra("a", imageRepo.image_url)
                    v.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding.root, viewType)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (position < itemCount - 1) {
            imageResultRepo.documents?.let { imageRepos -> holder.bind(imageRepos[position]) }
        } else holder.bind(null)

    }

    override fun getItemCount(): Int {
        return (imageResultRepo.documents?.size ?: 0).plus(1)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> ITEM_VIEW_FOOTER
            else -> ITEM_VIEW_BODY
        }
    }

    fun addImageResult(imageResultRepo: ImageResultRepo) {
        this.imageResultRepo.meta = imageResultRepo.meta
        imageResultRepo.documents?.let { docs ->
            docs.forEach { doc -> this.imageResultRepo.documents?.add(doc) }
        }
        page++
    }

    companion object {
        private const val ITEM_VIEW_BODY = 0
        private const val ITEM_VIEW_FOOTER = 1
    }
}

