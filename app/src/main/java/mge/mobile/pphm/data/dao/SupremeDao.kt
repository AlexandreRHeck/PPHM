package mge.mobile.pphm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import mge.mobile.pphm.data.model.ConsumerUnit
import mge.mobile.pphm.data.model.Equipment
import mge.mobile.pphm.data.model.Search

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
@Dao
interface SupremeDao {

    @Insert
    fun insertConsumerUnit(consumerUnit: ConsumerUnit)

    @Insert
    fun insertEquipment(equipment: Equipment)

    @Insert
    fun insertSearch(search: Search)

    @Update
    fun updateConsumerStatus(consumerUnit: ConsumerUnit)

    @Query("SELECT * FROM consumerunit")
    fun getAll(): List<ConsumerUnit>

    @Query("SELECT * FROM consumerunit where number = :name")
    fun getById(name: String): ConsumerUnit

    @Query("SELECT * FROM search where number = :consumerunitNumber order by count asc")
    fun getListOfSearch(consumerunitNumber: String): List<Search>

    @Query("SELECT * FROM equipment where number = :consumerunitNumber order by equipmentId")
    fun getListOfEquipments(consumerunitNumber: String): List<Equipment>

    @Query("SELECT * FROM consumerunit where cu_class = :mClass")
    fun getByClass(mClass: String): List<ConsumerUnit>

    @Query("SELECT * FROM consumerunit where cu_class = :mClass and cu_status = 4")
    fun getByStatusDoneAndClass(mClass: String): List<ConsumerUnit>

    @Query("SELECT * FROM consumerunit where cu_status = :status")
    fun getConsumerUnitByStatus(status: Int): List<ConsumerUnit>

    @Query("DELETE FROM consumerunit")
    fun nukeTableConsumerUnit()

    @Query("DELETE FROM equipment")
    fun nukeTableEquipment()

    @Query("DELETE FROM search")
    fun nukeTableSearch()

}