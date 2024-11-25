package com.group35.nutripath.util

import android.Manifest
import android.content.Intent
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
import com.group35.nutripath.api.openfoodfacts.ProductInfoActivity
import java.util.concurrent.Executors

class BarcodeScannerActivity : AppCompatActivity() {
    private var camera: Camera? = null
    private lateinit var previewView: PreviewView
    private lateinit var foodViewModel: OpenFoodFactsViewModel
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private val cameraResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    baseContext,
                    "Permission request denied",
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
        requestPermissions()

        // Initialize the ViewModel
        foodViewModel = ViewModelProvider(this).get(OpenFoodFactsViewModel::class.java)

        // Observe productLiveData from ViewModel
        foodViewModel.productLiveData.observe(this, Observer { product ->
            if (product != null) {
                Toast.makeText(this, "Product: ${product.product?.product_name}", Toast.LENGTH_LONG).show()
                // Update UI with product details
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
    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanBarcodes(image).addOnCompleteListener {
                imageProxy.close()
            }
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
                        camera?.cameraInfo?.zoomState?.value?.maxZoomRatio ?: 1.0f
                    )
                    .build()
            )
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

                        // Start ProductInfoActivity to show product details
                        val intent = Intent(this, ProductInfoActivity::class.java)
                        intent.putExtra("barcode", rawValue)
                        startActivity(intent)
                    }
                }
            }

            /*
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val rawValue = barcode.rawValue
                    rawValue?.let {
                        Log.e("BarcodeScannerActivity", "Raw Value: $rawValue")
                        // Pass the barcode value to ViewModel
                        foodViewModel.findFoodByBarcode(rawValue)
                    }
                }
            }*/
            .addOnFailureListener { e ->
                Log.e("BarcodeScannerActivity", "Barcode scanning failed", e)
            }
    }

    private fun requestPermissions() {
        cameraResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun setZoom(zoomRatio: Float): Boolean {
        return camera?.cameraControl?.setZoomRatio(zoomRatio) != null
    }
}
