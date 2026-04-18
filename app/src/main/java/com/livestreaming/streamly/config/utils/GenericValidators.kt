package com.livestreaming.streamly.config.utils

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.livestream.streamly.R

object GenericValidators {
    fun validateField(value: String, maxLength: Int? = null): Int? {
        return when {
            value.isBlank() -> R.string.field_is_required
            value.length < 2 -> R.string.atleast_two_characters_msg
            maxLength != null && value.length > maxLength -> R.string.must_be_less_than_fifty_characters
            else -> null
        }
    }

    fun validateEmail(value: String): Int? {
        return when {
            value.isBlank() -> R.string.field_is_required
            !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() -> R.string.invalid_email
            else -> null
        }
    }

    fun validatePassword(value: String): Int? {
        return when {
            value.isBlank() -> R.string.field_is_required
            value.length < 8 -> R.string.password_min_eight_characters
            !value.any { it.isUpperCase() } -> R.string.password_must_contain_uppercase
            !value.any { it.isLowerCase() } -> R.string.password_must_contain_lowercase
            !value.any { it.isDigit() } -> R.string.password_must_contain_digit
            !value.any { !it.isLetterOrDigit() } -> R.string.password_must_contain_special_character
            else -> null
        }
    }

    fun validatePhoneNumber(digits: String): Int? {
        if (digits.isEmpty()) return R.string.field_is_required

        val validNumber = runCatching {
            val phoneUtil = PhoneNumberUtil.getInstance()
            val number = phoneUtil.parseAndKeepRawInput("+$digits", null)
            val region = phoneUtil.getRegionCodeForNumber(number)
            phoneUtil.isValidNumberForRegion(number, region)
        }.getOrDefault(false)

        if (!validNumber) return R.string.invalid_phone_number
        return null
    }
}
