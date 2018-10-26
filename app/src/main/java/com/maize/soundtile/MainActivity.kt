/*
 * Created by willhou on 10/28/16.
 */

package com.maize.soundtile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var label: TextView? = null
    var btnPermission: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        label = findViewById(R.id.tvLabel)
        btnPermission = findViewById(R.id.btnPermission)
        btnPermission!!.setOnClickListener {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        }

        ensureUi()
    }

    override fun onResume() {
        super.onResume()
        ensureUi()
    }

    private fun ensureUi() {
        val permissionGranted = Util.checkNotificationPolicyAccess(this)
        label!!.setText(if (permissionGranted) R.string.all_set else R.string.permission_request)
        btnPermission!!.visibility = if (permissionGranted) View.GONE else View.VISIBLE
    }
}