package com.example.covidmap.repository

import com.example.covidmap.api.Service.Companion.api
import com.example.covidmap.db.CenterDatabase

class Repository {

    val db = CenterDatabase.getDatabase()

    fun getSearchResult(page:Int) = api.searchCenter(page)

    fun getSavedCenterList() = db.getDao().getSavedCenter()
}