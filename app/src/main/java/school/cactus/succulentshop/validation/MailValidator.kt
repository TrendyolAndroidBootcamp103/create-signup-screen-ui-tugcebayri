package school.cactus.succulentshop.validation

import school.cactus.succulentshop.R

class MailValidator : Validator {
    override fun validateForSignUp(field: String) = when {
        field.isEmpty() -> R.string.email_is_required
        //!field.contains(Regex("@")) -> R.string.email_is_invalid
        !(field.contains("@")) -> R.string.email_is_invalid
        field.length <= 5 -> R.string.email_is_invalid
        field.length >= 50 -> R.string.email_is_invalid
        else -> null
    }

    override fun validateForSignIn(field: String): Int? {
        return null
    }
}