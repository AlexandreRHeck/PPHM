package mge.mobile.pphm.ui.search

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.opencsv.CSVWriter
import mge.mobile.pphm.R
import mge.mobile.pphm.data.dao.SupremeDao
import mge.mobile.pphm.data.database.AppDatabase
import mge.mobile.pphm.data.database.CadastroDao
import mge.mobile.pphm.data.database.DatabaseConnector
import mge.mobile.pphm.data.model.ConsumerUnit
import mge.mobile.pphm.data.model.Equipment
import mge.mobile.pphm.data.model.Search
import mge.mobile.pphm.databinding.ActivitySearchBinding
import mge.mobile.pphm.ui.dialog.DialogEquipments
import mge.mobile.pphm.ui.dialog.DialogStatusUpdate
import mge.mobile.pphm.ui.main.MainActivity
import mge.mobile.pphm.util.CsvBuilderUtil
import mge.mobile.pphm.util.EquipmentUtil
import mge.mobile.pphm.util.ViewModelFactory
import org.jetbrains.anko.contentView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileWriter
import java.sql.Connection
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*
import mge.mobile.pphm.ui.dialog.DialogCreateNewClient


class SearchActivity : AppCompatActivity() {

    companion object {
        const val CALL_PHONE_REQUEST_CODE = 188
    }

    private var Class = 0
    private var Send = true
    private var ProjetoNome = ""
    private lateinit var consumerUnit: ConsumerUnit
    private val database: SupremeDao by lazy {
        AppDatabase.getInstance(this@SearchActivity).supremeDao()
    }
    private lateinit var cadastroDao: CadastroDao

