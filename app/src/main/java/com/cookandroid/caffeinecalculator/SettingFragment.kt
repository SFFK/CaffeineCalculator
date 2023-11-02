package com.cookandroid.caffeinecalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cookandroid.caffeinecalculator.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private var mBinding : FragmentSettingBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSettingBinding.inflate(inflater, container, false)
        mBinding = binding

        return binding.root
    }
}