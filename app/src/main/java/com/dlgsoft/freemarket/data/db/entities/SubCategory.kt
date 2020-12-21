package com.dlgsoft.freemarket.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SubCategory {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    @ColumnInfo(defaultValue = "")
    var name: String = ""

    @ColumnInfo(defaultValue = "")
    var serverId: String = ""

    var categoryId: Long = 0L
}
