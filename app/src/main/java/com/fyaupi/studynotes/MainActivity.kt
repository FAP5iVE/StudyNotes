package com.fyaupi.studynotes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyaupi.studynotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "StudyNotesLife"

    private val notes = mutableListOf<Note>()
    private lateinit var adapter: NoteAdapter

    private val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val reply = result.data?.getStringExtra("reply")
            binding.statusText.text = reply
        }
    }

    override fun onStart()   { super.onStart();   Log.i(TAG, "onStart") }
    override fun onResume()  { super.onResume();  Log.i(TAG, "onResume") }
    override fun onPause()   { super.onPause();   Log.i(TAG, "onPause") }
    override fun onStop()
    { super.onStop()
        Log.i(TAG, "onStop") }
    override fun onRestart() { super.onRestart(); Log.i(TAG, "onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.i(TAG, "onDestroy") }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putString("statusText", binding.statusText.text.toString())
        outState.putBoolean("important", binding.importantSwitch.isChecked)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")
        binding.statusText.text = savedInstanceState.getString("statusText")
        binding.importantSwitch.isChecked = savedInstanceState.getBoolean("important")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NoteAdapter(notes) { note ->
            val intent = Intent(this, NoteDetailActivity::class.java).apply {
                putExtra("noteTitle", note.title)
                putExtra("noteBody", note.body)
                putExtra("noteCategory", note.category)
                putExtra("noteImportant", note.important)
            }
            detailLauncher.launch(intent)
        }
        binding.noteList.layoutManager = LinearLayoutManager(this)
        binding.noteList.adapter = adapter

        if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let { shared ->
                binding.bodyInput.setText(shared)
            }
        }

        val defaultColor = binding.statusText.currentTextColor

        binding.remindCheck.isEnabled = false

        binding.importantSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.statusText.text = if (isChecked) "Marked important" else "No longer important"
            binding.statusText.setTextColor(if (isChecked) Color.RED else defaultColor)
        }

        binding.categoryGroup.setOnCheckedChangeListener { group, checkedId ->
            val chosen = group.findViewById<RadioButton>(checkedId)
            binding.statusText.text = "Category: ${chosen.text}"

            val allowed = checkedId != R.id.catStudy
            binding.remindCheck.isEnabled = allowed
            if (!allowed) binding.remindCheck.isChecked = false
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleInput.text.toString().trim()

            if (title.isEmpty()) {
                binding.statusText.text = getString(R.string.no_title)
                return@setOnClickListener
            }

            val categoryId = binding.categoryGroup.checkedRadioButtonId
            if (categoryId == -1) {
                binding.statusText.text = "Pick a category first"
                return@setOnClickListener
            }
            val category = binding.root.findViewById<RadioButton>(categoryId).text.toString()

            val important = binding.importantSwitch.isChecked
            val remind = binding.remindCheck.isChecked

            val marker = if (important) getString(R.string.important_marker) else ""
            binding.statusText.text = getString(R.string.summary_format, title, category, marker)

            if (remind) {
                Log.i("StudyNotes", "Reminder wanted for: $title")
            }

            notes.add(0, Note(title, binding.bodyInput.text.toString(), category, important))
            adapter.notifyItemInserted(0)
            binding.noteList.scrollToPosition(0)
            binding.titleInput.text.clear()
            binding.bodyInput.text.clear()
        }

        binding.clearButton.setOnClickListener {
            binding.titleInput.text.clear()
            binding.bodyInput.text.clear()
            binding.statusText.text = ""
        }
        Log.i(TAG, "onCreate")
    }
}