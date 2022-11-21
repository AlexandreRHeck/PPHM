package mge.mobile.pphm.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
@Entity
data class Equipment(
    @PrimaryKey val id: String,
    @ColumnInfo val number: String,
    @ColumnInfo var interval: String = "000000000000000000000000",
    @ColumnInfo var equipmentId: Int,
    @ColumnInfo val equipmentName: String,
    @ColumnInfo var equipmentQuantity: String = "0"
)