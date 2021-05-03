package school.cactus.succulentshop.validation

import school.cactus.succulentshop.R

class UsernameValidator : Validator {
    override fun validateForSignUp(field: String) = when {
        field.isEmpty() -> R.string.username_is_required
        field.length <= 2 -> R.string.username_is_too_short
        field.length >= 20 -> R.string.username_is_too_long
        field.contains(Regex("[^a-z0-9_ ]")) -> R.string.username_character_settings
        else -> null
    }

    override fun validateForSignIn(field: String): Int? {
        return null
    }
}