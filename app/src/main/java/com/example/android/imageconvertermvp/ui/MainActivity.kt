package com.example.android.imageconvertermvp.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.android.imageconvertermvp.databinding.ActivityMainBinding
import com.example.android.imageconvertermvp.presenter.MainPresenter
import com.example.android.imageconvertermvp.view.MainView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    var dialog: Dialog? = null
    private val presenter by moxyPresenter {
        MainPresenter(
            Converter(this),
            AndroidSchedulers.mainThread()
        )
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.convert.setOnClickListener { presenter.startConversion() }

    }

    override fun startConversion() {

        val mimeTypes = arrayOf("image/*")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
            if (mimeTypes.isNotEmpty()) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        } else {
            var mimeTypesStr = ""
            for (mimeType in mimeTypes) {
                mimeTypesStr += "$mimeType|"
            }
            intent.type = mimeTypesStr.substring(0, mimeTypesStr.length - 1)
        }
        resultLauncher.launch(intent)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let {
                    presenter.convertImage(it.toString())
                }
            }
        }

    override fun showDialog() {
        dialog = AlertDialog.Builder(this).setTitle("Conversion...")
            .setNegativeButton("Cancel") { dialog, _ ->
                presenter.dismiss()
            }.create()
        dialog?.show()
    }

    override fun hideDialog() {
        dialog?.dismiss()
    }

    override fun showResult() {
        Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
    }

    override fun showError() {
        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()

    }

    override fun showInterrupted() {
        Toast.makeText(this, "Stopped!", Toast.LENGTH_LONG).show()
    }

}