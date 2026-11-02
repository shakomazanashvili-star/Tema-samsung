package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteThemeDao {
    @Query("SELECT * FROM favorite_themes ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteThemeEntity>>

    @Query("SELECT * FROM favorite_themes WHERE primaryCategory = :category ORDER BY timestamp DESC")
    fun getFavoritesByCategory(category: String): Flow<List<FavoriteThemeEntity>>

    @Query("SELECT DISTINCT primaryCategory FROM favorite_themes WHERE primaryCategory != ''")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_themes WHERE themeId = :themeId)")
    fun isFavorite(themeId: String): Flow<Boolean>

    @Query("SELECT * FROM favorite_themes WHERE themeId = :themeId LIMIT 1")
    suspend fun getFavoriteById(themeId: String): FavoriteThemeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteThemeEntity)

    @Query("UPDATE favorite_themes SET primaryCategory = :category, generatedTagsCsv = :tagsCsv, vibeDescription = :vibe WHERE themeId = :themeId")
    suspend fun updateAiMetadata(themeId: String, category: String, tagsCsv: String, vibe: String)

    @Query("DELETE FROM favorite_themes WHERE themeId = :themeId")
    suspend fun deleteFavoriteById(themeId: String)

    @Query("DELETE FROM favorite_themes")
    suspend fun clearAll()
}
