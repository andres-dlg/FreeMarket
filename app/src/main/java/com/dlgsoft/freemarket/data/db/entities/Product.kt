package com.dlgsoft.freemarket.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Product {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    @ColumnInfo(defaultValue = "")
    var title: String? = ""

    @ColumnInfo(defaultValue = "")
    var thumbnail: String? = ""

    @ColumnInfo(defaultValue = "")
    var condition: String? = ""

    @ColumnInfo(defaultValue = "")
    var currencyId: String? = ""

    @ColumnInfo(defaultValue = "")
    var serverId: String? = ""

    @ColumnInfo(defaultValue = "")
    var address: String? = ""

    @ColumnInfo(defaultValue = "")
    var link: String? = ""

    var price: Double? = 0.0

    var categoryId: Long? = 0L
}
