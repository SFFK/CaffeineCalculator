package com.cookandroid.caffeinecalculator

import android.app.Dialog
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.cookandroid.caffeinecalculator.databinding.AddDialogBinding

class AddDialog(private val context : FragmentActivity) {
    private lateinit var binding : AddDialogBinding
    private val dlg = Dialog(context)

    fun show() {
        binding = AddDialogBinding.inflate(context.layoutInflater)

        // 다이얼로그 크기 조정
        dlg.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        // 타이틀바 제거
        dlg.requestWindowFeature((Window.FEATURE_NO_TITLE))
        // 화면을 눌렀을 떄 다이얼로그가 안닫히도록 함
        dlg.setCancelable(false)
        dlg.setContentView(binding.root)

        binding.addAddBtn.setOnClickListener {
            dlg.dismiss()
        }

        binding.addCloseBtn.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }
}