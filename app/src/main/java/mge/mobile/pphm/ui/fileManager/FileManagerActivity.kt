package mge.mobile.pphm.ui.fileManager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import mge.mobile.pphm.R
import mge.mobile.pphm.adapter.FilesAdapter
import mge.mobile.pphm.ui.main.MainActivity

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class FileManagerActivity : AppCompatActivity() {

    private lateinit var adapter: FilesAdapter
    private lateinit var list: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        list = findViewById(R.id.list_of_files)
        setAdapter()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> close()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() = close()

    override fun onResume() {
        super.onResume()
        adapter.setList(this)
    }

    private fun setAdapter() {
        adapter = FilesAdapter()
        list.adapter = adapter
        list.layoutManager = StaggeredGridLayoutManager(1, VERTICAL)
    }

    private fun close() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}