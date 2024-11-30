package com.group35.nutripath.homemenu.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.group35.nutripath.api.themealdb.MealViewModel
import com.group35.nutripath.databinding.FragmentHomeMenuBinding

class HomeMenuFragment : Fragment() {

    private var _binding: FragmentHomeMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MealViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMenuBinding.inflate(inflater, container, false)
        return binding.root
    }


}