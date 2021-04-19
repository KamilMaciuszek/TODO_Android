package com.example.todo_android

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.todo_android.Adapters.TaskAdapter
import com.example.todo_android.Models.Task
import com.example.todo_android.Utils.DBHelper
import kotlin.collections.ArrayList

class AddTaskActivity : AppCompatActivity(), TaskAdapter.OnItemClickListener {
    private lateinit var dbHelper: DBHelper
    private lateinit var taskList: ArrayList<Task>
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var confirmBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adding_task_layout)
        dbHelper = DBHelper(this)

        val actionBar = supportActionBar

        actionBar!!.title = "Add new Task"

        actionBar.setDisplayHomeAsUpEnabled(true)
        confirmBtn = findViewById(R.id.confirmButton)
        confirmBtn.setOnClickListener {
            confirmChanges(null)
        }
    }



    private fun confirmChanges(task: Task?){
        val etTitle = findViewById<EditText>(R.id.AddTitle)
        val etDescription = findViewById<EditText>(R.id.AddDescription)
        val titleText = etTitle.text.toString()
        val descriptionText = etDescription.text.toString()
        if (TextUtils.isEmpty(titleText)){
            Toast.makeText(this, "Enter the title of the task!", Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(descriptionText)){
            Toast.makeText(this, "Enter the description of the task!", Toast.LENGTH_SHORT).show()
        }
        else {
            addTask(
                Task(
                    taskTitle = titleText,
                    taskDescription = descriptionText
                )
            )
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDeleteBtnClick(task: Task) {
    }

    override fun onItemClick(position: Int) {
    }

    private fun addTask(task: Task) {
        taskList = dbHelper!!.allNotes
        val id = dbHelper!!.insertTask(task)
        val new = dbHelper!!.getTask(id)
        if (new != null) {
            taskList.add(0, new)
            taskAdapter = TaskAdapter(taskList, this)
            taskAdapter!!.notifyDataSetChanged()
        }
    }


}