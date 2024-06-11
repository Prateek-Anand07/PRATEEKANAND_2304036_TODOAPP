package com.example.todoapp

class TaskItem(
    val title: String,
    val description: String,
    val taskId: String,
    var isCompleted: Boolean = false
) {
    constructor() : this("", "", "", false)
}