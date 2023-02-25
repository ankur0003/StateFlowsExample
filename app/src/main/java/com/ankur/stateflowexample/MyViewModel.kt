package com.ankur.stateflowexample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    /*
    *create a immutable state flow
     */
    private val _loginUIState = MutableStateFlow<LoginUIState>(LoginUIState.Empty)

    val loginUIState :StateFlow<LoginUIState>  = _loginUIState



    fun login(username:String,password:String) = viewModelScope
        .launch{

            Log.d("MyViewModel","$username $password")
        _loginUIState.value = LoginUIState.Loading
            delay(2000L)
            if(username=="ankur" && password =="topsecret"){
                _loginUIState.value = LoginUIState.Success
            }else{
                _loginUIState .value = LoginUIState.Error("Wring Credentials")
            }
    }


    // Sealed class i similar to enum unlike enum we can define more class inside
    //sealed class and those classes are only allowed to inherit from the Sealed Class
    sealed class LoginUIState{
        object Success : LoginUIState()
        data class Error(val message:String) : LoginUIState()
        object Loading : LoginUIState()
        //Empty state because we need to define an initial state other the 3 above
        object Empty :LoginUIState()
    }
}