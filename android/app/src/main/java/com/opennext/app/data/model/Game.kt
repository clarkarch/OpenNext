package com.opennext.app.data.model

data class Game(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val heroImageUrl: String,
    val publisher: String,
    val genres: List<String>,
    val membershipTier: MembershipTier,
    val isInLibrary: Boolean = false,
    val isFavorite: Boolean = false,
    val lastPlayed: String? = null,
    val rating: Float = 0f,
    val isFeatured: Boolean = false,
)

enum class MembershipTier {
    FREE, PERFORMANCE, ULTIMATE
}
