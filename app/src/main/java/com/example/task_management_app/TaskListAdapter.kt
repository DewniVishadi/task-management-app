package com.example.task_management_app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task_management_app.TaskListAdapter.TaskViewHolder

class TaskListAdapter(private val viewModel: TaskViewModel, private val context: Context) : ListAdapter<Task, TaskViewHolder>(WORDS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.task, current.description, current.priority, current.deadline, viewModel, context)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView1: TextView = itemView.findViewById(R.id.textView1)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        private val newTaskActivityRequestCode = 2


        fun bind(taskName: String?, description: String?, priority: Int, deadline: String, viewModel: TaskViewModel, context: Context) {
            textView1.text = "Task: $taskName\nDescription: $description\nPriority: $priority\nDeadline: $deadline"
            btnDelete.setOnClickListener {
                if (taskName != null) {
                    viewModel.delete(taskName)
                }
            }
            textView1.setOnClickListener {
                val intent = Intent(context, NewTaskActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("TASK_NAME", taskName)
                intent.putExtra("DESCRIPTION", description)
                intent.putExtra("PRIORITY", priority)
                intent.putExtra("DEADLINE", deadline)
                intent.putExtra("NEED_TO_EDIT", true)
                println("INTENT $intent")
                context.startActivity(intent)
            }
        }

        companion object {
            fun create(parent: ViewGroup): TaskViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return TaskViewHolder(view)
            }
        }
    }

    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.task == newItem.task
            }
        }
    }
}
