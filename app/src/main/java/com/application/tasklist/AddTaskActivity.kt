package com.application.tasklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameInput = findViewById<EditText>(R.id.editName)
        val descriptionInput = findViewById<EditText>(R.id.editDescription)
        val dateInput = findViewById<EditText>(R.id.editDate)

        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val task = Task(nameInput.text.toString(), dateInput.text.toString(), descriptionInput.text.toString(), 0)
            val intent = Intent().apply {
                putExtra("task", task)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

    }
}