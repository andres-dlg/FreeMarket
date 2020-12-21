package com.dlgsoft.freemarket.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Category {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    @ColumnInfo(defaultValue = "")
    var name: String = ""

    @ColumnInfo(defaultValue = "")
    var serverId: String = ""

    @ColumnInfo(defaultValue = "")
    var pictureUrl: String = ""

    var totalItems: Int = 0
}
