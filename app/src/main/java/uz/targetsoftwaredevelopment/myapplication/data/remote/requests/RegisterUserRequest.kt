package uz.targetsoftwaredevelopment.myapplication.data.remote.requests

data class RegisterUserRequest(
    val username: String? = null,
    val password: String? = null,
    val confirm: String? = null,
    val email: String? = null
)
