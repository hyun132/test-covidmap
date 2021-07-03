package com.example.covidmap.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.covidmap.R
import com.example.covidmap.databinding.FragmentSplashBinding
import com.example.covidmap.ui.map.MapsViewModel
import kotlinx.coroutines.*

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println(">>>>>>>>>>>>> splash onViewCreated >>>>>>>>>>")


//            val response = viewModel.loadCenterList(currentPage)
//            println(response.message().toString())
//            if (response.isSuccessful){
//                response.body()?.data?.let { viewModel.saveCenterList(it)
//                currentPage++}
//            }
        viewModel.centerList.observe(viewLifecycleOwner, {
            //리스트에게 알려준당
            viewModel.saveCenterList(it)
        })

//        CoroutineScope(Dispatchers.IO).launch {
//            viewModel.currentPage.value?.let { viewModel.loadCenterList(it) }
//        }
        viewModel.currentPage.observe(viewLifecycleOwner,{
            if (it>=10){
                println("@@@@@@@@@@@@@@살려줘,,,@@@@@@@@@@@@@@2")
                findNavController().navigate(R.id.action_splashFragment_to_mapsFragment)
            }
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.loadCenterList(it)
                delay(50)
            }
        })







    }
}