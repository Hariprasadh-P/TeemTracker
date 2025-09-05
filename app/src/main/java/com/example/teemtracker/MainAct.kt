package com.example.teemtracker
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class MainAct : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var ad: TmAd
    override fun onCreate(b: Bundle?) {
        super.onCreate(b); setContentView(R.layout.activity_main)
        val rv = findViewById<RecyclerView>(R.id.rvTm)
        rv.layoutManager = LinearLayoutManager(this)
        ad = TmAd(mutableListOf()) {
            val i = Intent(this, TeamAct::class.java)
            i.putExtra("tid", it.id); i.putExtra("tn", it.name); startActivity(i)
        }
        rv.adapter = ad
        findViewById<Button>(R.id.bAddTm).setOnClickListener { addTm() }
    }
    override fun onStart() {
        super.onStart()
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("teams")
            .addSnapshotListener { qs, _ ->
                val li = qs?.documents?.map { d -> Tm(d.id, d.getString("name") ?: "") } ?: emptyList()
                ad.setData(li)
            }
    }
    private fun addTm() {
        val et = android.widget.EditText(this)
        AlertDialog.Builder(this).setTitle("Team name").setView(et)
            .setPositiveButton("Save") { _, _ ->
                val s = et.text.toString().trim()
                val uid = auth.currentUser?.uid ?: return@setPositiveButton
                if (s.isEmpty()) return@setPositiveButton

                // Save team to Firestore
                db.collection("users").document(uid).collection("teams")
                    .add(mapOf("name" to s))
                    .addOnSuccessListener { docRef ->
                        val i = Intent(this, TeamAct::class.java)
                        i.putExtra("tid", docRef.id)   // send teamId
                        i.putExtra("tn", s)            // send teamName
                        startActivity(i)
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
