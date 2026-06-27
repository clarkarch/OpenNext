package com.opennext.app.data.model

data class User(
    val id: String,
    val displayName: String,
    val email: String,
    val avatarUrl: String,
    val membershipTier: MembershipTier,
)
