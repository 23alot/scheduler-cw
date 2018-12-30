package com.boscatov.schedulercw.data.entity

data class Task (val taskId: Long,
                 val taskTitle: String,
                 val taskDescription: String,
                 val taskColor: Int,
                 val taskDuration: Int,
                 val taskDateStart: String,
                 val taskTimeStart: String
)