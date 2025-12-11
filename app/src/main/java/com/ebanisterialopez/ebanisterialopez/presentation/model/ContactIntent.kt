package com.ebanisterialopez.ebanisterialopez.presentation.model
sealed class ContactIntent {
    object Llamar : ContactIntent()
    object AbrirWeb : ContactIntent()
    object AbrirInstagram : ContactIntent()
    object VerUbicacion : ContactIntent()
}