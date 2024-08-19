package robi.codingchallenge.carousellnews.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import robi.codingchallenge.carousellnews.databinding.ItemNewsBinding
import robi.codingchallenge.library.DateTime
import robi.codingchallenge.networks.data.News

class Adapter: RecyclerView.Adapter<Adapter.ViewHolder>(){
    private val list = mutableListOf<News?>()
    var actionListener: OnActionListener? = null

    interface OnActionListener {
        fun onAction(result: News)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val m = list[position]
        if (m==null) {
            holder.binding.cardContent.visibility = View.GONE
            holder.binding.cardShimmer.visibility = View.VISIBLE
            holder.binding.cardShimmer.startShimmer()
        } else {
            holder.binding.cardShimmer.stopShimmer()
            holder.binding.cardShimmer.visibility = View.GONE
            holder.binding.cardContent.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(m.bannerUrl)
                .centerCrop()
                .into(holder.binding.imageView)
            holder.binding.tvTitle.text = m.title
            holder.binding.tvDescription.text = m.description
            holder.binding.tvTime.text = DateTime.duration(m.timeCreated.toLong())
        }
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<News>) {
        list.clear()
        if (data.isEmpty()) {
            list.addAll(mutableListOf(null, null, null, null))
        } else {
            list.addAll(data)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root)
}