package com.example.covidmap

import android.app.Application
import android.content.Context

class COVIDMapApplication:Application() {

    init{
        instance = this
    }

    companion object {
        lateinit var instance: COVIDMapApplication
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }
}
