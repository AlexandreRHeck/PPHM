package mge.mobile.pphm.ui.search

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.SearchView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mge.mobile.pphm.data.model.ConsumerUnit
import mge.mobile.pphm.data.model.Equipment
import mge.mobile.pphm.data.model.Search
import mge.mobile.pphm.util.Validator

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class SearchViewModel(private val activity: Activity) : ViewModel(), LifecycleObserver {

    val consumerUnit = MutableLiveData<ConsumerUnit>()

    val equipments = MutableLiveData<List<Equipment>>()
    val search1 = MutableLiveData<Search>()
    val search2 = MutableLiveData<Search>()
    val search3 = MutableLiveData<Search>()
    val search4 = MutableLiveData<Search>()
    val search5 = MutableLiveData<Search>()
    val search6 = MutableLiveData<Search>()
    val search7 = MutableLiveData<Search>()
    val search8 = MutableLiveData<Search>()
    val search9 = MutableLiveData<Search>()
    val search10 = MutableLiveData<Search>()
    val search11 = MutableLiveData<Search>()
    val search12 = MutableLiveData<Search>()
    val search13 = MutableLiveData<Search>()
    val search14 = MutableLiveData<Search>()
    val search15 = MutableLiveData<Search>()
    val search16 = MutableLiveData<Search>()
    val search17 = MutableLiveData<Search>()
    val search18 = MutableLiveData<Search>()
    val search19 = MutableLiveData<Search>()
    val search20 = MutableLiveData<Search>()
    val search21 = MutableLiveData<Search>()
    val search22 = MutableLiveData<Search>()
    val search23 = MutableLiveData<Search>()

    fun buildSearch(number: Int, answer: String): Search {
        return Search(
            id = Validator.genId(),
            count = number,
            answer = answer,
            number = consumerUnit.value!!.number
        )
    }

    fun callPhone(phoneToCall: String) {
        if (Validator.checkPermission(Validator.CALL_PHONE_PERMISSION, activity)) {
            startActivity(activity, Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneToCall")), null)
        } else {
            Validator.requestPermission(
                activity, Validator.CALL_PHONE_PERMISSION,
                SearchActivity.CALL_PHONE_REQUEST_CODE
            )
        }
    }
}