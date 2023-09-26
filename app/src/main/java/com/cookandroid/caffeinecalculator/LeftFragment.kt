package com.cookandroid.caffeinecalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cookandroid.caffeinecalculator.databinding.FragmentLeftBinding

class LeftFragment : Fragment() {
    private var mBinding : FragmentLeftBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLeftBinding.inflate(inflater, container, false)
        mBinding = binding

        return binding.root
    }
}