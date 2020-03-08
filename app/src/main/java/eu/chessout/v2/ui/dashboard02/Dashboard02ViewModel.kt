package eu.chessout.v2.ui.dashboard02

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread

class Dashboard02ViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is detail 2 fragment"
    }

    private val _showItem = MutableLiveData<Boolean>().apply {
        value = true
    }

    private val _countValue = MutableLiveData<Int>().apply {
        value = 0;
    }

    val text: LiveData<String> = _text
    val showItem: LiveData<Boolean> = _showItem
    val countValue: LiveData<Int> = _countValue


    fun initializeCount() {
        thread() {
            while (countValue.value!! < 100) {
                _countValue.postValue(countValue.value!! + 1)
                Thread.sleep(1000)
            }

        }
    }

}
