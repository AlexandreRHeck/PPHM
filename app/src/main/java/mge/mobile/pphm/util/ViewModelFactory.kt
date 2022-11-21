package mge.mobile.pphm.util

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mge.mobile.pphm.ui.search.SearchViewModel
import java.lang.IllegalArgumentException

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class ViewModelFactory(private val activity: Activity) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = with(modelClass) {
        when {
            isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(activity)
            }
            else -> {
                throw IllegalArgumentException("Unknown Class")
            }
        }
    } as T

}