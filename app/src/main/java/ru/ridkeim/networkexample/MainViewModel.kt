package ru.ridkeim.networkexample

import androidx.lifecycle.*

class MainViewModel : ViewModel(), MyNetworkCallback.CurrentNetworkListener{
    private val _hasInternet = MutableLiveData(false)
    val hasInternet : LiveData<Boolean>
    get() = _hasInternet

    override fun setNetworkAvailable(value: Boolean) {
        _hasInternet.postValue(value)
    }


    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}