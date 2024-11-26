package com.application.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterRecView (private val listTasks: ArrayList<Task>) : RecyclerView
    .Adapter<AdapterRecView.ListViewHolder> () {
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.tvName)
        var description = itemView.findViewById<TextView>(R.id.tvDescription)
        var date = itemView.findViewById<TextView>(R.id.tvDate)

        var delButton = itemView.findViewById<Button>(R.id.btnDelete)
        var editButton = itemView.findViewById<Button>(R.id.btnEdit)
        var actionButton = itemView.findViewById<Button>(R.id.btnAction)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun delTask(pos: Int)
        fun editTask(pos: Int)
        fun startTask(pos: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback =onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_recycler, parent, false)

        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTasks.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var task = listTasks[position]

        holder.name.setText(task.name)
        holder.description.setText(task.description)
        holder.date.setText(task.date)

        if (task.status >= 1) {
            holder.editButton.visibility = View.GONE
            holder.delButton.visibility = View.GONE
            holder.actionButton.setText("Finish")
        }

        if (task.status == 2) {
            holder.actionButton.setText("Done")
        }

        holder.delButton.setOnClickListener {
            onItemClickCallback.delTask(position)
        }

        holder.editButton.setOnClickListener {
            onItemClickCallback.editTask(position)
        }

        holder.actionButton.setOnClickListener {
            onItemClickCallback.startTask(position)
        }
    }
}