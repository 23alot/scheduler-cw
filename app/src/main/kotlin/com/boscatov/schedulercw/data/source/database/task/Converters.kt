package com.boscatov.schedulercw.data.source.database.task

import androidx.room.TypeConverter
import com.boscatov.schedulercw.data.entity.TaskStatus
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromIntToTaskStatus(taskStatus: Int?): TaskStatus? {
        return if (taskStatus != null) TaskStatus.values()[taskStatus] else null
    }

    @TypeConverter
    fun fromTaskStatusToInt(taskStatus: TaskStatus?): Int? {
        return taskStatus?.ordinal
    }
}