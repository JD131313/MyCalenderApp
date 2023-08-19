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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMemo(user: Memo?)

    @Delete
    fun delete(user: Memo)



    @Query("SELECT * FROM memo WHERE uid = :uid")
    suspend fun getMemoByUid(uid: Int): Memo?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMemo(memo: Memo): Long

    @Update
    suspend fun updateMemo(memo: Memo)

    suspend fun insertOrUpdateMemo(uid: Int, memoContent: String) {
        val existingMemo = getMemoByUid(uid)
        if (existingMemo == null) {
            insertMemo(Memo(uid = uid, memo = memoContent))
        } else {
            updateMemo(existingMemo.copy(memo = memoContent))
        }
    }
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