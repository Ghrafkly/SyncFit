package com.example.syncfit.events

sealed interface ClickEvents: AppEvents {
    object OnJoinClick: ClickEvents
    object OnSignInClick: ClickEvents
    object OnSignOutClick: ClickEvents

    object OnCreateAccountClick: ClickEvents
    object OnLogInClick: ClickEvents
    object OnGoogleLogInClick: ClickEvents

    object OnBackClick: ClickEvents
}
