package top.niunaijun.varuna_dumper.view.setting

import android.os.Bundle
import top.niunaijun.varuna_dumper.R
import top.niunaijun.varuna_dumper.databinding.ActivitySettingBinding
import top.niunaijun.varuna_dumper.util.inflate
import top.niunaijun.varuna_dumper.view.base.BaseActivity
import top.niunaijun.varuna_dumper.view.base.PermissionActivity

class SettingActivity : PermissionActivity() {

    private val viewBinding: ActivitySettingBinding by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initToolbar(viewBinding.toolbarLayout.toolbar, R.string.app_setting,true)
        supportFragmentManager.beginTransaction().replace(R.id.fragment,SettingFragment()).commit()
    }

    fun setRequestCallback(callback:((Boolean)->Unit)?){
        this.requestPermissionCallback = callback
        requestStoragePermission()
    }
}