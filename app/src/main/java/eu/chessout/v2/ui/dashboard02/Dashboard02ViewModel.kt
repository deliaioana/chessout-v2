package eu.chessout.v2.ui.dashboard02

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Dashboard02ViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "THis is detail 2 fragment"
    }
    val text: LiveData<String> = _text
}
