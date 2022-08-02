package com.example.androidthreading

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.StringBuilder
private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver: BroadcastReceiver() {
    private val scope = CoroutineScope(SupervisorJob())

    override fun onReceive(context: Context, intent: Intent) {

        Toast.makeText(context, "Receiver got message, check logs", Toast.LENGTH_LONG).show()
        val pendingResult: PendingResult = goAsync()
        scope.launch(Dispatchers.Default) {
            try {
                buildString {
                    append("Action: ").append(intent.action).append("\n")
                    append("URI: ").append(intent.toUri(Intent.URI_INTENT_SCHEME))
                }.also {
                    Log.d(TAG, it)
                }
            } finally {

                pendingResult.finish()
            }
        }
        /*
        StringBuilder().apply {
            append("Action: ${intent.action}\n")
            append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}")
            toString().also {
            }
        }

         */
    }
}