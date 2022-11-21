package mge.mobile.pphm.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
@Entity(tableName = "ConsumerUnit")
data class ConsumerUnit(
    @PrimaryKey var number: String,
    @ColumnInfo(name = "cu_project_name") val projectName: String,
    @ColumnInfo(name = "cu_client_name") val clientName: String,
    @ColumnInfo(name = "cu_client_meter_number") val meter: String,
    @ColumnInfo(name = "cu_class") val consumerClass: String,
    @ColumnInfo(name = "cu_first_phone") val firstPhone: String,
    @ColumnInfo(name = "cu_second_phone") val secondPhone: String,
    @ColumnInfo(name = "cu_status") var status: Int
) {

    fun hasFirstPhone(): Boolean {
        return firstPhone.isNotEmpty()
    }

    fun hasSecondPhone(): Boolean {
        return secondPhone.isNotEmpty()
    }

}