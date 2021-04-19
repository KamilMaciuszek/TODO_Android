package com.example.todo_android.Models

data class Task(
        var id: Int? = null,
        var taskTitle: String? = null,
        var taskDescription: String? = null,
        var timestamp: String? = null,
        var status: Int =1){

}