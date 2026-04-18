package com.livestreaming.streamly.config.utils.transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.google.i18n.phonenumbers.PhoneNumberUtil

class PhoneNumberVisualTransformation : VisualTransformation {
    private val phoneUtil = PhoneNumberUtil.getInstance()

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text // raw digits only, no "+"

        if (digits.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val formatted = runCatching {
            val number = phoneUtil.parseAndKeepRawInput("+$digits", null)
            phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
        }.getOrDefault("+$digits") // fallback: just show +digits

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var digitsSeen = 0
                for (i in formatted.indices) {
                    if (formatted[i].isDigit()) {
                        if (digitsSeen == offset) return i
                        digitsSeen++
                    }
                }
                return formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                var digitsSeen = 0
                for (i in 0 until minOf(offset, formatted.length)) {
                    if (formatted[i].isDigit()) digitsSeen++
                }
                return digitsSeen
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}