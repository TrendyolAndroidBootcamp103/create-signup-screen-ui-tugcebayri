package school.cactus.succulentshop.validation

interface Validator {
    fun validateForSignIn(field: String): Int?

    fun validateForSignUp(field: String): Int?
}