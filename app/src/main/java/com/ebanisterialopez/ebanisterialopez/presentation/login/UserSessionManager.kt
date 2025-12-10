package com.ebanisterialopez.ebanisterialopez.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class UserSessionManager @Inject constructor(
) : ViewModel() {
    fun clearSession() {
        println("UserSessionManager:.")
    }
}