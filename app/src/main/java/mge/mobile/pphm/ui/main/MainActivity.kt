package mge.mobile.pphm.ui.main


import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils.split
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mysql.jdbc.StringUtils.split
import com.opencsv.CSVReader
import mge.mobile.pphm.R
import mge.mobile.pphm.adapter.ConsumerUnitAdapter
import mge.mobile.pphm.data.dao.SupremeDao
import mge.mobile.pphm.data.database.AppDatabase
import mge.mobile.pphm.data.database.CadastroDao
import mge.mobile.pphm.data.database.DatabaseConnector
import mge.mobile.pphm.data.model.ConsumerUnit
import mge.mobile.pphm.ui.dialog.DialogCreateNewClient
import mge.mobile.pphm.ui.dialog.DialogStatusUpdate
import mge.mobile.pphm.ui.fileManager.FileManagerActivity
import mge.mobile.pphm.ui.search.SearchActivity
import mge.mobile.pphm.util.Validator
import org.apache.commons.lang3.StringUtils.split
import org.jetbrains.anko.doAsync
import java.io.File
import java.io.FileDescriptor
import java.io.FileReader
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import com.mysql.jdbc.StringUtils.split
import org.apache.commons.lang3.StringUtils
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {


    companion object {
        const val FILE_SELECT_INTENT_CODE = 233
        const val EXTERNAL_PERMISSION_CODE = 322
    }

    private var actualFilter = -1
    var ProjetoNome = ""
    var UCnumber = ""
    var enviado = 0
    private lateinit var cadastroDao: CadastroDao
    private lateinit var consumerUnit: ConsumerUnit

    private lateinit var database: SupremeDao
    private lateinit var adapter: ConsumerUnitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }

        val tvVersion = findViewById<TextView>(R.id.tv_version)

        try {
            val pInfo: PackageInfo = getPackageManager().getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            tvVersion.setText("Versão " + version)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        database = AppDatabase.getInstance(this).supremeDao()
        setAdapter()

        intent.extras?.let { bundle ->
            val projectName = bundle.getString("projectName")
            ProjetoNome = projectName.toString()
            val consumerUnit = bundle.getString("consumerUnit")
            val clientName = bundle.getString("clientName")
            val meterNumber = bundle.getString("meterNumber")
            val clientClass = bundle.getString("clientClass")
            if (consumerUnit != null && clientName != null &&
                meterNumber != null && clientClass != null && projectName != null) {
                DialogCreateNewClient(this) { hasInsertion ->
                    if (hasInsertion)
                        adapter.setList(database.getAll())
                }.setData(
                    projectName=projectName,
                    consumerUnit=consumerUnit,
                    clientClass=clientClass,
                    meterNumber=meterNumber,
                    clientName=clientName
                ).show()
            }
        }

        findViewById<FloatingActionButton>(R.id.ma_fab_create_new_client).setOnClickListener {
            DialogCreateNewClient(this) { hasInsertion ->
                if (hasInsertion)
                    adapter.setList(database.getAll())
            }.show()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.setList(database.getAll())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mm_item_list_import -> callFileSelectorToImport()
            R.id.mm_item_list_banco -> EnviarBanco()
            R.id.mm_item_list_file_manager -> getFileManager()
            R.id.mm_item_list_filter -> dialogFilter()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FILE_SELECT_INTENT_CODE -> {
                val uri=data?.data
                if (uri != null) {
                    val descriptor=contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
                    if (descriptor != null) {
                        importCsv(descriptor)
                    } else {
                        toast(getString(R.string.can_not_import_csv))
                    }
                }
            }
        }
    }

    private fun getFileManager() {
        if (canRead()) {
            startActivity(Intent(this, FileManagerActivity::class.java))
            finish()
        }
    }

    private fun importCsv(descriptor: FileDescriptor) {
        val mainList = CSVReader(FileReader(descriptor)).readAll()
        database.nukeTableConsumerUnit()
        database.nukeTableEquipment()
        database.nukeTableEquipment()
        filesDir.listFiles()?.forEach { it.delete() }

        mainList.forEachIndexed { index, subList ->
            if (index > 0) {
                var number = ""
                var name = ""
                var meter = ""
                var cuClass = ""
                var firstPhone = ""
                var secondPhone = ""

                subList.forEachIndexed { subIndex, item ->
                    when (subIndex) {
                        0 -> number=item
                        1 -> name=item
                        2 -> meter=item
                        3 -> cuClass=item
                        4 -> firstPhone=item
                        5 -> secondPhone=item
                    }
                }
                database.insertConsumerUnit(
                    ConsumerUnit(
                        number=number,
                        projectName=ProjetoNome,
                        clientName=name,
                        meter=meter,
                        firstPhone=firstPhone,
                        secondPhone=secondPhone,
                        consumerClass=cuClass,
                        status=0
                    )
                )
            }
        }
        adapter.setList(database.getAll())
    }

    private fun setAdapter() {
        val list = findViewById<RecyclerView>(R.id.ma_rv_list)
        adapter = ConsumerUnitAdapter { consumerUnit ->
            if (consumerUnit.status != 4) {
                startActivity(Intent(this, SearchActivity::class.java).also {
                    it.putExtra("Projeto", ProjetoNome)
                    it.putExtra("EXTRA_ID", consumerUnit.number)
                })
            } else {
                toast(getString(R.string.pph_already_done))
            }
        }
        list.adapter = adapter
        list.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        adapter.setList(database.getAll())
    }

    private fun dialogFilter() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_consumer_unit_filter)
        val statusGroup = dialog.findViewById<RadioGroup>(R.id.dcuf_status_group)
        when (actualFilter) {
            -1 -> dialog.findViewById<RadioButton>(R.id.dcuf_status_all).isChecked=true
            0 -> dialog.findViewById<RadioButton>(R.id.dcuf_status_0).isChecked=true
            1 -> dialog.findViewById<RadioButton>(R.id.dcuf_status_1).isChecked=true
            2 -> dialog.findViewById<RadioButton>(R.id.dcuf_status_2).isChecked=true
            3 -> dialog.findViewById<RadioButton>(R.id.dcuf_status_3).isChecked=true
        }
        dialog.findViewById<Button>(R.id.dcuf_btn_filter).setOnClickListener {
            val list = when (statusGroup.checkedRadioButtonId) {
                R.id.dcuf_status_all -> {
                    actualFilter=-1
                    database.getAll()
                }
                R.id.dcuf_status_0 -> {
                    actualFilter=0
                    database.getConsumerUnitByStatus(0)
                }
                R.id.dcuf_status_1 -> {
                    actualFilter=1
                    database.getConsumerUnitByStatus(1)
                }
                R.id.dcuf_status_2 -> {
                    actualFilter=2
                    database.getConsumerUnitByStatus(2)
                }
                R.id.dcuf_status_3 -> {
                    actualFilter=3
                    database.getConsumerUnitByStatus(3)
                }
                else -> {
                    actualFilter = 99
                    emptyList()
                }
            }
            adapter.setList(list)
            dialog.dismiss()
        }
        dialog.create()
        dialog.show()
    }



    private fun canRead(): Boolean {
        return if (Validator.checkPermission(Validator.READ_STORAGE_PERMISSION, this)) {
            true
        } else {
            Validator.requestPermission(
                this,
                Validator.READ_STORAGE_PERMISSION,
                EXTERNAL_PERMISSION_CODE
            )
            false
        }
    }

    private fun callFileSelectorToImport() = AlertDialog.Builder(this)
        .setTitle(getString(R.string.alert_dialog_title_warning))
        .setMessage(getString(R.string.alert_dialog_message_import_alert))
        .setPositiveButton(getString(R.string.alert_dialog_button_yes)) { dialog, _ ->
            if (canRead()) {
                startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).also {
                    it.type="*/*"
                }, FILE_SELECT_INTENT_CODE)
            }
            dialog.dismiss()
        }.setNegativeButton(getString(R.string.alert_dialog_button_no), null)
        .create()
        .show()

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun Context.isInternalConnection(): Boolean {
        val manager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager?
        if (manager != null) {
            if (manager.isWifiEnabled) {
                if (manager.connectionInfo.ssid.contains("MGE 2G")||(manager.connectionInfo.ssid.contains(
                        "MGE 5G"
                    ))) {
                    return true
                }
            }
        }
        return false
    }


    private fun connectToDatabase(projeto: String) : Boolean {
        var bConexaoInterna = false;
        var PesquisaStatus = ""
        var resultado = false

        if(isInternalConnection()){
            bConexaoInterna = true;
        }

        doAsync {
            runOnUiThread {  }
            val connection: Connection?
            try {
                connection = DatabaseConnector.connect(bConexaoInterna, projeto)

                if (connection == null) {
                    runOnUiThread {
                    }
                } else {
                    cadastroDao = CadastroDao(connection)
                    PesquisaStatus = cadastroDao.getPesquisaStatus(UCnumber)
                    if(PesquisaStatus != "concluida") {
                        resultado = atualizaStatusPesquisa(
                            connection,
                            ProjetoNome,
                            UCnumber,
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

        var resultado = false

        try {
            cadastroDao = CadastroDao(connection)
            resultado = cadastroDao.updatePesquisaStatus(projeto, uc, status)
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
        }

        return resultado
    }

    fun EnviarBanco() {

        val split1 = "\\."
        val split2 = "/"

        val connectionManager: ConnectivityManager= this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        //database.updateConsumerStatus(consumerUnit)


        if(isConnected){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    var fileName = Environment.getExternalStorageDirectory()
                        .toString()+ "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/NAO ENVIADOS/aux.txt"

                    var file=File(fileName)

                    file.writeText("")

                    var texto = File(fileName).readText()

                    File(Environment.getExternalStorageDirectory()
                        .toString()+ "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/NAO ENVIADOS/").walkBottomUp().forEach {

                        val espaço = "$it//"


                        val arr = Pattern.compile(split2).split(espaço)


                        if (it.toString() != Environment.getExternalStorageDirectory()
                                .toString()+ "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/NAO ENVIADOS"){


                            val projeto = Pattern.compile(split1).split(arr[6].toString())

                            File(it.toString()).forEachLine{var UC = "$it"


                                if (UC != "" && UC != " "){
                                    toast (UC)
                                    ProjetoNome = projeto[0].toString()
                                    UCnumber = UC
                                    var resul=connectToDatabase(ProjetoNome)
                                }



                                if(enviado == 0){



                                }else{

                                    texto = File(fileName).readText()

                                    if (texto != "")
                                    {
                                        file.writeText(texto + "\n" + "$UC")
                                    }
                                    else{
                                        file.writeText("$UC")
                                    }


                                }

                            }
                        }
                    }

                    var fileOrigin = Environment.getExternalStorageDirectory()
                        .toString()+ "/" + Environment.DIRECTORY_DOWNLOADS.toString() + "/MGE/NAO ENVIADOS/" + ProjetoNome + ".txt"

                    texto = File(fileName).readText()

                    file=File(fileOrigin)

                    file.writeText(texto)

                    file=File(fileName)

                    file.writeText("")
                        }



            else{

                var fileName = Environment.getExternalStorageDirectory()
                    .toString() + "/MGE/NAO ENVIADOS/aux.txt"

                var file=File(fileName)

                file.writeText("")

                var texto = File(fileName).readText()

                File(Environment.getExternalStorageDirectory()
                    .toString() + "/MGE/NAO ENVIADOS/").walkBottomUp().forEach {

                    val espaço = "$it//"


                    val arr = Pattern.compile(split2).split(espaço)


                    if (it.toString() != Environment.getExternalStorageDirectory()
                            .toString() + "/MGE/NAO ENVIADOS"){


                        val projeto = Pattern.compile(split1).split(arr[6].toString())

                        File(it.toString()).forEachLine{var UC = "$it"


                            if (UC != "" && UC != " "){
                                toast (UC)
                                ProjetoNome = projeto[0].toString()
                                UCnumber = UC
                                var resul=connectToDatabase(ProjetoNome)
                            }



                            if(enviado == 0){



                            }else{

                                texto = File(fileName).readText()

                                if (texto != "")
                                {
                                    file.writeText(texto + "\n" + "$UC")
                                }
                                else{
                                    file.writeText("$UC")
                                }


                            }

                        }
                    }
                }

                var fileOrigin = Environment.getExternalStorageDirectory()
                    .toString() + "/MGE/NAO ENVIADOS/" + ProjetoNome + ".txt"

                texto = File(fileName).readText()

                file=File(fileOrigin)

                file.writeText(texto)

                file=File(fileName)

                file.writeText("")

            }

        }

        else{
            toast("Sem conexão")
        }
    }


}
