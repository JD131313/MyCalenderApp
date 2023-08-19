package com.example.mycalenderapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memo(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo("memo") val memo: String? = null,
)

@Entity
data class Checklist(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo("checklist") var checklist: String? = null,
    @ColumnInfo("isChecked") var isChecked: Boolean? = false,
)
