package com.example.myfirebaseexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.myfirebaseexample.api.FirebaseApiAdapter
import com.example.myfirebaseexample.api.response.BarberiaResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    // Referenciar campos de las interfaz
    private lateinit var idSpinner: Spinner
    private lateinit var serviceField: EditText
    private lateinit var discountField: EditText
    private lateinit var genderField: EditText
    private lateinit var costField: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button

    // Referenciar la API
    private var firebaseApi = FirebaseApiAdapter()

    // Mantener los nombres e IDs de las armas
    private var barberiaList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        idSpinner = findViewById(R.id.idSpinner)
        serviceField = findViewById(R.id.serviceField)
        discountField = findViewById(R.id.discountField)
        genderField = findViewById(R.id.genderField)
        costField = findViewById(R.id.costField)

        buttonLoad = findViewById(R.id.buttonLoad)
        buttonLoad.setOnClickListener {
            Toast.makeText(this, "Cargando información", Toast.LENGTH_SHORT).show()
            runBlocking {
                getBarberiaFromApi()
            }
        }

        buttonSave = findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            Toast.makeText(this, "Guardando información", Toast.LENGTH_SHORT).show()
            runBlocking {
                sendBarberiaToApi()
            }
        }

        runBlocking {
            populateIdSpinner()
        }
    }

    private suspend fun populateIdSpinner() {
        val response = GlobalScope.async(Dispatchers.IO) {
            firebaseApi.getBarberias()
        }
        val barberias = response.await()
        barberias?.forEach { entry ->
            barberiaList.add("${entry.key}: ${entry.value.service}")
        }
        val barberiaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, barberiaList)
        with(idSpinner) {
            adapter = barberiaAdapter
            setSelection(0, false)
            gravity = Gravity.CENTER
        }
    }

    private suspend fun getBarberiaFromApi() {
        val selectedItem = idSpinner.selectedItem.toString()
        val barberiaId = selectedItem.subSequence(0, selectedItem.indexOf(":")).toString()
        println("Loading ${barberiaId}... ")
        val barberiaResponse = GlobalScope.async(Dispatchers.IO) {
            firebaseApi.getBarberia(barberiaId)
        }
        val barberia = barberiaResponse.await()
        serviceField.setText(barberia?.service)
        discountField.setText("${barberia?.discount}")
        genderField.setText(barberia?.gender)
        costField.setText("${barberia?.cost}")
    }

    private suspend fun sendBarberiaToApi() {
        val barberiaService = serviceField.text.toString()
        val gender = genderField.text.toString()
        val discount = discountField.text.toString().toLong()
        val cost = costField.text.toString().toLong()
        val barberia = BarberiaResponse("", barberiaService, discount, gender, cost)
        val barberiaResponse = GlobalScope.async(Dispatchers.IO) {
            firebaseApi.setBarberia(barberia)
        }
        val response = barberiaResponse.await()
        serviceField.setText(barberia?.service)
        genderField.setText(barberia?.gender)
        discountField.setText("${barberia?.discount}")
        costField.setText("${barberia?.cost}")
    }
}