    private val MY_REQUEST_WRITE_EXTERNAL_STORAGE=998

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        contentView?.let { view ->


            val binding = ActivitySearchBinding.bind(view)
            val viewModel: SearchViewModel by viewModels { ViewModelFactory(this@SearchActivity) }
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
            this.lifecycle.addObserver(viewModel)
            intent.extras?.getString("Projeto").let {
                if (it != null) {
                    ProjetoNome = it.toString()
                } else {
                    Toast.makeText(this, getString(R.string.ERROR), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                checkWriteExternalStoragePermission(this)

            }
            intent.extras?.getString("EXTRA_ID").let {
                if (it != null) {
                    consumerUnit =
                        AppDatabase.getInstance(this@SearchActivity).supremeDao().getById(it)
                    viewModel.consumerUnit.postValue(consumerUnit)
                } else {
                    Toast.makeText(this, getString(R.string.ERROR), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }

            when (consumerUnit.consumerClass) {
                "Residencial" -> setFragment(ResidentialFragment(viewModel))
                "Rural" -> setFragment(RuralFragment(viewModel))
                "Industrial" -> setFragment(CommercialIndustrialFragment(viewModel))
                "Comercial" -> setFragment(CommercialIndustrialFragment(viewModel))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed()=close()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuSearchItemEquipments=R.id.menu_search_item_equipments
        val connectionManager: ConnectivityManager=
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManager.activeNetworkInfo
        val isConnected: Boolean=activeNetwork?.isConnectedOrConnecting == true
        val viewModel: SearchViewModel by viewModels { ViewModelFactory(this) }
        when (item.itemId) {
            android.R.id.home -> close()
            menuSearchItemEquipments -> dialogEquipments()
            R.id.menu_search_item_update_status -> dialogUpdateStatus()
            R.id.menu_search_item_finish -> {

                when {
                    viewModel.equipments.value == null -> {
                        toast("Lista de equipamentos não preenchida")
                        return false
                    }
                    consumerUnit.consumerClass == "Residencial" -> {
                        when {
                            viewModel.search1.value == null -> {
                                toast("Nome Cliente não preenchido")
                                return false
                            }
                            viewModel.search2.value == null -> {
                                toast("Pergunta Titular não respondida")
                                return false
                            }
                            viewModel.search3.value == null -> {
                                toast("Telefone de contato não preenchido")
                                return false
                            }
                            viewModel.search4.value == null -> {
                                toast("Pergunta 1 não respondida")
                                return false
                            }
                            viewModel.search5.value == null -> {
                                toast("Pergunta 2 não respondida")
                                return false
                            }
                            viewModel.search6.value == null -> {
                                toast("Pergunta 3 não respondida")
                                return false
                            }
                            viewModel.search7.value == null -> {
                                toast("Pergunta 4 não respondida")
                                return false
                            }
                            viewModel.search8.value == null -> {
                                toast("Pergunta 5 não respondida")
                                return false
                            }
                            viewModel.search9.value == null -> {
                                toast("Pergunta 6 não respondida")
                                return false
                            }
                            viewModel.search10.value == null -> {
                                toast("Pergunta 7 não respondida")
                                return false
                            }
                            viewModel.search11.value == null -> {
                                toast("Pergunta 8 não respondida")
                                return false
                            }
                            else -> {
                                val dialog=AlertDialog.Builder(this@SearchActivity)
                                dialog.setTitle("Aviso")
                                dialog.setMessage("Deseja encerrar a PPH?")
                                dialog.setPositiveButton("Sim") { _, _ ->
                                    viewModel.equipments.value?.forEach {
                                        database.insertEquipment(it)
                                    }

                                    database.insertSearch(viewModel.search1.value!!)
                                    database.insertSearch(viewModel.search2.value!!)
                                    database.insertSearch(viewModel.search3.value!!)
                                    database.insertSearch(viewModel.search4.value!!)
                                    database.insertSearch(viewModel.search5.value!!)
                                    database.insertSearch(viewModel.search6.value!!)
                                    database.insertSearch(viewModel.search7.value!!)
                                    database.insertSearch(viewModel.search8.value!!)
                                    database.insertSearch(viewModel.search9.value!!)
                                    database.insertSearch(viewModel.search10.value!!)
                                    database.insertSearch(viewModel.search11.value!!)

                                    consumerUnit.status=4
                                    database.updateConsumerStatus(consumerUnit)
                                    var resul=connectToDatabase(ProjetoNome)

                                    if (isConnected) {
                                        var resul=connectToDatabase(ProjetoNome)
                                        toast("Status de pesquisa concluída atualizada no banco de dados da MGE.")
                                    }
                                    if (!isConnected) {

                                        if (createDirNaoEnviados()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                val fileName=
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_DOWNLOADS
                                                    )
                                                        .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt"

                                                var file=File(fileName)

                                                file.writeText("")

                                                var texto = File(fileName).readText()

                                                file.writeText(texto + "\n" + consumerUnit.number)

                                                toast("Pesquisa não atualizada")

                                            } else {
                                                val fileName=
                                                    Environment.getExternalStorageDirectory()
                                                        .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt"

                                                var file=File(fileName)

                                                file.writeText("")

                                                var texto = File(Environment.getExternalStorageDirectory()
                                                    .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt").readText()

                                                file.writeText(texto + "\n" + consumerUnit.number)

                                                toast("Pesquisa não atualizada")

                                            }

                                        }
                                    }
                                    Class=1
                                    exportPPHS()
                                    Class=0

                                    close()
                                }
                                dialog.setNegativeButton("Não", null)
                                dialog.show()
                                return true
                            }
                        }
                    }
                    consumerUnit.consumerClass == "Rural" -> {
                        when {
                            viewModel.search1.value == null -> {
                                toast("Nome Cliente não preenchido")
                                return false
                            }
                            viewModel.search2.value == null -> {
                                toast("Pergunta Titular não respondida")
                                return false
                            }
                            viewModel.search3.value == null -> {
                                toast("Telefone de contato não preenchido")
                                return false
                            }
                            viewModel.search4.value == null -> {
                                toast("Pergunta 1 não respondida")
                                return false
                            }
                            viewModel.search5.value == null -> {
                                toast("Pergunta 2 não respondida")
                                return false
                            }
                            viewModel.search6.value == null -> {
                                toast("Pergunta 3 não respondida")
                                return false
                            }
                            viewModel.search7.value == null -> {
                                toast("Pergunta 4 não respondida")
                                return false
                            }
                            viewModel.search8.value == null -> {
                                toast("Pergunta 5 não respondida")
                                return false
                            }
                            viewModel.search9.value == null -> {
                                toast("Pergunta 6 não respondida")
                                return false
                            }
                            viewModel.search10.value == null -> {
                                toast("Pergunta 7 não respondida")
                                return false
                            }
                            viewModel.search11.value == null -> {
                                toast("Pergunta 8 não respondida")
                                return false
                            }
                            else -> {
                                val dialog=AlertDialog.Builder(this@SearchActivity)
                                dialog.setTitle("Aviso")
                                dialog.setMessage("Deseja encerrar a PPH?")
                                dialog.setPositiveButton("Sim") { _, _ ->
                                    viewModel.equipments.value?.forEach {
                                        database.insertEquipment(it)
                                    }
                                    database.insertSearch(viewModel.search1.value!!)
                                    database.insertSearch(viewModel.search2.value!!)
                                    database.insertSearch(viewModel.search3.value!!)
                                    database.insertSearch(viewModel.search4.value!!)
                                    database.insertSearch(viewModel.search5.value!!)
                                    database.insertSearch(viewModel.search6.value!!)
                                    database.insertSearch(viewModel.search7.value!!)
                                    database.insertSearch(viewModel.search8.value!!)
                                    database.insertSearch(viewModel.search9.value!!)
                                    database.insertSearch(viewModel.search10.value!!)
                                    database.insertSearch(viewModel.search11.value!!)

                                    consumerUnit.status=4
                                    database.updateConsumerStatus(consumerUnit)
                                    if (isConnected) {
                                        var resul=connectToDatabase(ProjetoNome)
                                        toast("Status de pesquisa concluída atualizada no banco de dados da MGE.")
                                    }
                                    if (!isConnected) {

                                        if (createDirNaoEnviados()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                val fileName=
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_DOWNLOADS
                                                    )
                                                        .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt"

                                                var file=File(fileName)

                                                file.writeText("")

                                                var texto = File(fileName).readText()

                                                file.writeText(texto + "\n" + consumerUnit.number)

                                                toast("Pesquisa não atualizada")

                                            } else {
                                                val fileName=
                                                    Environment.getExternalStorageDirectory()
                                                        .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt"

                                                var file=File(fileName)

                                                file.writeText("")

                                                var texto = File(Environment.getExternalStorageDirectory()
                                                    .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt").readText()

                                                file.writeText(texto + "\n" + consumerUnit.number)

                                                toast("Pesquisa não atualizada")

                                            }

                                        }
                                    }
                                    Class=2
                                    exportPPHS()
                                    Class=0

                                    close()
                                }
                                dialog.setNegativeButton("Não", null)
                                dialog.show()
                                return true
                            }
                        }
                    }
                    consumerUnit.consumerClass == "Comercial" -> {
                        when {
                            viewModel.search1.value == null -> {
                                toast("Nome do Estabelecimento não preenchido")
                                return false
                            }
                            viewModel.search2.value == null -> {
                                toast("Pergunta Titular não respondida")
                                return false
                            }
                            viewModel.search3.value == null -> {
                                toast("Telefone de contato não preenchido")
                                return false
                            }
                            viewModel.search4.value == null -> {
                                toast("Pergunta 1 não respondida")
                                return false
                            }
                            viewModel.search5.value == null -> {
                                toast("Pergunta 2 não respondida")
                                return false
                            }
                            viewModel.search6.value == null -> {
                                toast("Pergunta 3 não respondida")
                                return false
                            }
                            viewModel.search7.value == null -> {
                                toast("Pergunta 4 não respondida")
                                return false
                            }
                            viewModel.search8.value == null -> {
                                toast("Pergunta 5 não respondida")
                                return false
                            }
                            viewModel.search9.value == null -> {
                                toast("Pergunta 6 não respondida")
                                return false
                            }
                            viewModel.search10.value == null -> {
                                toast("Pergunta 7 não respondida")
                                return false
                            }
                            viewModel.search11.value == null -> {
                                toast("Pergunta 8 não respondida")
                                return false
                            }
                            else -> {
                                val dialog=AlertDialog.Builder(this@SearchActivity)
                                dialog.setTitle("Aviso")
                                dialog.setMessage("Deseja encerrar a PPH?")
                                dialog.setPositiveButton("Sim") { _, _ ->
                                    viewModel.equipments.value?.forEach {
                                        database.insertEquipment(it)
                                    }
                                    database.insertSearch(viewModel.search1.value!!)
                                    database.insertSearch(viewModel.search2.value!!)
                                    database.insertSearch(viewModel.search3.value!!)
                                    database.insertSearch(viewModel.search4.value!!)
                                    database.insertSearch(viewModel.search5.value!!)
                                    database.insertSearch(viewModel.search6.value!!)
                                    database.insertSearch(viewModel.search7.value!!)
                                    database.insertSearch(viewModel.search8.value!!)
                                    database.insertSearch(viewModel.search9.value!!)
                                    database.insertSearch(viewModel.search10.value!!)
                                    database.insertSearch(viewModel.search11.value!!)

                                    consumerUnit.status=4
                                    database.updateConsumerStatus(consumerUnit)
                                    var resul=connectToDatabase(ProjetoNome)
                                    if (isConnected) {
                                        var resul=connectToDatabase(ProjetoNome)
                                        toast("Status de pesquisa concluída atualizada no banco de dados da MGE.")
                                    }
                                    if (!isConnected) {


                                        if (createDirNaoEnviados()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                val fileName=
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_DOWNLOADS
                                                    )
                                                        .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt"

                                                var file=File(fileName)

                                                file.writeText("")

                                                var texto = File(fileName).readText()

                                                file.writeText(texto + "\n" + consumerUnit.number)

                                                toast("Pesquisa não atualizada")

                                            } else {
                                                val fileName=
                                                    Environment.getExternalStorageDirectory()
                                                        .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt"

                                                var file=File(fileName)

                                                file.writeText("")

                                                var texto = File(Environment.getExternalStorageDirectory()
                                                    .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt").readText()

                                                file.writeText(texto + "\n" + consumerUnit.number)

                                                toast("Pesquisa não atualizada")

                                            }

                                        }
                                    }
                                    Class=3
                                    exportPPHS()
                                    Class=0

                                    close()

                                }
                                dialog.setNegativeButton("Não", null)
                                dialog.show()
                                return true
                            }
                        }
                    }
                    consumerUnit.consumerClass == "Industrial" -> {
                        when {
                            viewModel.search1.value == null -> {
                                toast("Nome Cliente não preenchido")
                                return false
                            }
                            viewModel.search2.value == null -> {
                                toast("Pergunta do titular não respondida")
                                return false
                            }
                            viewModel.search3.value == null -> {
                                toast("Telefone de contato não preenchido")
                                return false
                            }
                            viewModel.search4.value == null -> {
                                toast("Pergunta 1 não respondida")
                                return false
                            }
                            viewModel.search5.value == null -> {
                                toast("Pergunta 2 não respondida")
                                return false
                            }
                            viewModel.search6.value == null -> {
                                toast("Pergunta 3 não respondida")
                                return false
                            }
                            viewModel.search7.value == null -> {
                                toast("Pergunta 4 não respondida")
                                return false
                            }
                            viewModel.search8.value == null -> {
                                toast("Pergunta 5 não respondida")
                                return false
                            }
                            viewModel.search9.value == null -> {
                                toast("Pergunta 6 não respondida")
                                return false
                            }
                            viewModel.search10.value == null -> {
                                toast("Pergunta 7 não respondida")
                                return false
                            }
                            viewModel.search11.value == null -> {
                                toast("Pergunta 8 não respondida")
                                return false
                            }
                            else -> {
                                val dialog=AlertDialog.Builder(this@SearchActivity)
                                dialog.setTitle("Aviso")
                                dialog.setMessage("Deseja encerrar a PPH?")
                                dialog.setPositiveButton("Sim") { _, _ ->
                                    viewModel.equipments.value?.forEach {
                                        database.insertEquipment(it)
                                    }
                                    database.insertSearch(viewModel.search1.value!!)
                                    database.insertSearch(viewModel.search2.value!!)
                                    database.insertSearch(viewModel.search3.value!!)
                                    database.insertSearch(viewModel.search4.value!!)
                                    database.insertSearch(viewModel.search5.value!!)
                                    database.insertSearch(viewModel.search6.value!!)
                                    database.insertSearch(viewModel.search7.value!!)
                                    database.insertSearch(viewModel.search8.value!!)
                                    database.insertSearch(viewModel.search9.value!!)
                                    database.insertSearch(viewModel.search10.value!!)
                                    database.insertSearch(viewModel.search11.value!!)

                                    consumerUnit.status=4
                                    database.updateConsumerStatus(consumerUnit)
                                    var resul=connectToDatabase(ProjetoNome)
                                    if (isConnected) {
                                        var resul=connectToDatabase(ProjetoNome)
                                        toast("Status de pesquisa concluída atualizada no banco de dados da MGE.")
                                    }
                                    if (!isConnected) {

                                        if (createDirNaoEnviados()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                val fileName=
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_DOWNLOADS
                                                    )
                                                        .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt"

                                                var file=File(fileName)

                                                file.writeText("")

                                                file.writeText("")

                                                var texto = File(fileName).readText()

                                                file.writeText(texto + "\n" + consumerUnit.number)

                                                toast("Pesquisa não atualizada")

                                            } else {
                                                val fileName=
                                                    Environment.getExternalStorageDirectory()
                                                        .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt"

                                                var file=File(fileName)

                                                file.writeText("")

                                                var texto = File(Environment.getExternalStorageDirectory()
                                                    .toString() + "/MGE/NAO ENVIADOS/" + consumerUnit.projectName + ".txt").readText()

                                                file.writeText(texto + "\n" + consumerUnit.number)

                                                toast("Pesquisa não atualizada")

                                            }

                                        }
                                    }
                                    Class=4
                                    exportPPHS()
                                    Class=0

                                    close()
                                }
                                dialog.setNegativeButton("Não", null)
                                dialog.show()
                                return true
                            }
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun exportPPHS() {

        val root=filesDir.absoluteFile
        val date=SimpleDateFormat("HH_mm_ss_dd_MM_yyyy").format(Date()).toString()

        val listOfResidentialForPPH=database.getByClass(mClass="Residencial")
        val listOfResidentialForEquipments=
            database.getByStatusDoneAndClass(mClass="Residencial")
        val listOfRuralForPPH=database.getByClass(mClass="Rural")
        val listOfRuralForEquipments=database.getByStatusDoneAndClass(mClass="Rural")
        val listOfCommercialForPPH=database.getByClass(mClass="Comercial")
        val listOfCommercialForEquipments=database.getByStatusDoneAndClass(mClass="Comercial")
        val listOfIndustrialForPPH=database.getByClass(mClass="Industrial")
        val listOfIndustrialForEquipments=database.getByStatusDoneAndClass(mClass="Industrial")

        var somethingExported=false

        if (Class == 1) {
            if (listOfResidentialForPPH.isNotEmpty()) {
                somethingExported=true
                exportResidentialPPH(root, date, listOfResidentialForPPH)
                if (listOfResidentialForEquipments.isNotEmpty())
                    exportResidentialEquipment(root, date, listOfResidentialForEquipments)
            }
        }
        if (Class == 2) {
            if (listOfRuralForPPH.isNotEmpty()) {
                somethingExported=true
                exportRuralPPH(root, date, listOfRuralForPPH)
                if (listOfRuralForEquipments.isNotEmpty())
                    exportRuralEquipment(root, date, listOfRuralForEquipments)
            }
        }
        if (Class == 3) {
            if (listOfCommercialForPPH.isNotEmpty()) {
                somethingExported=true
                exportCommercialPPH(root, date, listOfCommercialForPPH)
                if (listOfCommercialForEquipments.isNotEmpty())
                    exportCommercialEquipment(root, date, listOfCommercialForEquipments)
            }
        }
        if (Class == 4) {
            if (listOfIndustrialForPPH.isNotEmpty()) {
                somethingExported=true
                exportIndustrialPPH(root, date, listOfIndustrialForPPH)
                if (listOfIndustrialForEquipments.isNotEmpty())
                    exportIndustrialEquipment(root, date, listOfIndustrialForEquipments)
            }
        }

        if (!somethingExported) {
            toast("Nada para exportar")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)

    private fun exportResidentialPPH(
        root: File,
        date: String,
        listOfConsumerUnit: List<ConsumerUnit>
    ) {
        val residentialPPHPath="${root.absolutePath}/residential_pph_list_$date.csv"
        val connectionManager: ConnectivityManager=
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManager.activeNetworkInfo
        val isConnected: Boolean=activeNetwork?.isConnectedOrConnecting == true

        if (root.canWrite()) {
            if (listOfConsumerUnit.isNotEmpty()) {
                if (File(residentialPPHPath).createNewFile()) {
                    val pphWriter=CSVWriter(FileWriter(residentialPPHPath))
                    pphWriter.writeNext(CsvBuilderUtil.residentialSearchHeader(context=this@SearchActivity))
                    listOfConsumerUnit.forEach {
                        pphWriter.writeNext(
                            buildListOfSearchLine(
                                it,
                                database.getListOfSearch(it.number)

                            )
                        )

                    }

                    pphWriter.flush()
                    pphWriter.close()
                    toast("PPH Residencial Exportada")

                    if (checkWriteExternalStoragePermission(this)) {
                        if (createDirPesquisa()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                File("${root.absolutePath}/residential_pph_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_residencial_pph_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("PPH Residencial movida")
                                }
                            } else {
                                File("${root.absolutePath}/residential_pph_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_residencial_pph_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("PPH Residencial movida")
                                }
                            }

                        }
                    }
                } else {
                    toast("PPH Residencial\nFalha ao criar arquivo")
                }
            } else {
                toast("PPH Residencial\nNão há nada para exportar!")
            }
        } else {
            toast("PPH Residencial\nImpossivel criar arquivo")
        }
    }

    private fun exportRuralPPH(
        root: File,
        date: String,
        listOfConsumerUnit: List<ConsumerUnit>
    ) {
        val ruralPPHPath="${root.absolutePath}/rural_pph_list_$date.csv"
        val connectionManager: ConnectivityManager=
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManager.activeNetworkInfo
        val isConnected: Boolean=activeNetwork?.isConnectedOrConnecting == true

        if (root.canWrite()) {
            if (listOfConsumerUnit.isNotEmpty()) {
                if (File(ruralPPHPath).createNewFile()) {
                    val pphWriter=CSVWriter(FileWriter(ruralPPHPath))
                    pphWriter.writeNext(CsvBuilderUtil.ruralSearchHeader(this@SearchActivity))
                    listOfConsumerUnit.forEach {
                        pphWriter.writeNext(
                            buildListOfSearchLine(
                                it,
                                database.getListOfSearch(it.number)
                            )
                        )
                    }
                    pphWriter.flush()
                    pphWriter.close()
                    toast("PPH Rural Exportada")


                    if (checkWriteExternalStoragePermission(this)) {
                        if (createDirPesquisa()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                File("${root.absolutePath}/rural_pph_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_rural_pph_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("PPH Rural movida")
                                }
                            } else {
                                File("${root.absolutePath}/rural_pph_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_rural_pph_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("PPH Rural movida")
                                }
                            }

                        }
                    }



                } else {
                    toast("PPH Rural\nFalha ao criar arquivo")
                }
            } else {
                toast("PPH Rural\nNão há nada para exportar!")
            }
        } else {
            toast("PPH Rural\nImpossivel criar arquivo")
        }
    }


    private fun exportCommercialPPH(
        root: File,
        date: String,
        listOfConsumerUnit: List<ConsumerUnit>
    ) {

        val commercialPPHPath="${root.absolutePath}/commercial_pph_list_$date.csv"
        val connectionManager: ConnectivityManager=
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManager.activeNetworkInfo
        val isConnected: Boolean=activeNetwork?.isConnectedOrConnecting == true

        if (root.canWrite()) {
            if (listOfConsumerUnit.isNotEmpty()) {
                if (File(commercialPPHPath).createNewFile()) {
                    val pphWriter=CSVWriter(FileWriter(commercialPPHPath))
                    pphWriter.writeNext(CsvBuilderUtil.commercialIndustrialHeader(this@SearchActivity))
                    listOfConsumerUnit.forEach {
                        pphWriter.writeNext(
                            buildListOfSearchLine(
                                it,
                                database.getListOfSearch(it.number)
                            )
                        )
                    }
                    pphWriter.flush()
                    pphWriter.close()
                    toast("PPH Comercial Exportada")

                    if (checkWriteExternalStoragePermission(this)) {
                        if (createDirPesquisa()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                File("${root.absolutePath}/commercial_pph_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_comercial_pph_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("PPH Comercial movida")
                                }
                            } else {
                                File("${root.absolutePath}/commercial_pph_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_comercial_pph_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("PPH Comercial movida")
                                }
                            }

                        }
                    }
                } else {
                    toast("PPH Comercial\nFalha ao criar arquivo")
                }
            } else {
                toast("PPH Comercial\nNão há nada para exportar!")
            }
        } else {
            toast("PPH Comercial\nImpossivel criar arquivo")
        }
    }

    private fun exportIndustrialPPH(
        root: File,
        date: String,
        listOfConsumerUnit: List<ConsumerUnit>
    ) {
        val industrialPPHPath="${root.absolutePath}/industrial_pph_list_$date.csv"
        val connectionManager: ConnectivityManager=
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManager.activeNetworkInfo
        val isConnected: Boolean=activeNetwork?.isConnectedOrConnecting == true

        if (root.canWrite()) {
            if (listOfConsumerUnit.isNotEmpty()) {
                if (File(industrialPPHPath).createNewFile()) {
                    val pphWriter=CSVWriter(FileWriter(industrialPPHPath))
                    pphWriter.writeNext(CsvBuilderUtil.commercialIndustrialHeader(this@SearchActivity))
                    listOfConsumerUnit.forEach {
                        pphWriter.writeNext(
                            buildListOfSearchLine(
                                it,
                                database.getListOfSearch(it.number)
                            )
                        )
                    }
                    pphWriter.flush()
                    pphWriter.close()
                    toast("PPH Industrial Exportada")

                    var resul=connectToDatabase(ProjetoNome)

                    if (checkWriteExternalStoragePermission(this)) {
                        if (createDirPesquisa()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                File("${root.absolutePath}/industrial_pph_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_industrial_pph_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("PPH Industrial movida")
                                }
                            } else {
                                File("${root.absolutePath}/industrial_pph_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_industrial_pph_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("PPH Industrial movida")
                                }
                            }

                        }
                    }
                } else {
                    toast("PPH Industrial\nFalha ao criar arquivo")
                }
            } else {
                toast("PPH Industrial\nNão há nada para exportar!")
            }
        } else {
            toast("PPH Industrial\nImpossivel criar arquivo")
        }
    }

    private fun exportResidentialEquipment(
        root: File,
        date: String,
        listOfConsumerUnit: List<ConsumerUnit>
    ) {
        val residentialEquipmentPath="${root.absolutePath}/residential_equipment_list_$date.csv"
        val connectionManager: ConnectivityManager=
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManager.activeNetworkInfo
        val isConnected: Boolean=activeNetwork?.isConnectedOrConnecting == true

        if (root.canWrite()) {
            if (listOfConsumerUnit.isNotEmpty()) {
                if (File(residentialEquipmentPath).createNewFile()) {
                    val pphWriter=CSVWriter(FileWriter(residentialEquipmentPath))
                    pphWriter.writeNext(CsvBuilderUtil.equipmentHeaderBuild(EquipmentUtil.listOfResidential))
                    listOfConsumerUnit.forEach {
                        pphWriter.writeNext(
                            buildListOfEquipmentLine(
                                it.number,
                                database.getListOfEquipments(it.number)
                            )
                        )
                    }
                    pphWriter.flush()
                    pphWriter.close()
                    toast("LE Residenical Exportada")

                    if (checkWriteExternalStoragePermission(this)) {
                        if (createDirPesquisa()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                File("${root.absolutePath}/residential_equipment_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_residencial_equipment_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("LE Residencial movida")
                                }
                            } else {
                                File("${root.absolutePath}/residential_equipment_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_residencial_equipment_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("LE Residencial movida")
                                }
                            }

                        }
                    }
                } else {
                    toast("LE Residenical\nFalha ao criar arquivo")
                }
            } else {
                toast("LE Residenical\nNada para exportar")
            }
        } else {
            toast("LE Residenical\nImpossivel criar arquivo")
        }
    }

    private fun exportRuralEquipment(
        root: File,
        date: String,
        listOfConsumerUnit: List<ConsumerUnit>
    ) {
        val ruralEquipmentPath="${root.absolutePath}/rural_equipment_list_$date.csv"
        val connectionManager: ConnectivityManager=
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManager.activeNetworkInfo
        val isConnected: Boolean=activeNetwork?.isConnectedOrConnecting == true

        if (root.canWrite()) {
            if (listOfConsumerUnit.isNotEmpty()) {
                if (File(ruralEquipmentPath).createNewFile()) {
                    val pphWriter=CSVWriter(FileWriter(ruralEquipmentPath))
                    pphWriter.writeNext(CsvBuilderUtil.equipmentHeaderBuild(EquipmentUtil.listOfRural))
                    listOfConsumerUnit.forEach {
                        pphWriter.writeNext(
                            buildListOfEquipmentLine(
                                it.number,
                                database.getListOfEquipments(it.number)
                            )
                        )
                    }
                    pphWriter.flush()
                    pphWriter.close()
                    toast("LE Rural Exportada")

                    if (checkWriteExternalStoragePermission(this)) {
                        if (createDirPesquisa()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                File("${root.absolutePath}/rural_equipment_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_rural_equipment_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("LE Rural movida")
                                }
                            } else {
                                File("${root.absolutePath}/rural_equipment_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_rural_equipment_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("LE Rural movida")
                                }
                            }

                        }
                    }
                } else {
                    toast("LE Rural\nFalha ao criar arquivo")
                }
            } else {
                toast("LE Rural\nNada para exportar")
            }
        } else {
            toast("LE Rural\nImpossivel criar arquivo")
        }
    }

    private fun exportCommercialEquipment(
        root: File,
        date: String,
        listOfConsumerUnit: List<ConsumerUnit>
    ) {
        val commercialEquipmentPath="${root.absolutePath}/commercial_equipment_list_$date.csv"
        val connectionManager: ConnectivityManager=
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManager.activeNetworkInfo
        val isConnected: Boolean=activeNetwork?.isConnectedOrConnecting == true

        if (root.canWrite()) {
            if (listOfConsumerUnit.isNotEmpty()) {
                if (File(commercialEquipmentPath).createNewFile()) {
                    val pphWriter=CSVWriter(FileWriter(commercialEquipmentPath))
                    pphWriter.writeNext(CsvBuilderUtil.equipmentHeaderBuild(EquipmentUtil.listOfCommercialAndIndustrial))
                    listOfConsumerUnit.forEach {
                        pphWriter.writeNext(
                            buildListOfEquipmentLine(
                                it.number,
                                database.getListOfEquipments(it.number)
                            )
                        )
                    }
                    pphWriter.flush()
                    pphWriter.close()
                    toast("LE Comercial Exportada")

                    if (checkWriteExternalStoragePermission(this)) {
                        if (createDirPesquisa()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                File("${root.absolutePath}/commercial_equipment_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_comercial_equipment_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("LE Comercial movida")
                                }
                            } else {
                                File("${root.absolutePath}/commercial_equipment_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_comercial_equipment_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("LE Comercial movida")
                                }
                            }

                        }
                    }
                } else {
                    toast("LE Comercial\nFalha ao criar arquivo")
                }
            } else {
                toast("LE Comercial\nNada para exportar")
            }
        } else {
            toast("LE Comercial\nImpossivel criar arquivo")
        }
    }

