package com.cloud.animecharactersfirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloud.animecharactersfirebase.R
import com.cloud.animecharactersfirebase.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*

class PostRecyclerAdapter(var postList : ArrayList<Post>) : RecyclerView.Adapter<PostRecyclerAdapter.PostHolder>() {
    class PostHolder(itemview : View) : RecyclerView.ViewHolder(itemview){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        holder.itemView.recycler_row_userNick.text = postList[position].userNick
        holder.itemView.recycler_row_comment.text = postList[position].comment
        Picasso.get().load(postList[position].imageUrl).into(holder.itemView.recycler_row_imageview)

    }

    override fun getItemCount(): Int {
        return postList.size
    }
}