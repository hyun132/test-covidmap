package com.example.covidmap.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.covidmap.constant.COVIDConstants.CENTER_DB
import com.example.covidmap.model.Center

/**
 * CenterDao
 */
@Dao
interface CenterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCenter(center: Center): Long

    @Query("Select * from $CENTER_DB")
    fun getSavedCenter(): List<Center>
}