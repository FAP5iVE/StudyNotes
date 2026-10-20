package com.fyaupi.studynotes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,
    val body: String,
    val category: String,
    val important: Boolean
)