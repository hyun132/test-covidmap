//package com.example.covidmap.repository
//
//import android.util.Log
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.covidmap.api.Service
//import com.example.covidmap.model.Center
//import java.io.IOException
//
///**
// * SearchDataSource
// */
//class SearchDataSource(
//) : PagingSource<Int, Center>() {
//
//    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Center> {
//        try {
//            Log.d("search : ", "SearchPagingSource.load")
//            val nextPage = params.key ?: 1
//            val response = Service.api.searchLocation(
//                display = nextPage
//            ).body()
//
//            return if (response != null) {
//                LoadResult.Page(
//                    data = response.documents,
//                    prevKey = if (nextPage == 1) null else nextPage - 1,
//                    nextKey = nextPage + 1
//                )
//            } else {
//                return LoadResult.Error(IOException())
//            }
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Center>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//}