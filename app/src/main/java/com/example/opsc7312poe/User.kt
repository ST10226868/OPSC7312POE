package com.example.opsc7312poe

data class User(
    val uid: String,
    val name: String,
    val surname: String,
    val studentNumber: String,
    val email: String,
    val profilePicUrl: String
) {
    // Logic to generate the username by combining the name and surname
    val username: String
        get() = "$name $surname"
}

