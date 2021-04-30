package school.cactus.succulentshop

class UsernameValidator : Validator {
    override fun validate(field: String) = when {
        field.isEmpty() -> R.string.username_is_required
        !(field.contains("[a-zA-Z]+") &&
                field.contains("[0-9]+") &&
                field.contains("_")) -> R.string.username_character_settings
        field.length < 2 -> R.string.username_is_too_short
        field.length > 20 -> R.string.username_is_too_long
        else -> null
    }
}