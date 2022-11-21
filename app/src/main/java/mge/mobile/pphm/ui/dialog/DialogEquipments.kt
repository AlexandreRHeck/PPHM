package mge.mobile.pphm.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import mge.mobile.pphm.R
import mge.mobile.pphm.adapter.EquipmentsAdapter
import mge.mobile.pphm.data.model.ConsumerUnit
import mge.mobile.pphm.data.model.Equipment
import mge.mobile.pphm.util.EquipmentUtil

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class DialogEquipments(
    context: Context,
    private val consumerUnit: ConsumerUnit,
    private val dismissListener: (ArrayList<Equipment>) -> Unit
) : Dialog(context) {

    private lateinit var equipmentsAdapter: EquipmentsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_set_equipments)
        recyclerView = findViewById(R.id.list_of_equipments)
        setAdapter()
        setOnDismissListener { dismissListener(equipmentsAdapter.getList()) }
        findViewById<Button>(R.id.save_list_of_equipemnts).setOnClickListener { _ -> dismiss() }
    }

    private fun setAdapter() {
        equipmentsAdapter = EquipmentsAdapter()
        recyclerView.adapter = equipmentsAdapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        equipmentsAdapter.setList(getList())
    }

    private fun getList(): ArrayList<Equipment> {
        val list = ArrayList<Equipment>()
        if (consumerUnit.consumerClass == "Residencial") {
            EquipmentUtil.listOfResidential.forEachIndexed { index, item ->
                list.add(
                    Equipment(
                        id = "${consumerUnit.number}_$index",
                        equipmentId = index,
                        equipmentName = item,
                        number = consumerUnit.number
                    )
                )
            }
        } else if (consumerUnit.consumerClass == "Rural") {
            EquipmentUtil.listOfRural.forEachIndexed { index, item ->
                list.add(
                    Equipment(
                        id = "${consumerUnit.number}_$index",
                        equipmentId = index,
                        equipmentName = item,
                        number = consumerUnit.number
                    )
                )
            }
        } else if (consumerUnit.consumerClass == "Comercial" || consumerUnit.consumerClass == "Industrial") {
            EquipmentUtil.listOfCommercialAndIndustrial.forEachIndexed { index, item ->
                list.add(
                    Equipment(
                        id = "${consumerUnit.number}_$index",
                        equipmentId = index,
                        equipmentName = item,
                        number = consumerUnit.number
                    )
                )
            }
        }
        return list
    }

}