package com.example.covidmap.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.centerList.observe(viewLifecycleOwner, {
            viewModel.saveCenterList(it)
        })

        viewModel.currentPage.observe(viewLifecycleOwner,{
            if (it>=10){
                findNavController().navigate(R.id.action_splashFragment_to_mapsFragment)
            }
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.loadCenterList(it)
                delay(50)
            }
        })

    }
}