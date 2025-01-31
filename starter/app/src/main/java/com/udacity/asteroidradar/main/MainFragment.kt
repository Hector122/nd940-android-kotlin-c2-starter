package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        
        // init adapter and click listeners
        val adapter = MainAdapter(MainAdapter.AsteroidListeners { id ->
            viewModel.navigateToDetailScreen(id)
        })
        binding.asteroidRecycler.adapter = adapter
        
        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.submitList(it)
                
                binding.swipeRefresh.isRefreshing = false
                
            }
        })
        
        //Observe tha navigation variable
        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                
                //Reset the navigation variable.
                viewModel.doneNavigationToDetailScreen()
            }
        })
        
        //swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getNeoFeed() //            viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            //                binding.swipeRefresh.isRefreshing = false
            //            })
        }
        
        return binding.root
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
