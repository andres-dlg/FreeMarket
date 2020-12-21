package com.dlgsoft.freemarket.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dlgsoft.freemarket.data.db.entities.ProductAttribute

@Dao
interface ProductAttributeDao {
    @Insert
    fun insertProductAttribute(attribute: ProductAttribute)

    @Query("DELETE FROM ProductAttribute")
    fun clearData()
}