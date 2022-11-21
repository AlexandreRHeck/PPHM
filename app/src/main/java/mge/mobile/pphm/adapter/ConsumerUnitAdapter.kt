package mge.mobile.pphm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import mge.mobile.pphm.R
import mge.mobile.pphm.data.model.ConsumerUnit
import mge.mobile.pphm.ui.main.MainActivity
import org.jetbrains.anko.find
import org.w3c.dom.Text

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class ConsumerUnitAdapter(private val onClickListener: (ConsumerUnit) -> Unit) :
    RecyclerView.Adapter<ConsumerUnitAdapter.ViewHolder>() {

    private val list = ArrayList<ConsumerUnit>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(consumerUnit: ConsumerUnit) {
            itemView.findViewById<TextView>(R.id.consumer_unit_project).text = consumerUnit.projectName
            itemView.findViewById<TextView>(R.id.consumer_unit_number).text = consumerUnit.number
            itemView.findViewById<TextView>(R.id.consumer_unit_class).text =
                consumerUnit.consumerClass
            itemView.findViewById<TextView>(R.id.consumer_unit_client_name).text =
                consumerUnit.clientName
            itemView.findViewById<TextView>(R.id.consumer_unit_status).text =
                itemView.context.resources.getStringArray(R.array.complete_status_list)[consumerUnit.status]
            itemView.setOnClickListener { onClickListener(consumerUnit) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_consumer_unit, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setList(listOfConsumerUnit: List<ConsumerUnit>) {
        list.clear()
        list.addAll(listOfConsumerUnit)
        notifyDataSetChanged()
    }

}