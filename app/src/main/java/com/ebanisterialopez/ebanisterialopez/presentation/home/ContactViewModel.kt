package com.ebanisterialopez.ebanisterialopez.presentation.home

import com.ebanisterialopez.ebanisterialopez.presentation.model.ContactIntent
import com.ebanisterialopez.ebanisterialopez.presentation.model.ContactUiState
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState

    fun onIntent(intent: ContactIntent) {
        val ctx = getApplication<Application>()
        val state = _uiState.value

        viewModelScope.launch {
            try {
                when (intent) {
                    ContactIntent.Llamar -> {
                        val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${state.telefono}"))
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        ctx.startActivity(i)
                    }
                    ContactIntent.AbrirWeb -> {
                        val i = Intent(Intent.ACTION_VIEW, Uri.parse(state.sitioWeb))
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        ctx.startActivity(i)
                    }
                    ContactIntent.AbrirInstagram -> {
                        val i = Intent(Intent.ACTION_VIEW, Uri.parse(state.instagramUrl))
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        ctx.startActivity(i)
                    }
                    ContactIntent.VerUbicacion -> {
                        val i = Intent(Intent.ACTION_VIEW, Uri.parse(state.ubicacionUrl))
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        ctx.startActivity(i)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }
}