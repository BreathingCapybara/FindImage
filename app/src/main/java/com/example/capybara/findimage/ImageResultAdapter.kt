package com.example.capybara.findimage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.capybara.findimage.databinding.ItemImageBinding
import com.example.capybara.findimage.network.repo.ImageRepo
import com.example.capybara.findimage.network.repo.ImageResultRepo

class ImageResultAdapter(private val imageResultRepo: ImageResultRepo) :
    RecyclerView.Adapter<ImageResultAdapter.ImageViewHolder>() {

    inner class ImageViewHolder : RecyclerView.ViewHolder {
        private lateinit var binding: ItemImageBinding

        constructor(view: View) : super(view){
            // todo is_end 값에 따라 view를 gone/visible로 변경
//            imageResultRepo.meta.is_end
        }

        constructor(binding: ItemImageBinding) : super(binding.root) {
            this.binding = binding
        }

        fun bind(objects: Any) {
            val imageRepo = objects as ImageRepo
            //todo fresco를 이용하여 이미지 출력

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return when (viewType) {
            ITEM_VIEW_BODY -> {
                val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ImageViewHolder(binding.root)
            }
            else -> { // ITEM_VIEW_FOOTER
                //todo is_end 가 true 이면 가리기(height = 0으로 지정 또는 gone으로 설정
                val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ImageViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (position > 0) {
            imageResultRepo.documents?.let { imageRepos ->
                holder.bind(imageRepos[position - 1])
            }
        }
    }

    override fun getItemCount(): Int {
        return imageResultRepo.documents?.size ?: 0.plus(1)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> ITEM_VIEW_FOOTER
            else -> ITEM_VIEW_BODY
        }
    }

    companion object {
        private const val ITEM_VIEW_BODY = 0
        private const val ITEM_VIEW_FOOTER = 1
    }
}
