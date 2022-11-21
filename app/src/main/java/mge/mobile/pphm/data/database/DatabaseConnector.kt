package mge.mobile.pphm.data.database

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import java.io.BufferedReader
import java.io.InputStreamReader
import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class DatabaseConnector {

    companion object {
        private const val LOCAL_HOST = "192.168.0.29"
        private const val REMOTE_HOST = "mgers.dyndns.org"
        private const val PORT = "3306"
        private const val DATABASE = "energisagd"
        private const val user = "SYSDBA"
        private const val password = "masterkey"

        private var URL = "jdbc:mysql://$LOCAL_HOST:$PORT/$DATABASE"
        private var connection: Connection? = null

        private fun getDriver() {
            try {
                Class.forName("com.mysql.jdbc.Driver")
            } catch (e: ClassNotFoundException) {
                Log.e("erro->", e.message.toString())
                throw e
            }
        }

        fun disconnect() {
            if (connection != null) {
                connection!!.close()
                connection = null
            }
        }

        fun connect(bConexaoInterna: Boolean, projeto: String): Connection? {
            getDriver()

            if(bConexaoInterna){
                URL = "jdbc:mysql://$LOCAL_HOST:$PORT/$projeto?useSSL=false"
            }
            else
            {
                URL = "jdbc:mysql://$REMOTE_HOST:$PORT/$projeto?useSSL=false"
            }

            try {
                if (connection == null) {
                    Log.i("test->", "Null connection, trying to create a new connection")
                    //connection = DriverManager.getConnection(URL, mountProperties())
                    connection = DriverManager.getConnection(URL, user, password)
                    Log.i("test->", "new connection created $connection")
                } else {
                    if (connection!!.isClosed) {
                        disconnect()
                        connection = DriverManager.getConnection(URL, user, password)
                    }
                }
            } catch (e: SQLException) {
                Log.i("erro->", "new connection error")
                throw e
            }

            return connection
        }

        private fun mountProperties(): Properties {
            val properties = Properties()
            properties["user"] = user
            properties["password"] = password
            return properties
        }
    }
}