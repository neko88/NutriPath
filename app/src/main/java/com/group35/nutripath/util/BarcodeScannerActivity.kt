package com.group35.nutripath.util

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.group35.nutripath.R
import com.group35.nutripath.api.openfoodfacts.OpenFoodFactsViewModel
import com.group35.nutripath.api.openfoodfacts.OpenFoodFactsActivity
import java.util.concurrent.Executors
import android.net.Uri
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher

class BarcodeScannerActivity : AppCompatActivity() {

    private var camera: Camera? = null
    private lateinit var previewView: PreviewView
    private lateinit var foodViewModel: OpenFoodFactsViewModel
    private lateinit var selectImageButton: Button

    private val REQUIRED_PERMISSIONS = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private val permissionResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (!it.value) {
                    permissionGranted = false
                }
            }
            if (!permissionGranted) {
                Toast.makeText(
                    baseContext,
                    "Permission request denied. Please enable permissions in settings.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                launchCamera()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)

        previewView = findViewById(R.id.previewView)
        selectImageButton = findViewById(R.id.selectImageButton)
        requestPermissions()

        foodViewModel = ViewModelProvider(this).get(OpenFoodFactsViewModel::class.java)

        // for choosing gallery barcodes
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    processImageFromUri(uri)
                }
            }
        }
        // for gallery button
        selectImageButton.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    requestPermissions() // Request permission if not granted
                }
            } else {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    requestPermissions() // Request permission if not granted
                }
            }
        }

        // live data for barcode
        foodViewModel.productLiveData.observe(this, Observer { product ->
            if (product != null) {
                Toast.makeText(this, "Product: ${product.product?.product_name}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Product not found or error occurred.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun launchCamera() {
        val cameraProviderInstance = ProcessCameraProvider.getInstance(this@BarcodeScannerActivity)

        cameraProviderInstance.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderInstance.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also {
                    it.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy -> processImageProxy(imageProxy) }
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this@BarcodeScannerActivity, cameraSelector, preview, imageAnalyzer
                )
                Log.d("BarcodeScannerActivity", "camera successfully launched.")
            } catch (e: Exception) {
                Log.d("BarcodeScannerActivity", "camera failed to launch: $e")
            }
        }, ContextCompat.getMainExecutor(this@BarcodeScannerActivity))
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanBarcodes(image).addOnCompleteListener {
                imageProxy.close()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }

    private fun requestPermissions() {
        permissionResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun processImageFromUri(uri: Uri) {
        try {
            val image = InputImage.fromFilePath(this, uri)
            scanBarcodes(image)
        } catch (e: Exception) {
            Log.e("BarcodeScannerActivity", "Failed to load image from gallery: ${e.message}")
        }
    }

    private fun scanBarcodes(image: InputImage): Task<List<Barcode>> {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_QR_CODE
            ).setZoomSuggestionOptions(
                ZoomSuggestionOptions.Builder(::setZoom)
                    .setMaxSupportedZoomRatio(
                        camera?.cameraInfo?.zoomState?.value?.maxZoomRatio ?: 1.0f)
                    .build())
            .build()
        val scanner = BarcodeScanning.getClient(options)
        return scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val rawValue = barcode.rawValue
                    rawValue?.let {
                        Log.e("BarcodeScannerActivity", "Raw Value: $rawValue")
                        // Pass the barcode value to ViewModel
                        foodViewModel.findFoodByBarcode(rawValue)
                        val intent = Intent(this, OpenFoodFactsActivity::class.java)
                        intent.putExtra("barcode", rawValue)
                        startActivity(intent)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("BarcodeScannerActivity", "Barcode scanning failed", e)
            }
    }

    private fun setZoom(zoomRatio: Float): Boolean {
        return camera?.cameraControl?.setZoomRatio(zoomRatio)  != null
    }
}