    private fun exportIndustrialEquipment(
        root: File,
        date: String,
        listOfConsumerUnit: List<ConsumerUnit>
    ) {
        val industrialEquipmentPath="${root.absolutePath}/industrial_equipment_list_$date.csv"
        val connectionManager: ConnectivityManager=
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManager.activeNetworkInfo
        val isConnected: Boolean=activeNetwork?.isConnectedOrConnecting == true

        if (root.canWrite()) {
            if (listOfConsumerUnit.isNotEmpty()) {
                if (File(industrialEquipmentPath).createNewFile()) {
                    val pphWriter=CSVWriter(FileWriter(industrialEquipmentPath))
                    pphWriter.writeNext(CsvBuilderUtil.equipmentHeaderBuild(EquipmentUtil.listOfCommercialAndIndustrial))
                    listOfConsumerUnit.forEach {
                        pphWriter.writeNext(
                            buildListOfEquipmentLine(
                                it.number,
                                database.getListOfEquipments(it.number)
                            )
                        )
                    }
                    pphWriter.flush()
                    pphWriter.close()
                    toast("LE Industrial Exportada")

                    if (checkWriteExternalStoragePermission(this)) {
                        if (createDirPesquisa()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                File("${root.absolutePath}/industrial_equipment_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_industrial_equipment_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("LE Industrial movida")
                                }
                            } else {
                                File("${root.absolutePath}/industrial_equipment_list_$date.csv").let { sourceFile ->
                                    sourceFile.copyTo(
                                        File(
                                            Environment.getExternalStorageDirectory()
                                                .toString() + "/MGE/PESQUISA/" + (consumerUnit.projectName).toLowerCase() + "_industrial_equipment_list_$date.csv"
                                        )
                                    )
                                    sourceFile.delete()
                                    toast("LE Industrial movida")
                                }
                            }

                        }
                    }
                } else {
                    toast("LE Industrial\nFalha ao criar arquivo")
                }
            } else {
                toast("LE Industrial\nNada para exportar")
            }
        } else {
            toast("LE Industrial\nImpossivel criar arquivo")
        }
    }

    private fun buildListOfSearchLine(
        consumerUnit: ConsumerUnit,
        listOfSearch: List<Search>
    ): Array<String> {
        val list=ArrayList<String>()
        list.add(consumerUnit.number)
        list.add(resources.getStringArray(R.array.complete_status_list)[consumerUnit.status])
        listOfSearch.forEach { search -> list.add(search.answer) }
        return list.toTypedArray()
    }

    private fun buildListOfEquipmentLine(
        number: String,
        listOfEquipments: List<Equipment>
    ): Array<String> {
        val list=ArrayList<String>()
        list.add(number)
        listOfEquipments.forEach { equipment ->
            list.add(equipment.interval)
            list.add(equipment.equipmentQuantity)
        }
        return list.toTypedArray()
    }

    private fun dialogUpdateStatus()=DialogStatusUpdate(this) { newStatus, dialog ->
        consumerUnit.status=newStatus
        database.updateConsumerStatus(consumerUnit)
        close()
        dialog.dismiss()
    }.show()

    private fun dialogEquipments()=DialogEquipments(this, consumerUnit) {
        val viewModel: SearchViewModel by viewModels { ViewModelFactory(this@SearchActivity) }
        viewModel.equipments.postValue(it)
    }.show()

    private fun close()=finish()

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.as_fragment_container, fragment).commit()
    }

    private fun Context.isInternalConnection(): Boolean {
        val manager=applicationContext.getSystemService(WIFI_SERVICE) as WifiManager?
        if (manager != null) {
            if (manager.isWifiEnabled) {
                if (manager.connectionInfo.ssid.contains("MGE") || (manager.connectionInfo.ssid.contains(
                        "STARMEASURE"
                    ))
                ) {
                    return true
                }
            }
        }
        return false
    }

    private fun connectToDatabase(projeto: String): Boolean {
        var bConexaoInterna=false;
        var PesquisaStatus=""
        var resultado=false

        if (isInternalConnection()) {
            bConexaoInterna=true;
        }

        doAsync {
            runOnUiThread { }
            val connection: Connection?
            try {
                connection=DatabaseConnector.connect(bConexaoInterna, projeto)

                if (connection == null) {
                    runOnUiThread {
                    }
                } else {
                    cadastroDao=CadastroDao(connection)
                    PesquisaStatus=cadastroDao.getPesquisaStatus(consumerUnit.number)
                    if (PesquisaStatus != "concluida") {
                        resultado=atualizaStatusPesquisa(
                            connection,
                            ProjetoNome,
                            consumerUnit.number,
                            "concluida"
                        )
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                runOnUiThread {

                }
            }
        }
        return resultado
    }

    private fun atualizaStatusPesquisa(
        connection: Connection,
        projeto: String,
        uc: String,
        status: String
    ): Boolean {

        var resultado=false

        try {
            cadastroDao=CadastroDao(connection)
            resultado=cadastroDao.updatePesquisaStatus(projeto, uc, status)
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
        }

        return resultado
    }

    fun checkWriteExternalStoragePermission(context: Context?): Boolean {
        val PermissaoEscrita=
            ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (PermissaoEscrita != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as Activity?)!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {

                AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
                    .setTitle("Permissão de escrita")
                    .setMessage("Para salvar os arquivos é necessária a permissão para acesso ao sistema de arquivos. É necessário salvar o arquivo novamente após concedida a permissão.")
                    .setPositiveButton(
                        "Eu entendi"
                    ) { dialog, which ->
                        ActivityCompat.requestPermissions(
                            (context as Activity?)!!,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                    }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_REQUEST_WRITE_EXTERNAL_STORAGE
                )

            }
            return false
        }

        return true
    }

    fun createDirPesquisa(): Boolean {
        val f: File=File(pathPesquisa())
        return if (!f.exists()) {
            f.mkdirs()
            true
        } else {
            true
        }
    }

    fun createDirNaoEnviados(): Boolean {
        val f: File=File(pathNaoEnviados())
        return if (!f.exists()) {
            f.mkdirs()
            true
        } else {
            true
        }
    }

    fun pathPesquisa(): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/MGE/PESQUISA"
        } else {
            return Environment.getExternalStorageDirectory()
                .toString() + "/MGE/PESQUISA"
        }

    }

    fun pathNaoEnviados(): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/MGE/NAO ENVIADOS"
        } else {
            return Environment.getExternalStorageDirectory()
                .toString() + "/MGE/NAO ENVIADOS"
        }
    }
}