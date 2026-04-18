package com.livestreaming.streamly.ui.auth.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.livestreaming.streamly.core.model.AccountType
import com.livestreaming.streamly.core.model.User

fun FirebaseUser.toUser(type: AccountType): User = User(
    id = this.uid,
    email = this.email,
    name = this.displayName,
    accountType = type.value,
    phoneNumber = this.phoneNumber,
    avatar = this.photoUrl?.toString(),
)