package com.dlgsoft.freemarket.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dlgsoft.freemarket.data.db.entities.ProductPicture

@Dao
interface ProductPictureDao {
    @Insert
    fun insertProductPicture(pic: ProductPicture)

    @Query("DELETE FROM ProductPicture")
    fun clearData()
}