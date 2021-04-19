package com.example.todo_android

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_android.Adapters.TaskAdapter
import com.example.todo_android.Models.Task
import com.example.todo_android.Utils.DBHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), TaskAdapter.OnItemClickListener {
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var addBtn: FloatingActionButton
    private lateinit var dbHelper: DBHelper
    private lateinit var taskList: ArrayList<Task>

    override fun onDeleteBtnClick(task: Task) {
        deleteConfirmation(task)
    }

    override fun onItemClick(position: Int) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar

        actionBar!!.title = "Your own TODO List"

        dbHelper = DBHelper(this)
        getTaskList()
        addBtn = findViewById(R.id.addBtn)
        addBtn.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

    }



    override fun onResume() {
        super.onResume()
        getTaskList()
        taskAdapter.notifyDataSetChanged()
    }

    private fun getTaskList() {
        taskList = dbHelper.allNotes
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskList,this)
        tasksRecyclerView.adapter = taskAdapter
    }

    private fun isChecked(task: Task){
        dbHelper.updateStatus(Task(status = 0))
    }

    private fun isNotChecked(task: Task){
        dbHelper.updateStatus(Task(status = 1))
    }

    private fun deleteConfirmation(task: Task) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirm Delete")
        alertDialog.setMessage("Are you sure you want to delete this?")
        alertDialog.setIcon(R.drawable.ic_baseline_delete_forever_24)
        alertDialog.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
            dbHelper!!.deleteTask(task)
            getTaskList()
        })

        alertDialog.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel() //Cancel the dialog
        })
        alertDialog.show()
    }
}