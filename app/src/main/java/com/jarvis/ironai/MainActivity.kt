package com.jarvis.ironai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.RecognitionListener
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import com.jarvis.ironai.ui.JarvisHome
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {

    private var status by mutableStateOf("JARVIS ONLINE")
    private var transcript by mutableStateOf("Hello Sir. I am ready.")

    private var recognizer: SpeechRecognizer? = null
    private var tts: TextToSpeech? = null

    private val permission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) startListening()
            else status = "MICROPHONE PERMISSION DENIED"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this) {
            tts?.language = Locale("hi", "IN")
        }

        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            recognizer = SpeechRecognizer.createSpeechRecognizer(this)

            recognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    status = "LISTENING..."
                }

                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    status = "THINKING..."
                }

                override fun onError(error: Int) {
                    status = "VOICE ERROR: $error"
                }

                override fun onResults(results: Bundle?) {
                    val command = results?.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION
                    )?.firstOrNull() ?: return

                    transcript = command
                    val lower = command.lowercase(Locale.getDefault())

                    val answer = when {
                        "time" in lower || "समय" in lower ->
                            "समय है " + SimpleDateFormat(
                                "hh:mm a", Locale.getDefault()
                            ).format(Date())

                        "hello" in lower || "नमस्ते" in lower ->
                            "नमस्ते सर। JARVIS तैयार है।"

                        "jarvis" in lower ->
                            "जी सर, बताइए।"

                        else ->
                            "सर, आपका कमांड प्राप्त हुआ। AI Brain अगले चरण में जोड़ा जाएगा।"
                    }

                    status = "JARVIS ONLINE"
                    tts?.speak(
                        answer,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "jarvis"
                    )
                }

                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        } else {
            status = "SPEECH SERVICE UNAVAILABLE"
        }

        setContent {
            JarvisHome(
                status = status,
                transcript = transcript,
                onListen = {
                    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        startListening()
                    } else {
                        permission.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
            )
        }
    }

    private fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        try {
            recognizer?.startListening(intent)
        } catch (e: Exception) {
            status = "VOICE SERVICE ERROR"
        }
    }

    override fun onDestroy() {
        recognizer?.destroy()
        tts?.shutdown()
        super.onDestroy()
    }
}
