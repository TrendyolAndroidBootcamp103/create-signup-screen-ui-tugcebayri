package school.cactus.succulentshop

class SignUpPasswordValidator : Validator {
    override fun validate(field: String) = when {
        field.isEmpty() -> R.string.password_is_required
        //(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$("[a-zA-Z]+") &&
                //field.contains("[0-9]+") &&
                //field.contains("_")) ->
        else -> null
    }
}