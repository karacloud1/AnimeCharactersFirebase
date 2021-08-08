package com.cloud.animecharactersfirebase.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cloud.animecharactersfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private var nickname = ""
    private lateinit var database : FirebaseFirestore
    lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        sharedPreferences = this.getSharedPreferences("com.cloud.animecharactersfirebase",
            Context.MODE_PRIVATE)

        nickname = sharedPreferences.getString("userNickname","null").toString()

        val currentUser = auth.currentUser
        if (currentUser!=null){
            Toast.makeText(this,"Hoşgeldin $nickname",Toast.LENGTH_LONG).show()
            val intent = Intent(this,PostActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun login(view: View){
        if (textEmail.text.toString() == "" || textPassword.text.toString() == ""){
            Toast.makeText(this,"Kullanıcı adı ve şifre alanlarını doldurun!",Toast.LENGTH_LONG).show()
        }
        else{
            auth.signInWithEmailAndPassword(textEmail.text.toString(),textPassword.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    val currentUserEmail = auth.currentUser?.email.toString()
                    database.collection("User").whereEqualTo("email",currentUserEmail).addSnapshotListener { value, error ->
                        if (error!=null){
                            Toast.makeText(this,error.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                        else{
                            if(value!=null){
                                val documents = value.documents
                                for (document in documents){
                                    nickname = document.get("nick") as String
                                }
                                sharedPreferences.edit().putString("userNickname",nickname).apply()
                                Toast.makeText(this,"Hoşgeldin $nickname",Toast.LENGTH_LONG).show()
                                val intent = Intent(this,PostActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }



    }
    fun openRegisterActivity(view:View){
        val intent = Intent(this,SignupActivity::class.java)
        startActivity(intent)
    }
}