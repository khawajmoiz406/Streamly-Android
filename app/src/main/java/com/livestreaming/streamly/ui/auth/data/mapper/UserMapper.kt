package com.livestreaming.streamly.ui.auth.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.livestreaming.streamly.core.model.AccountType
import com.livestreaming.streamly.core.model.User

fun FirebaseUser.toUser(type: AccountType): User = User(
    id = this.uid,
    email = this.email,
    accountType = type,
    name = this.displayName,
    phoneNumber = this.phoneNumber,
    avatar = this.photoUrl?.toString(),
)