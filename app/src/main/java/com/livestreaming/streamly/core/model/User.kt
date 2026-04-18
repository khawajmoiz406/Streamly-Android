package com.livestreaming.streamly.core.model

data class User(
    val id: String?,
    val name: String?,
    val email: String?,
    val avatar: String?,
    val phoneNumber: String?,
    val accountType: Int,
) {
    fun getAccountType() = AccountType.fromValue(accountType)
}

sealed class AccountType(val value: Int) {
    data object Email : AccountType(1)
    data object Google : AccountType(2)

    companion object {
        fun fromValue(value: Int): AccountType = when (value) {
            1 -> Email
            2 -> Google
            else -> Email
        }
    }
}
