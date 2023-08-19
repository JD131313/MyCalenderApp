package com.example.mycalenderapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {
    @Query("SELECT * FROM memo")
    fun getAll(): Flow<List<Memo>>

    @Query("SELECT * FROM memo WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Memo>

    @Query("SELECT * FROM memo WHERE uid == (:userId)")
    fun loadUid(userId: Int): Flow<Memo>

//   	@Query("SELECT * FROM user WHERE first_name LIKE :first AND " +           //안쓰는 코드라 주석처리
//          "last_name LIKE :last LIMIT 1")
//  	fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: Memo?)

    @Update
    suspend fun updateChecklist(user: Memo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMemo(user: Memo)

    @Delete
    fun delete(user: Memo)

    @Query("SELECT * FROM memo")
    fun getAllFlow(): Flow<List<Memo>>
}

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM checklist")
    fun getAll(): Flow<List<Checklist>>

    @Query("SELECT * FROM checklist WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Checklist>

//   	@Query("SELECT * FROM user WHERE first_name LIKE :first AND " +           //안쓰는 코드라 주석처리
//          "last_name LIKE :last LIMIT 1")
//  	fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: Checklist?)

    @Update
    suspend fun updateChecklist(user: Checklist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateChecklist(user: Checklist)

    @Delete
    fun delete(user: Checklist)

    @Query("SELECT * FROM memo")
    fun getAllFlow(): Flow<List<Checklist>>
}