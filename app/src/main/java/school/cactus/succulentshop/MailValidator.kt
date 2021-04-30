package school.cactus.succulentshop

class MailValidator : Validator {
    override fun validate(field: String) = when {
        field.isEmpty() -> R.string.email_is_required
        !(field.contains("@")) &&
                field.length < 5 &&
                field.length > 50 -> R.string.email_is_invalid
        else -> null
    }
}