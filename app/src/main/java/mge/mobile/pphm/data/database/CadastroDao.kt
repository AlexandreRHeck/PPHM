package mge.mobile.pphm.data.database

import org.jetbrains.anko.doAsync
import java.sql.Connection

class CadastroDao(private val conn: Connection) {

    companion object {
        const val TABLE_NAME = "tbcadastro"
        const val COLUMN_PROJETO = "PROJETO"
        const val COLUMN_GRUPO = "GRUPO"
        const val COLUMN_POSICAO = "POSICAO"
        const val COLUMN_UC = "UC"
        const val COLUMN_CLASSE = "CLASSE"
        const val COLUMN_SUBCLASSE = "SUBCLASSE"
        const val COLUMN_NUMERO_MEDIDOR = "NUMERO_MEDIDOR"
        const val COLUMN_NOME_CONSUMIDOR = "NOME_CONSUMIDOR"
        const val COLUMN_ENDERECO_CONSUMIDOR = "ENDERECO_CONSUMIDOR"
        const val COLUMN_BAIRRO_CONSUMIDOR = "BAIRRO_CONSUMIDOR"
        const val COLUMN_CIDADE_CONSUMIDOR = "CIDADE_CONSUMIDOR"
        const val COLUMN_LATITUDE_CONSUMIDOR = "LATITUDE_CONSUMIDOR"
        const val COLUMN_LONGITUDE_CONSUMIDOR = "LONGITUDE_CONSUMIDOR"
        const val COLUMN_FASE_CONSUMIDOR = "FASE_CONSUMIDOR"
        const val COLUMN_TENSAO_CONSUMIDOR = "TENSAO_CONSUMIDOR"
        const val COLUMN_CONSUMO_MEDIO_12M_CONSUMIDOR = "CONSUMO_MEDIO_12M_CONSUMIDOR"
        const val COLUMN_PESQUISA_REALIZADA = "PESQUISA_REALIZADA"
    }

    fun updatePesquisaStatus(projeto: String, uc: String, status: String): Boolean {
        val query =
            "UPDATE $projeto.$TABLE_NAME SET $COLUMN_PESQUISA_REALIZADA = '$status' WHERE ($COLUMN_UC = '$uc');"
        val statement = conn.prepareStatement(query)
        val result = statement.executeUpdate()
        return result > 0
    }

    fun getPesquisaStatus(uc: String): String {

        val query =
            "select $COLUMN_UC, $COLUMN_PESQUISA_REALIZADA " +
                    "from $TABLE_NAME " +
                    "where $COLUMN_UC = \"$uc\""

        val statement = conn.prepareStatement(query)
        val resultSet = statement.executeQuery()
        if (resultSet != null) {
            if (resultSet.first()) {
                return resultSet.getString(COLUMN_PESQUISA_REALIZADA)
            }
        }
        return ""
    }
}