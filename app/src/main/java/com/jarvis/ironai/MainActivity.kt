package com.jarvis.ironai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.jarvis.ironai.ui.JarvisHome
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private var speechRecognizer: SpeechRecognizer? = null
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var tts: TextToSpeech? = null
    private var ttsReady = false

    private val status = mutableStateOf("SYSTEM READY")
    private val transcript = mutableStateOf("Waiting for your command...")
    private val response = mutableStateOf("Hello! I am JARVIS.")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                startListeningInternal()
            } else {
                status.value = "MICROPHONE PERMISSION DENIED"
                speak("Please allow microphone permission.")
            }
        }

        tts = TextToSpeech(this) { result ->
            if (result == TextToSpeech.SUCCESS) {
                ttsReady = true
                tts?.language = Locale("hi", "IN")
            }
        }

        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {

                override fun onReadyForSpeech(params: Bundle?) {
                    status.value = "LISTENING..."
                }

                override fun onBeginningOfSpeech() {
                    status.value = "HEARING YOUR VOICE"
                }

                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {
                    status.value = "PROCESSING..."
                }

                override fun onError(error: Int) {
                    status.value = "VOICE ERROR - TRY AGAIN"
                }

                override fun onResults(results: Bundle?) {
                    val words = results?.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION
                    )
                    val command = words?.firstOrNull()

                    if (!command.isNullOrBlank()) {
                        transcript.value = command
                        processCommand(command)
                    } else {
                        status.value = "NO COMMAND DETECTED"
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        } else {
            status.value = "SPEECH RECOGNITION NOT AVAILABLE"
        }

        setContent {
            JarvisHome(
                status = status.value,
                transcript = transcript.value,
                response = response.value,
                onActivate = { startListening() }
            )
        }
    }

    private fun startListening() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }

        startListeningInternal()
    }

    private fun startListeningInternal() {
        if (speechRecognizer == null) {
            status.value = "SPEECH RECOGNIZER UNAVAILABLE"
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }

        try {
            speechRecognizer?.startListening(intent)
            status.value = "STARTING MICROPHONE..."
        } catch (e: Exception) {
            status.value = "COULD NOT START MICROPHONE"
        }
    }

    private fun processCommand(rawCommand: String) {
        val command = rawCommand.lowercase(Locale.ROOT)
        var answer: String

        when {
            command.contains("time") || command.contains("समय") ||
                command.contains("kitne baje") -> {
                val time = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    .format(Date())
                answer = "The current time is $time"
            }

            command.contains("hello") || command.contains("नमस्ते") ||
                command.contains("namaste") -> {
                answer = "Hello! JARVIS is online and ready."
            }

            command.contains("your name") ||
                command.contains("tumhara naam") -> {
                answer = "I am JARVIS Iron AI, your personal assistant."
            }

            command.contains("youtube") -> {
                answer = "Opening YouTube."
                openUrl("https://www.youtube.com")
            }

            command.contains("chrome") ||
                command.contains("browser") -> {
                answer = "Opening browser."
                openUrl("https://www.google.com")
            }

            command.contains("camera") -> {
                answer = "Opening camera."
                openCamera()
            }

            command.contains("wifi") -> {
                answer = "Opening Wi-Fi settings."
                openSettings(Settings.ACTION_WIFI_SETTINGS)
            }

            command.contains("bluetooth") -> {
                answer = "Opening Bluetooth settings."
                openSettings(Settings.ACTION_BLUETOOTH_SETTINGS)
            }

            command.contains("settings") ||
                command.contains("सेटिंग") -> {
                answer = "Opening phone settings."
                openSettings(Settings.ACTION_SETTINGS)
            }

            command.contains("stop") ||
                command.contains("बंद") -> {
                answer = "Okay. I am ready when you need me."
            }

            else -> {
                answer = "I heard you say: $rawCommand. I do not have an answer for this command yet."
            }
        }

        response.value = answer
        status.value = "SYSTEM READY"
        speak(answer)
    }

    private fun speak(message: String) {
        if (ttsReady) {
            tts?.speak(
                message,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "JARVIS_RESPONSE"
            )
        }
    }

    private fun openUrl(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            response.value = "Unable to open browser."
        }
    }

    private fun openCamera() {
        try {
            startActivity(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        } catch (e: Exception) {
            response.value = "Camera app is not available."
        }
    }

    private fun openSettings(action: String) {
        try {
            startActivity(Intent(action))
        } catch (e: Exception) {
            response.value = "Unable to open settings."
        }
    }

    override fun onDestroy() {
        speechRecognizer?.destroy()
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
