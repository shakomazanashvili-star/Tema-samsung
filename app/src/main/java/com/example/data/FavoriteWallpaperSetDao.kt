package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteWallpaperSetDao {

    @Query("SELECT * FROM favorite_wallpaper_sets ORDER BY savedAtTimestamp DESC")
    fun getAllWallpaperSets(): Flow<List<FavoriteWallpaperSetEntity>>

    @Query("SELECT * FROM favorite_wallpaper_sets WHERE isQuickAccess = 1 ORDER BY savedAtTimestamp DESC")
    fun getQuickAccessWallpaperSets(): Flow<List<FavoriteWallpaperSetEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_wallpaper_sets WHERE id = :id)")
    fun isFavoriteWallpaperSet(id: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_wallpaper_sets WHERE id = :id)")
    suspend fun isFavoriteWallpaperSetSync(id: String): Boolean

    @Query("SELECT * FROM favorite_wallpaper_sets WHERE id = :id LIMIT 1")
    suspend fun getWallpaperSetById(id: String): FavoriteWallpaperSetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaperSet(entity: FavoriteWallpaperSetEntity)

    @Query("UPDATE favorite_wallpaper_sets SET isQuickAccess = :isQuickAccess WHERE id = :id")
    suspend fun updateQuickAccess(id: String, isQuickAccess: Boolean)

    @Query("DELETE FROM favorite_wallpaper_sets WHERE id = :id")
    suspend fun deleteWallpaperSetById(id: String)

    @Query("DELETE FROM favorite_wallpaper_sets")
    suspend fun clearAll()
}
