package top.niunaijun.varuna_dumper.view.main

import android.view.ViewGroup
import top.niunaijun.varuna_dumper.data.entity.AppInfo
import top.niunaijun.varuna_dumper.databinding.ItemPackageBinding
import top.niunaijun.varuna_dumper.util.newBindingViewHolder
import top.niunaijun.varuna_dumper.view.base.BaseAdapter

/**
 *
 * @Description: 软件显示界面适配器
 * @Author: wukaicheng
 * @CreateDate: 2021/4/29 21:52
 */

class MainAdapter : BaseAdapter<ItemPackageBinding, AppInfo>() {
    override fun getViewBinding(parent: ViewGroup): ItemPackageBinding {
        return newBindingViewHolder(parent, false)

    }

    override fun initView(binding: ItemPackageBinding, position: Int, data: AppInfo) {
        binding.icon.setImageDrawable(data.icon)
        binding.name.text = data.name
        binding.packageName.text = data.packageName
    }
}