package com.example.todo_android.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_android.Models.Task
import com.example.todo_android.R
import com.google.android.material.chip.Chip


class TaskAdapter(private val tasks: ArrayList<Task>, private val listener: OnItemClickListener) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        val task: CheckBox
        val chip: Chip
        val button: ImageButton
        val textView: TextView
        init {
            task = itemView.findViewById(R.id.isDoneCheckBox)
            chip = itemView.findViewById(R.id.calendarChip)
            button = itemView.findViewById(R.id.deleteButton)
            textView = itemView.findViewById(R.id.DescriptionTV)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position!=RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) : ViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.tasks_cardview_layout, viewGroup,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = tasks[position]
        viewHolder.itemView.apply {
            viewHolder.task.text = item.taskTitle
            if(item.status==0) viewHolder.task.isChecked = false
            if(item.status!=0) viewHolder.task.isChecked = true
            viewHolder.chip.text = item.timestamp
            viewHolder.textView.text = item.taskDescription
        }

        val task: Task = tasks[position]
        viewHolder.itemView.findViewById<ImageButton>(R.id.deleteButton).setOnClickListener(View.OnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                listener.onDeleteBtnClick(task)
            }
        })

    }


    override fun getItemCount(): Int {
        return tasks.size
    }


    interface OnItemClickListener{
        fun onDeleteBtnClick(task:Task)
        fun onItemClick(position: Int)
    }
}


