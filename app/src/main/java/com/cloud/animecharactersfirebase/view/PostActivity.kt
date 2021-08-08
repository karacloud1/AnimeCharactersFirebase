package com.cloud.animecharactersfirebase.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloud.animecharactersfirebase.R
import com.cloud.animecharactersfirebase.adapter.PostRecyclerAdapter
import com.cloud.animecharactersfirebase.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    var postList = ArrayList<Post>()
    private lateinit var recyclerViewAdapter : PostRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        getPosts()
        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = PostRecyclerAdapter(postList)
        recyclerView.adapter = recyclerViewAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPosts() {
        database.collection("Post").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error!=null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (value!=null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        postList.clear()
                        for (document in documents){
                            val nickname = document.get("nickname") as String
                            val email = document.get("email") as String
                            val comment = document.get("comment") as String
                            val imageUrl = document.get("downloadUrl") as String
                            val downloadedPost = Post(nickname,email,comment,imageUrl)
                            postList.add(downloadedPost)

                        }
                        recyclerViewAdapter.notifyDataSetChanged()

                    }

                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_options,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share){
            val intent = Intent(this,ShareActivity::class.java)
            startActivity(intent)
        }
        else if(item.itemId == R.id.exit){
            auth.signOut()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}