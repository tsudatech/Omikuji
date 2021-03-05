package com.example.omikuji

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.about.*

class AboutActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.about)

        try {
            val info = packageManager.getPackageInfo(packageName, 0)
            textView2.text = "Version " + info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("ERROR", "error happened when getting package name")
        }

    }
}