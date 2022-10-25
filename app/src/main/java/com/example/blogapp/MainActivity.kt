package com.example.blogapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.blogapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding
private const val  REQUEST_IMAGE_CAPTURE = 1

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //consultarInformacion()
        //ingresarInformación()
        iniActionButton()

    }

    private fun iniActionButton(){
        binding.btnTakePicture.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }catch (e: ActivityNotFoundException){
            Toast.makeText(this, "No se encontro app para tomar la foto.", Toast.LENGTH_SHORT).show()
        }
    }

    private val initActivityForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "No se encontro app para tomar la foto.", Toast.LENGTH_SHORT).show()

            }
        }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(imageBitmap)
        }
    }

//    private fun uploadPicture(bitmap: Bitmap) {
//        val storageRef = FirebaseFirestore.getInstance()
//        val imagesRef =
//
//    }

    //Consultar informacion Firebase
    private fun consultarInformacion(){
        db.collection("ciudades").document("NY").addSnapshotListener { value, error ->
            value?.let { document ->
                val ciudad = document.toObject(Ciudad::class.java)
                //binding.id.text = ciudad?.color
                //binding.valor.text = ciudad?.population
                Log.d("Firebase", "DocumentSnapshot data: ${document.data}")
                Log.d("Firebase", "DocumentSnapshot data: ${ciudad?.pc}")
            }

        }
    }

    //Ingresar información
    fun ingresarInformación(){
        db.collection("ciudades").document("NY")
            .set(Ciudad("456","Red")).addOnSuccessListener {
                Log.d("Firebase", "Se guardo la ciudad correctamenta")
            }.addOnFailureListener { error ->
                Log.e("FirebaseError", error.toString())
            }
    }
}

data class Ciudad(val population: String = "",
                  val color: String = "",
                  val pc: String = "")