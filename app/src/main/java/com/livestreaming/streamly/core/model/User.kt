package com.livestreaming.streamly.core.model

data class User(
    val id: String?,
    val name: String?,
    val email: String?,
    val avatar: String?,
    val phoneNumber: String?,
    val accountType: AccountType,
)

sealed class AccountType(val value: Int) {
    data object Email : AccountType(1)
    data object Google : AccountType(2)
}
