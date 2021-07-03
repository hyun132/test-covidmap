package com.example.covidmap.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.covidmap.api.Service
import com.example.covidmap.api.Service.Companion.api
import com.example.covidmap.db.CenterDao
import com.example.covidmap.db.CenterDatabase

class Repository {

    val db = CenterDatabase.getDatabase()

    fun getSearchResult(page:Int)= api.searchCenter(page)

    fun getSavedCenterList() = db.getDao().getSavedCenter()
}