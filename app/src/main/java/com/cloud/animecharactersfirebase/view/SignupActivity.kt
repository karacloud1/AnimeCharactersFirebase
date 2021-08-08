package com.cloud.animecharactersfirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cloud.animecharactersfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    fun registerDone(view: View){

        val nick = textNickname_reg.text.toString()
        val email = textEmail_reg.text.toString()
        val password = textPassword_reg.text.toString()
        if (nick == "" || email == "" || password == ""){
            Toast.makeText(this,"Tüm alanları doldurunuz!",Toast.LENGTH_LONG).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    val userHashMap = hashMapOf<String,Any>()
                    userHashMap.put("nick",nick)
                    userHashMap.put("email",email)
                    database.collection("User").add(userHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this,"Kayıt başarılı.",Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }.addOnFailureListener { it ->
                        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }

    }
}