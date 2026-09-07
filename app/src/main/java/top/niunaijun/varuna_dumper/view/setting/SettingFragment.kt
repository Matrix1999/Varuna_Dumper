package top.niunaijun.varuna_dumper.view.setting

import android.os.Bundle
import android.os.Environment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.folderChooser
import top.niunaijun.varuna_dumper.app.App
import top.niunaijun.varuna_dumper.R
import top.niunaijun.varuna_dumper.app.AppManager
import top.niunaijun.varuna_dumper.app.VarunaDumperLoader
import java.io.File


/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/5/28 19:55
 */
class SettingFragment : PreferenceFragmentCompat() {

    private lateinit var savePathPreference: Preference

    private lateinit var saveEnablePreference: SwitchPreferenceCompat

    private lateinit var fixCodeItemPreference: SwitchPreferenceCompat

    private lateinit var hookDumpPreference: SwitchPreferenceCompat

    private val initialDirectory = AppManager.mVarunaDumperLoader.getSavePath()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting)
        savePathPreference = findPreference("save_path")!!
        savePathPreference.onPreferenceClickListener = mSavedPathClick
        savePathPreference.summary = initialDirectory

        saveEnablePreference = findPreference("save_enable")!!
        saveEnablePreference.onPreferenceChangeListener = mSaveEnableChange
        saveEnablePreference.isChecked = AppManager.mVarunaDumperLoader.saveEnable()

        fixCodeItemPreference = findPreference("fix_code_item")!!
        fixCodeItemPreference.onPreferenceChangeListener = mFixCodeItemChange
        fixCodeItemPreference.isChecked = AppManager.mVarunaDumperLoader.isFixCodeItem()

        hookDumpPreference = findPreference("hook_dump")!!
        hookDumpPreference.onPreferenceChangeListener = mHookDumpChange
        hookDumpPreference.isChecked = AppManager.mVarunaDumperLoader.isHookDump()

    }

    private val mSavedPathClick = Preference.OnPreferenceClickListener {
        val initialFile = with(initialDirectory) {
            if (initialDirectory.isEmpty()) {
                Environment.getExternalStorageDirectory()
            } else {
                File(this)
            }
        }

        MaterialDialog(requireContext()).show {
            folderChooser(
                requireContext(),
                initialDirectory = initialFile,
                allowFolderCreation = true
            ) { _, file ->
                AppManager.mVarunaDumperLoader.setSavePath(file.absolutePath)
                savePathPreference.summary = file.absolutePath
            }
            negativeButton(res = R.string.cancel)
        }
        return@OnPreferenceClickListener true
    }

    private val mSaveEnableChange = Preference.OnPreferenceChangeListener { _, newValue ->
        if (newValue == false) {
            (requireActivity() as SettingActivity).setRequestCallback(requestResult)
        } else {
            AppManager.mVarunaDumperLoader.saveEnable(true)
            saveEnablePreference.isChecked = true
        }
        return@OnPreferenceChangeListener true
    }

    private val mHookDumpChange = Preference.OnPreferenceChangeListener { _, newValue ->
        AppManager.mVarunaDumperLoader.setHookDump(newValue as Boolean)
        return@OnPreferenceChangeListener true
    }

    private val mFixCodeItemChange = Preference.OnPreferenceChangeListener { _, newValue ->
        if (newValue as Boolean) {

            MaterialDialog(requireContext()).show {
                title(R.string.warn)
                message(R.string.fix_code_item_message)
                positiveButton(R.string.confirm) {
                    AppManager.mVarunaDumperLoader.setFixCodeItem(true)
                }
                negativeButton(R.string.cancel) {
                    fixCodeItemPreference.isChecked = false
                    AppManager.mVarunaDumperLoader.setFixCodeItem(false)
                }
            }

        } else {
            AppManager.mVarunaDumperLoader.setFixCodeItem(newValue)
        }
        return@OnPreferenceChangeListener true
    }


    private val requestResult = { hasPermission: Boolean ->
        AppManager.mVarunaDumperLoader.saveEnable(!hasPermission)
        saveEnablePreference.isChecked = !hasPermission

        if (AppManager.mVarunaDumperLoader.getSavePath().isEmpty()) {
            val path = VarunaDumperLoader.getDexDumpDir(App.getContext())
            AppManager.mVarunaDumperLoader.setSavePath(path)
            savePathPreference.summary = path
        }
    }
}
