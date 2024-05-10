package com.example.task_management_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task_management_app.TaskListAdapter.TaskViewHolder

class TaskListAdapter(private val viewModel: TaskViewModel) : ListAdapter<Task, TaskViewHolder>(WORDS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.task, current.description, current.priority, current.deadline, viewModel)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView1: TextView = itemView.findViewById(R.id.textView1)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)


        fun bind(taskName: String?, description: String?, priority: Int, deadline: String, viewModel: TaskViewModel) {
            textView1.text = "Task: $taskName\nDescription: $description\nPriority $priority\nDeadline $deadline"
            btnDelete.setOnClickListener {
                if (taskName != null) {
                    viewModel.delete(taskName)
                }
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
