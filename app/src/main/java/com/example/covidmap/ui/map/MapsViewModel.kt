package com.example.covidmap.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covidmap.model.Center
import com.example.covidmap.repository.Repository

class MapsViewModel:ViewModel() {

    private val repository = Repository()

    private val _centerList=MutableLiveData<List<Center>>()
    val centerList : LiveData<List<Center>>
        get() = _centerList

    fun loadSavedCenterList(){
        _centerList.postValue(repository.getSavedCenterList())
    }

}