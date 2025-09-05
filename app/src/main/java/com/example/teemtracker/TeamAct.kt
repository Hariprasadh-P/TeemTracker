package com.example.teemtracker

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class TeamAct : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var tid: String
    private lateinit var tn: String

    private val mems = mutableListOf<Mem>()
    private lateinit var sp: Spinner
    private lateinit var adTsk: TskAd

    override fun onCreate(b: Bundle?) {
        super.onCreate(b); setContentView(R.layout.activity_team)
        tid = intent.getStringExtra("tid") ?: ""; tn = intent.getStringExtra("tn") ?: ""
        findViewById<TextView>(R.id.tvTn).text = "Team: $tn"
        sp = findViewById(R.id.spMem)

        val rv = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvTsk)
        rv.layoutManager = LinearLayoutManager(this)
        adTsk = TskAd(mutableListOf()) { delTsk(it) }
        rv.adapter = adTsk

        findViewById<Button>(R.id.bAddMem).setOnClickListener { addMem() }
        findViewById<Button>(R.id.bAddTsk).setOnClickListener { addTsk() }
    }

    override fun onStart() {
        super.onStart()
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("teams").document(tid)
            .collection("members").addSnapshotListener { qs, _ ->
                mems.clear(); val names = mutableListOf<String>()
                qs?.forEach { d -> val m = Mem(d.id, d.getString("name") ?: ""); mems.add(m); names.add(m.name) }
                sp.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, names)
            }
        db.collection("users").document(uid).collection("teams").document(tid)
            .collection("tasks").orderBy("ts").addSnapshotListener { qs, _ ->
                val li = qs?.documents?.map { d ->
                    Tsk(d.id, d.getString("txt") ?: "", d.getString("memId") ?: "", d.getString("memName") ?: "")
                } ?: emptyList()
                adTsk.setData(li)
            }
    }

    private fun addMem() {
        val et = EditText(this)
        AlertDialog.Builder(this).setTitle("Member name").setView(et)
            .setPositiveButton("Save") { _, _ ->
                val s = et.text.toString().trim(); val uid = auth.currentUser?.uid ?: return@setPositiveButton
                if (s.isEmpty()) return@setPositiveButton
                db.collection("users").document(uid).collection("teams").document(tid).collection("members").add(mapOf("name" to s))
            }.setNegativeButton("Cancel", null).show()
    }

    private fun addTsk() {
        val uid = auth.currentUser?.uid ?: return
        if (mems.isEmpty()) { Toast.makeText(this, "Add a member first", Toast.LENGTH_SHORT).show(); return }
        val i = sp.selectedItemPosition
        val m = mems[i]
        val et = findViewById<EditText>(R.id.etTsk)
        val txt = et.text.toString().trim(); if (txt.isEmpty()) return
        val data = mapOf("txt" to txt, "memId" to m.id, "memName" to m.name, "ts" to FieldValue.serverTimestamp())
        db.collection("users").document(uid).collection("teams").document(tid).collection("tasks").add(data)
        et.setText("")
    }

    private fun delTsk(id: String) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("teams").document(tid).collection("tasks").document(id).delete()
    }
}
