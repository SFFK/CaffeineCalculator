package com.cookandroid.caffeinecalculator

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.cookandroid.caffeinecalculator.databinding.AddDialogBinding

class AddDialog(private val context : FragmentActivity) {
    private lateinit var binding : AddDialogBinding
    private val dlg = Dialog(context)

    interface addClickListener {
        fun onClicked(item : CoffeeData)
    }

    private lateinit var onClickedListener : addClickListener

    fun setOnClickedListener(listener : addClickListener) {
        onClickedListener = listener
    }

    fun show() {
        binding = AddDialogBinding.inflate(context.layoutInflater)

        dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 타이틀바 제거
        dlg.requestWindowFeature((Window.FEATURE_NO_TITLE))
        // 화면을 눌렀을 떄 다이얼로그가 안닫히도록 함
        dlg.setCancelable(false)
        dlg.setContentView(binding.root)

        // 다이얼로그 크기 조정
        dlg.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

        binding.addAddBtn.setOnClickListener {
            val brand = binding.addBrand.text.toString()
            val coffee = binding.addCoffee.text.toString()
            val size = binding.addSize.text.toString()
            val caffeine = binding.addCaffeine.text.toString().toDouble()

            val customItem = CoffeeData(brand, coffee, size, caffeine)
            onClickedListener.onClicked(customItem)

            dlg.dismiss()
        }

        binding.addCloseBtn.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }
}