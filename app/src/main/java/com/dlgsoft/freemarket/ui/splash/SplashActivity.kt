package com.dlgsoft.freemarket.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.ui.main.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val splashViewModel by viewModel<SplashViewModel>()

    private val progress: ProgressBar by lazy { findViewById(R.id.progress) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel.onSuccess.observe(this) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        splashViewModel.onError.observe(this) {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.error)
                .setMessage(it)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    finish()
                }
                .show()
        }

        splashViewModel.showLoading.observe(this) { isLoading ->
            progress.visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        splashViewModel.getInitialConfig()
    }
}