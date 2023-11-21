package com.cookandroid.caffeinecalculator

import android.content.Intent
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

        binding.notify.setOnClickListener {
            val intent = Intent(activity, NotifyActivity::class.java)
            startActivity(intent)
        }

        binding.version.setOnClickListener {
            val intent = Intent(activity, VersionActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}