package com.draco.dfw

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PermissionDialog: Activity() {

    lateinit var message: TextView
    lateinit var grant: Button
    lateinit var deny: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.permission_dialog)
        setFinishOnTouchOutside(false)

        message = findViewById(R.id.message)
        grant = findViewById(R.id.grant)
        deny = findViewById(R.id.deny)

        val intentMessage = intent.extras.getString("message")
        message.text = intentMessage

        grant.setOnClickListener {
            Exec.permissionGranted = true
            finish()
        }

        deny.setOnClickListener {
            Exec.permissionGranted = false
            finish()
        }
    }
}