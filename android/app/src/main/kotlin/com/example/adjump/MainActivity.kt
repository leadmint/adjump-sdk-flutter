package com.example.adjump

import android.content.Context
import android.util.Log
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.adjump.offerwall.AdJump

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.offerwall_app"
    private var adJump: AdJump? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "launchOfferWall" -> {
                        val arguments = call.arguments as? Map<*, *>
                        if (arguments != null) {
                            val accountId = arguments["accountId"] as? String
                            val appId = arguments["appId"] as? String
                            val userId = arguments["userId"] as? String

                            if (accountId != null && appId != null && userId != null) {
                                // Initialize AdJump with the provided credentials
                                adJump = AdJump(this, accountId, appId, userId)
                                launchOfferWall()
                                result.success("Offer wall launched")
                            } else {
                                result.error("INVALID_ARGUMENTS", "Missing required parameters", null)
                            }
                        } else {
                            result.error("INVALID_ARGUMENTS", "Arguments are required", null)
                        }
                    }
                    else -> result.notImplemented()
                }
            }
    }

    private fun launchOfferWall() {
        adJump?.initialize(object : AdJump.InitialisationListener {
            override fun onInitialisationSuccess() {
                runOnUiThread { Toast.makeText(this@MainActivity, "AdJump SDK Initialized", Toast.LENGTH_SHORT).show() }
                // Show the offer wall
                if (adJump?.isAvailable == true) {
                    adJump?.launchOfferWall()
                } else {
                    Toast.makeText(this@MainActivity, "Offerwall not available!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onInitialisationError(exception: Exception) {
                runOnUiThread { Toast.makeText(this@MainActivity, "Initialization failed: ${exception.message}", Toast.LENGTH_SHORT).show() }
            }
        })
    }
}
