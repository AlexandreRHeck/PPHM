package mge.mobile.pphm.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import mge.mobile.pphm.R

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class DialogStatusUpdate(
    context: Context,
    private val onClickListener: (Int, Dialog) -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_update_status)

        findViewById<Button>(R.id.dus_update_status).setOnClickListener {
            onClickListener(findViewById<Spinner>(R.id.dus_status_select).selectedItemPosition, this)
        }
    }

}