package com.dlgsoft.freemarket.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dlgsoft.freemarket.data.db.entities.Site

@Dao
interface SiteDao {
    @Insert
    fun insertSite(site: Site)

    @Query("DELETE FROM Site")
    fun clearData()

    @Query("SELECT * FROM Site ORDER BY name")
    fun getAllSites(): List<Site>
}