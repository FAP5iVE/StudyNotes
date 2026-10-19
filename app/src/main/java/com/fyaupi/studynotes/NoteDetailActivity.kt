package com.fyaupi.studynotes

import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fyaupi.studynotes.databinding.ActivityNoteDetailBinding

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val extras = intent.extras ?: return

        val title = extras.getString("noteTitle") ?: "Untitled"
        val body = extras.getString("noteBody") ?: ""
        val category = extras.getString("noteCategory") ?: ""
        val important = extras.getBoolean("noteImportant")

        binding.detailTitle.text = if (important) "$title (!)" else title
        binding.detailBody.text = "$category\n\n$body"

        binding.doneButton.setOnClickListener {
            val data = Intent().putExtra("reply", "Note reviewed: $title")
            setResult(RESULT_OK, data)
            finish()
        }

        binding.shareButton.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, "$title\n\n$body")
            }
            startActivity(Intent.createChooser(sendIntent, "Share note via"))
        }
    }
}