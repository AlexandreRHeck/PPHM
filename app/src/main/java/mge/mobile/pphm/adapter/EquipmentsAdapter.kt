package mge.mobile.pphm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import mge.mobile.pphm.R
import mge.mobile.pphm.data.model.Equipment
import java.lang.StringBuilder

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class EquipmentsAdapter :
    RecyclerView.Adapter<EquipmentsAdapter.ViewHolder>() {

    private val list = ArrayList<Equipment>()

    init {
        setHasStableIds(true)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var mEquipment: Equipment
        private var mPosition: Int = -1
        private val builder = StringBuilder("000000000000000000000000")
        private fun updateBuilder(position: Int, isChecked: Boolean) {
            builder.setCharAt(position, if (isChecked) '1' else '0')
            mEquipment.interval = builder.toString()
            list[mPosition] = mEquipment
        }

        fun bind(equipment: Equipment, position: Int) {
            mEquipment =  equipment
            mPosition =  position

            itemView.findViewById<TextView>(R.id.item_equipment_name).text = equipment.equipmentName
            val cb00: CheckBox = itemView.findViewById(R.id.cb_time_picker_00)
            cb00.setOnCheckedChangeListener { _, isChecked -> updateBuilder(0, isChecked) }
            val cb01: CheckBox = itemView.findViewById(R.id.cb_time_picker_01)
            cb01.setOnCheckedChangeListener { _, isChecked -> updateBuilder(1, isChecked) }
            val cb02: CheckBox = itemView.findViewById(R.id.cb_time_picker_02)
            cb02.setOnCheckedChangeListener { _, isChecked -> updateBuilder(2, isChecked) }
            val cb03: CheckBox = itemView.findViewById(R.id.cb_time_picker_03)
            cb03.setOnCheckedChangeListener { _, isChecked -> updateBuilder(3, isChecked) }
            val cb04: CheckBox = itemView.findViewById(R.id.cb_time_picker_04)
            cb04.setOnCheckedChangeListener { _, isChecked -> updateBuilder(4, isChecked) }
            val cb05: CheckBox = itemView.findViewById(R.id.cb_time_picker_05)
            cb05.setOnCheckedChangeListener { _, isChecked -> updateBuilder(5, isChecked) }
            val cb06: CheckBox = itemView.findViewById(R.id.cb_time_picker_06)
            cb06.setOnCheckedChangeListener { _, isChecked -> updateBuilder(6, isChecked) }
            val cb07: CheckBox = itemView.findViewById(R.id.cb_time_picker_07)
            cb07.setOnCheckedChangeListener { _, isChecked -> updateBuilder(7, isChecked) }
            val cb08: CheckBox = itemView.findViewById(R.id.cb_time_picker_08)
            cb08.setOnCheckedChangeListener { _, isChecked -> updateBuilder(8, isChecked) }
            val cb09: CheckBox = itemView.findViewById(R.id.cb_time_picker_09)
            cb09.setOnCheckedChangeListener { _, isChecked -> updateBuilder(9, isChecked) }
            val cb10: CheckBox = itemView.findViewById(R.id.cb_time_picker_10)
            cb10.setOnCheckedChangeListener { _, isChecked -> updateBuilder(10, isChecked) }
            val cb11: CheckBox = itemView.findViewById(R.id.cb_time_picker_11)
            cb11.setOnCheckedChangeListener { _, isChecked -> updateBuilder(11, isChecked) }
            val cb12: CheckBox = itemView.findViewById(R.id.cb_time_picker_12)
            cb12.setOnCheckedChangeListener { _, isChecked -> updateBuilder(12, isChecked) }
            val cb13: CheckBox = itemView.findViewById(R.id.cb_time_picker_13)
            cb13.setOnCheckedChangeListener { _, isChecked -> updateBuilder(13, isChecked) }
            val cb14: CheckBox = itemView.findViewById(R.id.cb_time_picker_14)
            cb14.setOnCheckedChangeListener { _, isChecked -> updateBuilder(14, isChecked) }
            val cb15: CheckBox = itemView.findViewById(R.id.cb_time_picker_15)
            cb15.setOnCheckedChangeListener { _, isChecked -> updateBuilder(15, isChecked) }
            val cb16: CheckBox = itemView.findViewById(R.id.cb_time_picker_16)
            cb16.setOnCheckedChangeListener { _, isChecked -> updateBuilder(16, isChecked) }
            val cb17: CheckBox = itemView.findViewById(R.id.cb_time_picker_17)
            cb17.setOnCheckedChangeListener { _, isChecked -> updateBuilder(17, isChecked) }
            val cb18: CheckBox = itemView.findViewById(R.id.cb_time_picker_18)
            cb18.setOnCheckedChangeListener { _, isChecked -> updateBuilder(18, isChecked) }
            val cb19: CheckBox = itemView.findViewById(R.id.cb_time_picker_19)
            cb19.setOnCheckedChangeListener { _, isChecked -> updateBuilder(19, isChecked) }
            val cb20: CheckBox = itemView.findViewById(R.id.cb_time_picker_20)
            cb20.setOnCheckedChangeListener { _, isChecked -> updateBuilder(20, isChecked) }
            val cb21: CheckBox = itemView.findViewById(R.id.cb_time_picker_21)
            cb21.setOnCheckedChangeListener { _, isChecked -> updateBuilder(21, isChecked) }
            val cb22: CheckBox = itemView.findViewById(R.id.cb_time_picker_22)
            cb22.setOnCheckedChangeListener { _, isChecked -> updateBuilder(22, isChecked) }
            val cb23: CheckBox = itemView.findViewById(R.id.cb_time_picker_23)
            cb23.setOnCheckedChangeListener { _, isChecked -> updateBuilder(23, isChecked) }

            itemView.findViewById<CheckBox>(R.id.cb_time_picker_full_day).setOnCheckedChangeListener { _, isChecked ->
                    cb00.isChecked = isChecked
                    cb01.isChecked = isChecked
                    cb02.isChecked = isChecked
                    cb03.isChecked = isChecked
                    cb04.isChecked = isChecked
                    cb05.isChecked = isChecked
                    cb06.isChecked = isChecked
                    cb07.isChecked = isChecked
                    cb08.isChecked = isChecked
                    cb09.isChecked = isChecked
                    cb10.isChecked = isChecked
                    cb11.isChecked = isChecked
                    cb12.isChecked = isChecked
                    cb13.isChecked = isChecked
                    cb14.isChecked = isChecked
                    cb15.isChecked = isChecked
                    cb16.isChecked = isChecked
                    cb17.isChecked = isChecked
                    cb18.isChecked = isChecked
                    cb19.isChecked = isChecked
                    cb20.isChecked = isChecked
                    cb21.isChecked = isChecked
                    cb22.isChecked = isChecked
                    cb23.isChecked = isChecked
                }

            itemView.findViewById<EditText>(R.id.item_equipment_quantity).addTextChangedListener {
                mEquipment.equipmentQuantity = it.toString()
                list[mPosition] = mEquipment
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_equipment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    fun setList(listOfEquipments: List<Equipment>) {
        list.clear()
        list.addAll(listOfEquipments)
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<Equipment> = list

}