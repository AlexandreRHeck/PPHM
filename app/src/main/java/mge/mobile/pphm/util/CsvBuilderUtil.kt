package mge.mobile.pphm.util

import android.content.Context
import mge.mobile.pphm.R

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class CsvBuilderUtil {

    companion object {
        fun residentialSearchHeader(context: Context): Array<String> = arrayOf(
            "UC",
            "STATUS",
            context.getString(R.string.residential_search_quest_1),
            context.getString(R.string.residential_search_quest_2),
            context.getString(R.string.residential_search_quest_3),
            context.getString(R.string.residential_search_quest_4),
            context.getString(R.string.residential_search_quest_5),
            context.getString(R.string.residential_search_quest_6),
            context.getString(R.string.residential_search_quest_7),
            context.getString(R.string.residential_search_quest_8),
            context.getString(R.string.residential_search_quest_9),
            context.getString(R.string.residential_search_quest_10),
            context.getString(R.string.residential_search_quest_11)
        )

        fun ruralSearchHeader(context: Context): Array<String> = arrayOf(
            "UC",
            "STATUS",
            context.getString(R.string.rural_search_quest_1),
            context.getString(R.string.rural_search_quest_2),
            context.getString(R.string.rural_search_quest_3),
            context.getString(R.string.rural_search_quest_4),
            context.getString(R.string.rural_search_quest_5),
            context.getString(R.string.rural_search_quest_6),
            context.getString(R.string.rural_search_quest_7),
            context.getString(R.string.rural_search_quest_8),
            context.getString(R.string.rural_search_quest_9),
            context.getString(R.string.rural_search_quest_10),
            context.getString(R.string.rural_search_quest_11)
        )

        fun commercialIndustrialHeader(context: Context): Array<String> = arrayOf(
            "UC",
            "STATUS",
            context.getString(R.string.industrial_commercial_search_quest_1),
            context.getString(R.string.industrial_commercial_search_quest_2),
            context.getString(R.string.industrial_commercial_search_quest_3),
            context.getString(R.string.industrial_commercial_search_quest_4),
            context.getString(R.string.industrial_commercial_search_quest_5),
            context.getString(R.string.industrial_commercial_search_quest_6),
            context.getString(R.string.industrial_commercial_search_quest_7),
            context.getString(R.string.industrial_commercial_search_quest_8),
            context.getString(R.string.industrial_commercial_search_quest_9),
            context.getString(R.string.industrial_commercial_search_quest_10),
            context.getString(R.string.industrial_commercial_search_quest_11)
        )

        fun equipmentHeaderBuild(list: List<String>): Array<String> {
            val mList = ArrayList<String>()
            var isFirst = false
            list.forEach {
                if (!isFirst) {
                    mList.add("UC")
                    isFirst = true
                }
                mList.add("$it Intervalo")
                mList.add("$it Quantidade")
            }
            return mList.toTypedArray()
        }
    }
}