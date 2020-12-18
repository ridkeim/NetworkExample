package ru.ridkeim.networkexample

import androidx.lifecycle.*

class MainViewModel : ViewModel(), MyNetworkCallback.CurrentNetworkListener{
    private val _networkChanged = MutableLiveData(false)
    val networkChanged : LiveData<Boolean>
    get() = _networkChanged

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    override fun setSomethingChanged(value: Boolean) {
        _networkChanged.postValue(value)
    }
}