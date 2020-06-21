package com.example.bkmerchant.login

data class User(
    val name: String = "",
    val avatarUrl: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val accountType: AccountType = AccountType.CUSTOMER
)

data class UserType(
    val email: String = "",
    val accountType: AccountType = AccountType.CUSTOMER,
    val update: Boolean = false
)

enum class AccountType(val value: Int) {
    CUSTOMER(0),
    COOK(1),
    VENDOR_OWNER(2),
    MANAGER(3),
    SYS_ADMIN(4)
}