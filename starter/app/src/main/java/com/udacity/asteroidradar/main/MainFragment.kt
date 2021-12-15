package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        
        binding.viewModel = viewModel
        
        setHasOptionsMenu(true)
        
        val adapter = MainAdapter()
        adapter.submitList(getDummyList())
        binding.asteroidRecycler.adapter = adapter
        
        return binding.root
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
    
    fun getDummyList(): List<Asteroid> {
        return mutableListOf(Asteroid(49,
                "Prueba (12323-)",
                "12-3-2021",
                59595.00,
                6969696.56,
                454.68,
                435545.00,
                true),
                Asteroid(49,
                        "Test (987-65)",
                        "12-3-2021",
                        59595.00,
                        6969696.56,
                        454.68,
                        435545.00,
                        true),
                Asteroid(49,
                        "Change (12323-)",
                        "12-3-2021",
                        59595.00,
                        6969696.56,
                        454.68,
                        435545.00,
                        true))
    }
}
