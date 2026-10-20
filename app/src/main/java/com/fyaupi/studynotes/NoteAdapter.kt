package com.fyaupi.studynotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private var notes: List<Note>,
    private val onClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    fun submit(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.cardTitle)
        val subtitle: TextView = itemView.findViewById(R.id.cardSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]

        holder.title.text = if (note.important) "${note.title} (!)" else note.title
        holder.subtitle.text = note.category

        holder.itemView.setOnClickListener { onClick(note) }
    }

    override fun getItemCount(): Int = notes.size
}