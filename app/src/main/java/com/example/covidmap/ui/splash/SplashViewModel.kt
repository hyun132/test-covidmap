package com.example.covidmap.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covidmap.model.Center
import com.example.covidmap.model.CenterResponse
import com.example.covidmap.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel : ViewModel() {

    private val repository = Repository()

    private val _centerList = MutableLiveData<List<Center>>()
    val centerList: LiveData<List<Center>>
        get() = _centerList

    var currentPage = MutableLiveData(1)

    fun loadCenterList(page: Int) {
        repository.getSearchResult(page).enqueue(object : Callback<CenterResponse> {
            override fun onResponse(
                call: Call<CenterResponse>,
                response: Response<CenterResponse>
            ) {
                response.body().let {
                    if (it != null) {
                        _centerList.postValue(it.data)
                        currentPage.postValue(it.page + 1)
                    }
                }
            }

            override fun onFailure(call: Call<CenterResponse>, t: Throwable) {
                println(t.message)
            }

        })
    }


    fun saveCenterList(centerList: List<Center>) {
        for (center in centerList) {
            repository.db.getDao().saveCenter(center)
        }
    }
}