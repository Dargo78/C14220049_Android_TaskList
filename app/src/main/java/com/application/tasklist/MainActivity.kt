package com.application.tasklist

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainActivity : AppCompatActivity() {
    private var arTasks = arrayListOf<Task>()
    private lateinit var rvTask: RecyclerView
    private val REQUEST_CODE_NEW_TASK = 1

    private val addTaskLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val task = result.data!!.getParcelableExtra<Task>("task", Task::class.java)
                if (task != null) {
                    arTasks.add(task)
                    Log.i("APLIKASI", arTasks.toString())
                    showTasks()
                }
            }
        }

    fun showEditDialog(task: Task, pos: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.edit_task_dialog, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.editTaskName)
        val dateInput = dialogView.findViewById<EditText>(R.id.editTaskDate)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.editTaskDescription)

        nameInput.setText(task.name)
        dateInput.setText(task.date)
        descriptionInput.setText(task.description)

        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                // Update the task in the list
                val updatedTask = task.copy(
                    name = nameInput.text.toString(),
                    date = dateInput.text.toString(),
                    description = descriptionInput.text.toString()
                )
                arTasks[pos] = updatedTask
                showTasks()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun showTasks() {
        rvTask.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)

        val adapterTask = AdapterRecView(arTasks)
        rvTask.adapter = adapterTask

        adapterTask.setOnItemClickCallback(object : AdapterRecView.OnItemClickCallback {
            override fun delTask(pos: Int) {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("HAPUS DATA")
                    .setMessage("Apakah benar data " + arTasks[pos].name + " akan dihapus ?")
                    .setPositiveButton(
                        "HAPUS",
                        DialogInterface.OnClickListener { dialog, which ->
                            arTasks.removeAt(pos)
                            showTasks()
                        }
                    )
                    .setNegativeButton(
                        "Batal",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(
                                this@MainActivity,
                                "Data batal dihapus",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    ).show()
            }

            override fun editTask(pos: Int) {
                showEditDialog(arTasks[pos], pos)
            }

            override fun startTask(pos: Int) {
                if (arTasks[pos].status == 0)
                    arTasks[pos] = arTasks[pos].copy(status = 1)
                else if (arTasks[pos].status == 1)
                    arTasks[pos] = arTasks[pos].copy(status = 2)

                showTasks()
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addTaskButton = findViewById<Button>(R.id.btnAdd)

        addTaskButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            addTaskLauncher.launch(intent)
        }

        rvTask = findViewById<RecyclerView>(R.id.rvTasks)

        if (arTasks.size > 0) {
            showTasks()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val task = intent.getParcelableExtra<Task>("task")
            if (task != null) {
                arTasks.add(task)
            }
        }
    }
}