package com.boscatov.schedulercw.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by boscatov on 13.04.2019.
 */

@Entity
data class Project(
    @PrimaryKey(autoGenerate = true) val projectId: Long = 0,
    @ColumnInfo(name = "project_name") val projectName: String
)