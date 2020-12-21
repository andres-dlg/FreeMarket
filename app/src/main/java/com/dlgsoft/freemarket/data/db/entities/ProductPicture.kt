package com.dlgsoft.freemarket.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductPicture(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(defaultValue = "")
    val url: String? = "",

    val productId: Long? = 0L
)