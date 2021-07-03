package com.example.covidmap.model


data class CenterResponse(
    val currentCount: Int,
    val `data`: List<Center>,
    val matchCount: Int,
    val page: Int,
    val perPage: Int,
    val totalCount: Int
)