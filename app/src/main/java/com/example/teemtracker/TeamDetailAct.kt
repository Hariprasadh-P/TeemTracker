package com.example.teemtracker

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class TeamDetailAct : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var rv: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: MemberAdapter
    private var members = mutableListOf<Member>()
    private var teamId: String? = null
    private var teamName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        db = FirebaseFirestore.getInstance()
        rv = findViewById(R.id.rvMembers)
        fab = findViewById(R.id.fabAddMember)

        adapter = MemberAdapter(members)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // get team info from intent
        teamId = intent.getStringExtra("teamId")
        teamName = intent.getStringExtra("teamName")
        title = "Team: $teamName"

        loadMembers()

        fab.setOnClickListener {
            showAddDialog()
        }
    }

    private fun loadMembers() {
        teamId?.let { id ->
            db.collection("teams").document(id).collection("members")
                .get()
                .addOnSuccessListener { result ->
                    members.clear()
                    for (doc in result) {
                        val m = Member(
                            doc.getString("id") ?: "",
                            doc.getString("name") ?: "",
                            doc.getString("role") ?: ""
                        )
                        members.add(m)
                    }
                    adapter.notifyDataSetChanged()
                }
        }
    }

    private fun showAddDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_add_member, null)
        val etName = view.findViewById<EditText>(R.id.etMemberName)
        val etRole = view.findViewById<EditText>(R.id.etMemberRole)

        AlertDialog.Builder(this)
            .setTitle("Add Member")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val name = etName.text.toString().trim()
                val role = etRole.text.toString().trim()
                if (name.isNotEmpty()) {
                    addMember(name, role)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addMember(name: String, role: String) {
        val id = db.collection("teams").document(teamId!!).collection("members").document().id
        val m = hashMapOf(
            "id" to id,
            "name" to name,
            "role" to role
        )
        db.collection("teams").document(teamId!!).collection("members")
            .document(id).set(m)
            .addOnSuccessListener {
                Toast.makeText(this, "Member added", Toast.LENGTH_SHORT).show()
                loadMembers()
            }
    }
}
