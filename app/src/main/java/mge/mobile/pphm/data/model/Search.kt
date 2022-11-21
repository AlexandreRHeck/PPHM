package mge.mobile.pphm.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
@Entity
data class Search(
    @PrimaryKey val id: String,
    @ColumnInfo val number: String,
    @ColumnInfo val count: Int,
    @ColumnInfo val answer: String,
)