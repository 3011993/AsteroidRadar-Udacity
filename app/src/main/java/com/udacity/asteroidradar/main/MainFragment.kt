package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.AsteroidAdapter
import com.udacity.asteroidradar.PictureOfDay

import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var factory: Factory
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application

        factory = Factory(application)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        val adapter = AsteroidAdapter(AsteroidAdapter.AsteroidClickListener {
            viewModel.displayDetailFragment(it)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayDetailFragmentDone()
            }

        }

        viewModel.images.observe(viewLifecycleOwner) {
            it?.let {
                if (it.mediaType == "image") {
                    setImageUrl(it)
                }
            }
        }


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.show_week_asteroids -> viewModel.getAsteroidOfWeek()
            R.id.show_today_asteroids -> viewModel.getAsteroidOfDay()
            R.id.show_saved_asteroids -> viewModel.getAllasteroids()
        }
        return true
    }



    private fun setImageUrl(pictureOfDay: PictureOfDay) {
        Picasso.with(binding.activityMainImageOfTheDay.context)
            .load(pictureOfDay.url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.placeholder_picture_of_day)
            .into(binding.activityMainImageOfTheDay)
        binding.textView.text = pictureOfDay.title
    }

}
