package com.example.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.TasksItemBinding
import android.graphics.Color

class TaskAdapter(private val tasks: List<TaskItem>, private val itemClickListener: HomePg) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    interface OnItemClickListener {
        fun onDeleteClick(taskId: String)
        fun onUpdateClick(taskId: String, title: String, description: String)
        fun onTaskStatusChange(task: TaskItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TasksItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
        holder.binding.delete.setOnClickListener {
            itemClickListener.onDeleteClick(task.taskId)
        }
        holder.binding.edit.setOnClickListener {
            itemClickListener.onUpdateClick(task.taskId, task.title, task.description)
        }
        holder.binding.taskCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            task.isCompleted = isChecked
            itemClickListener.onTaskStatusChange(task)
            holder.updateStatus(task.isCompleted)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }


    class TaskViewHolder(val binding: TasksItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskItem) {
            binding.taskCheckBox.text = task.title
            binding.taskDescription.text = task.description
            binding.taskCheckBox.isChecked = task.isCompleted
            updateStatus(task.isCompleted)
        }

        fun updateStatus(isCompleted: Boolean) {
            if (isCompleted) {
                binding.status.text = "Completed"
                binding.status.setTextColor(Color.GREEN)
            } else {
                binding.status.text = "Not Completed"
                binding.status.setTextColor(Color.RED)
            }
        }
    }
}