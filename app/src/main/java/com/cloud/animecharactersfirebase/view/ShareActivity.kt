package com.cloud.animecharactersfirebase.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cloud.animecharactersfirebase.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_share.*
import java.util.*

class ShareActivity : AppCompatActivity() {
    var chosenImage : Uri? = null
    var chosenBitmap : Bitmap? = null
    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    private var nickname = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        sharedPreferences = this.getSharedPreferences("com.cloud.animecharactersfirebase",
            Context.MODE_PRIVATE)

        nickname = sharedPreferences.getString("userNickname","null").toString()
    }

    fun choseImage(view :View){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }
        else{
            val mediaIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(mediaIntent,2)
        }
    }

    fun share(view : View){
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)
        if (chosenImage!=null){
            imageReference.putFile(chosenImage!!).addOnSuccessListener {
                val uploadedImageRef = FirebaseStorage.getInstance().reference.child("images").child(imageName)
                uploadedImageRef.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    val currentUserEmail = auth.currentUser!!.email.toString()
                    val comment = textComment.text.toString()
                    val date = Timestamp.now()
                    val postHashMap = hashMapOf<String, Any>()
                    postHashMap.put("downloadUrl",downloadUrl)
                    postHashMap.put("email",currentUserEmail)
                    postHashMap.put("nickname",nickname)
                    postHashMap.put("comment",comment)
                    postHashMap.put("date",date)

                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this,"Post paylaşıldı.",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==1){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val mediaIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(mediaIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==2 && resultCode == Activity.RESULT_OK && data!=null){
            chosenImage = data.data
            if (chosenImage!=null){
                if (Build.VERSION.SDK_INT>=28){
                    val source = ImageDecoder.createSource(this.contentResolver,chosenImage!!)
                    chosenBitmap = ImageDecoder.decodeBitmap(source)
                    imageView_share.setImageBitmap(chosenBitmap)
                }else{
                    chosenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,chosenImage)
                    imageView_share.setImageBitmap(chosenBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}