package com.kiran.githubsearch.ui.repository

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.kiran.githubsearch.R
import com.kiran.githubsearch.databinding.FragmentRepositoryBinding

class RepositoryFragment : Fragment() {

    private var _binding: FragmentRepositoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentRepositoryBinding.inflate(inflater, container, false)
        val ownerName = arguments?.getString("ownerName") ?: ""
        val repoName = arguments?.getString("repoName") ?: ""
        binding.test.text = ownerName
        return binding.root
    }
}