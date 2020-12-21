package com.dlgsoft.freemarket.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dlgsoft.freemarket.data.db.dao.*
import com.dlgsoft.freemarket.data.db.entities.*

@Database(
    entities = [
        Category::class,
        SubCategory::class,
        Product::class,
        ProductAttribute::class,
        ProductPicture::class,
        Site::class
    ],
    version = 1
)
abstract class FreeMarketDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val productDao: ProductDao
    abstract val productAttributeDao: ProductAttributeDao
    abstract val productPictureDao: ProductPictureDao
    abstract val siteDao: SiteDao
}