package mge.mobile.pphm.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import mge.mobile.pphm.R
import mge.mobile.pphm.data.dao.SupremeDao
import mge.mobile.pphm.data.database.AppDatabase
import mge.mobile.pphm.data.model.ConsumerUnit

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class DialogCreateNewClient(context: Context, private val dismissListener: (Boolean) -> Unit) :
    Dialog(context) {

    private val database by lazy {
        AppDatabase.getInstance(context).supremeDao()
    }

    private var hasInsertion = false

    private lateinit var etConsumerUnit: EditText
    private lateinit var etMeterNumber: EditText
    private lateinit var etClientName: EditText
    private lateinit var rgClass: RadioGroup
    private lateinit var rbResidential: RadioButton
    private lateinit var rbRural: RadioButton
    private lateinit var rbCommercial: RadioButton
    private lateinit var rbIndustrial: RadioButton
    private lateinit var tvProjeto: TextView
    private var projectName = ""
    private var consumerUnit = ""
    private var clientName = ""
    private var meterNumber = ""
    private var clientClass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_create_new_client)

        etConsumerUnit = findViewById(R.id.dcnc_et_consumer_unit)
        etMeterNumber = findViewById(R.id.dcnc_et_meter_client)
        etClientName = findViewById(R.id.dcnc_et_client_name)
        rgClass = findViewById(R.id.dcnc_rg_class)
        rbResidential = findViewById(R.id.dcnc_rb_class_residential)
        rbRural = findViewById(R.id.dcnc_rb_class_rural)
        rbIndustrial = findViewById(R.id.dcnc_rb_class_industrial)
        rbCommercial = findViewById(R.id.dcnc_rb_class_commercial)
        tvProjeto = findViewById(R.id.tv_Projeto)

        etConsumerUnit.setText(consumerUnit)
        etMeterNumber.setText(meterNumber)
        etClientName.setText(clientName)
        tvProjeto.setText(projectName)
        when(clientClass) {
            "Rural" -> rbRural.isChecked = true
            "Comercial" -> rbCommercial.isChecked = true
            "Industrial" -> rbIndustrial.isChecked = true
            "Residencial" -> rbResidential.isChecked = true
        }


        setOnDismissListener { dismissListener(hasInsertion) }

        findViewById<Button>(R.id.dcnc_btn_save).setOnClickListener {
            if (validateEntries()) {
                projectName = tvProjeto.text.toString()
                consumerUnit = etConsumerUnit.text.toString()
                clientName = etClientName.text.toString()
                meterNumber = etConsumerUnit.text.toString()
                clientClass = when (rgClass.checkedRadioButtonId) {
                    R.id.dcnc_rb_class_commercial -> rbCommercial.text.toString()
                    R.id.dcnc_rb_class_rural -> rbRural.text.toString()
                    R.id.dcnc_rb_class_residential -> rbResidential.text.toString()
                    R.id.dcnc_rb_class_industrial -> rbIndustrial.text.toString()
                    else -> ""
                }
                database.insertConsumerUnit(
                    ConsumerUnit(
                        consumerClass = clientClass,
                        meter = meterNumber,
                        number = consumerUnit,
                        projectName = projectName,
                        firstPhone = "",
                        secondPhone = "",
                        status = 0,
                        clientName = clientName
                    )
                )
                hasInsertion = true
                dismiss()
            }
        }
    }



    private fun validateEntries(): Boolean {
        return when {
            etConsumerUnit.text.isNullOrEmpty() -> {
                Toast.makeText(context, "Unidade Consumidora em branco", Toast.LENGTH_SHORT).show()
                false
            }
            //etClientName.text.isNullOrEmpty() -> {
            //    Toast.makeText(context, "Nome do cliente em branco", Toast.LENGTH_SHORT).show()
            //    false
            //}
            etMeterNumber.text.isNullOrEmpty() -> {
                Toast.makeText(context, "Número do medidor em branco", Toast.LENGTH_SHORT).show()
                false
            }
            checkRadioGroup() -> {
                Toast.makeText(context, "Classe não selecionada", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    fun setData(
        projectName: String,
        consumerUnit: String,
        meterNumber: String,
        clientClass: String,
        clientName: String
    ): DialogCreateNewClient {
        this.projectName = projectName
        this.consumerUnit = consumerUnit
        this.meterNumber = meterNumber
        this.clientClass = clientClass
        this.clientName = clientName
        return this
    }

    private fun checkRadioGroup() = when (rgClass.checkedRadioButtonId) {
        R.id.dcnc_rb_class_commercial -> false
        R.id.dcnc_rb_class_rural -> false
        R.id.dcnc_rb_class_residential -> false
        R.id.dcnc_rb_class_industrial -> false
        else -> true
    }
}