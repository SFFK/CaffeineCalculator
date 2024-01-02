package com.cookandroid.caffeinecalculator

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cookandroid.caffeinecalculator.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private var mBinding : FragmentSettingBinding? = null
    private val bundle : Bundle? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSettingBinding.inflate(inflater, container, false)
        mBinding = binding

        // 기록을 위한 sharedPreferences 객체 생성
        val sharedPreferences = context?.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        // 클릭 이벤트...

        binding.notify.setOnClickListener {
            startActivity(Intent(activity, NotifyActivity::class.java))
        }

        binding.version.setOnClickListener {
            val intent = Intent(activity, VersionActivity::class.java)
            startActivity(intent)
        }

        binding.use.setOnClickListener {
            val intent = Intent(activity, UseActivity::class.java)
            startActivity(intent)
        }

        binding.popup.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
                .setTitle("푸시 알림 요청")
                .setMessage("푸시알림을 허용하시겠습니까?")
                .setPositiveButton("예", DialogInterface.OnClickListener { dialog, id ->
                    editor?.putBoolean("result", true)?.apply()
                })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, id->
                    editor?.putBoolean("result", false)?.apply()
                })
                .show()
        }

        return binding.root
    }
}