package school.cactus.succulentshop.validation

import school.cactus.succulentshop.R


class IdentifierValidator : Validator {
    override fun validateForSignIn(field: String) = when {
        field.isEmpty() -> R.string.this_field_is_required
        field.length < 5 -> R.string.identifier_is_too_short
        else -> null
    }

    override fun validateForSignUp(field: String): Int? {
        return null
    }
}