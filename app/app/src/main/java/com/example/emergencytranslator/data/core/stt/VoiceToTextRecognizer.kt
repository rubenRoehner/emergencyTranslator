package com.example.emergencytranslator.data.core.stt

import android.app.Application
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.SpeechService
import org.vosk.android.StorageService
import java.io.IOException
import javax.inject.Singleton

@Singleton
class VoiceToTextRecognizer(private val application: Application) :
    org.vosk.android.RecognitionListener {

    private val supportedLanguages: Array<String> = arrayOf("de", "en", "es", "fr", "ru", "tr");

    private val _infos = MutableStateFlow(VoiceToTextRecognizerInfos())
    val infos: StateFlow<VoiceToTextRecognizerInfos>
        get() = _infos.asStateFlow()

    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1

    private var model: Model? = null
    private var speechService: SpeechService? = null

    private var currentLanguageCode = ""
    private var isModelInitialized = false

    fun startListening() {
        if(!isModelInitialized){
            updateLanguage(currentLanguageCode)
        }

        //TODO: Check if user has given permission to record audio, init the model after permission is granted

        if(isModelInitialized){
            recognizeMicrophone()
        }
    }

    fun updateLanguage(languageCode: String) : Boolean{
        Log.i("updateLanguage received:", languageCode)
        var isLanguageSupported : Boolean = true
        var safeLanguageCode : String = languageCode

        if(safeLanguageCode == "") {
            safeLanguageCode = "de"
        }

        if(currentLanguageCode == safeLanguageCode) return true
        if(safeLanguageCode !in supportedLanguages) {
            isLanguageSupported = false
            safeLanguageCode = "de"
        }

        val sourcePath = "model-small-$safeLanguageCode"

        StorageService.unpack(application, sourcePath, "model",
            { model ->
                this.model = model
                this.isModelInitialized = true
                currentLanguageCode = safeLanguageCode

                updateState(VoiceToTextRecognizerState.STATE_READY)
            }
        ) { exception ->
            this.isModelInitialized = false
            updateError(exception.message.toString())
            updateState(VoiceToTextRecognizerState.STATE_DONE)
        }

        return isLanguageSupported
    }

    fun stopListening(){
        onStop()
    }

    private fun recognizeMicrophone() {
        if (speechService != null) {
            onStop()
        } else {
            updateState(VoiceToTextRecognizerState.STATE_MIC)

            try {
                val rec = Recognizer(model, 16000.0f)
                speechService = SpeechService(rec, 16000.0f)
                speechService!!.startListening(this)

            } catch (e: IOException) {
                updateError(e.message.toString())
                Log.e("recognizeMicrophone", e.message.toString())
            }
        }
    }

    override fun onPartialResult(hypothesis: String?) {}

    override fun onResult(hypothesis: String?) {
        if (hypothesis != null) {
            updateText(hypothesis)
        }
        updateState(VoiceToTextRecognizerState.STATE_DONE)
    }

    override fun onFinalResult(hypothesis: String?) {
        updateState(VoiceToTextRecognizerState.STATE_DONE)
    }

    override fun onError(exception: Exception?) {
        if (exception != null) {
            updateError(exception.message.toString())
            Log.e("onError", exception.message.toString())
        }
    }

    override fun onTimeout() {
        updateState(VoiceToTextRecognizerState.STATE_DONE)
    }

    private fun onStop(){
        updateState(VoiceToTextRecognizerState.STATE_DONE)
        speechService!!.stop()
        speechService = null
    }

    private fun updateText(newText: String) {
        if (newText == "") {
            stopListening()
            return
        }

        val extractedText = newText.substringAfter(": \"").substringBefore("\"")

        _infos.update {
            it.copy(
                spokenText = extractedText
            )
        }

        Log.i("updateText", "Updated Text to:$extractedText")
    }

    private fun updateState(newState: VoiceToTextRecognizerState){
        if(this._infos.value.state != newState){
            _infos.update {
                it.copy(
                    state = newState
                )
            }

            if(newState == VoiceToTextRecognizerState.STATE_DONE) {
                stopListening()
            }
        }
    }

    private fun updateError(newErrorMessage: String){
        _infos.update {
            it.copy(
                error = newErrorMessage
            )
        }
    }

    data class VoiceToTextRecognizerInfos(
        var state: VoiceToTextRecognizerState = VoiceToTextRecognizerState.STATE_START,
        var spokenText: String = "",
        val error: String? = null
    )
    enum class VoiceToTextRecognizerState {
        STATE_START, STATE_READY, STATE_DONE, STATE_MIC
    }
}