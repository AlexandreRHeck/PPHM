package mge.mobile.pphm.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import mge.mobile.pphm.R
import mge.mobile.pphm.util.Consts
import org.jetbrains.anko.toast
import java.io.File
import java.text.FieldPosition

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class FilesAdapter : RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    private val listOfFiles = ArrayList<File>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(file: File, position: Int) {
            itemView.findViewById<TextView>(R.id.item_file_name).text = file.name
            itemView.findViewById<TextView>(R.id.item_file_delete).setOnClickListener { view ->
                AlertDialog.Builder(view.context)
                    .setTitle(view.context.getString(R.string.alert_dialog_title_warning))
                    .setMessage(view.context.getString(R.string.alert_dialog_message_delete_file))
                    .setPositiveButton(view.context.getString(R.string.alert_dialog_button_yes)) { dialog, _ ->
                        if (file.delete()) {
                            notifyItemRemoved(position)
                            listOfFiles.remove(file)
                            dialog.dismiss()
                        } else {
                            view.context.toast("Falha ao excluir arquivo!")
                        }
                    }.setNegativeButton(view.context.getString(R.string.alert_dialog_button_no), null)
                    .create()
                    .show()
            }
            itemView.findViewById<TextView>(R.id.item_file_share).setOnClickListener { view ->
                view.context.startActivity(Intent(Intent.ACTION_SEND).also { intent ->
                    intent.putExtra(Intent.EXTRA_SUBJECT, file.name)
                    intent.type = "file/*"
                    intent.putExtra(
                        Intent.EXTRA_STREAM,
                        FileProvider.getUriForFile(view.context, Consts.PROVIDER, file)
                    )
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listOfFiles[position], position)
    }

    override fun getItemCount(): Int = listOfFiles.size

    fun setList(context: Context) = context.filesDir.listFiles()?.let {
        listOfFiles.clear()
        listOfFiles.addAll(it)
        notifyDataSetChanged()
    }

}