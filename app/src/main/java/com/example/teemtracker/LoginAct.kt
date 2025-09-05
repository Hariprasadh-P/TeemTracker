package com.example.teemtracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginAct : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(b: Bundle?) {
        super.onCreate(b); setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) { go() }
//        go()
        val e = findViewById<EditText>(R.id.etE)
        val p = findViewById<EditText>(R.id.etP)
        findViewById<Button>(R.id.bIn).setOnClickListener {
            val em = e.text.toString().trim(); val pw = p.text.toString().trim()
            if (em.isEmpty() || pw.isEmpty()) return@setOnClickListener
            auth.signInWithEmailAndPassword(em, pw).addOnCompleteListener {
                if (it.isSuccessful) go() else toast(it.exception?.localizedMessage)
            }
        }
        findViewById<Button>(R.id.bUp).setOnClickListener {
            val em = e.text.toString().trim(); val pw = p.text.toString().trim()
            if (em.isEmpty() || pw.isEmpty()) return@setOnClickListener
            auth.createUserWithEmailAndPassword(em, pw).addOnCompleteListener {
                if (it.isSuccessful) go() else toast(it.exception?.localizedMessage)
            }
        }
    }

    private fun go() {
        startActivity(Intent(this, MainAct::class.java));
        finish()
    }
    private fun toast(msg: String?) { Toast.makeText(this, msg ?: "Error", Toast.LENGTH_SHORT).show() }
}
