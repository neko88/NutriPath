package com.group35.nutripath.util

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.group35.nutripath.R
import com.group35.nutripath.databinding.ActivityMainBinding
import java.util.concurrent.Executors

/*
    BARCODE SCANNER ACTIVITY:
    references: https://developers.google.com/ml-kit/vision/barcode-scanning/android
    Launches the camera to scan and analyze for any barcodes in view.
    TODO: launch the openfoods API after barcode detected
 */
class BarcodeScannerActivity : AppCompatActivity() {
    private var camera: Camera? = null
    private lateinit var previewView: PreviewView
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)


    private val cameraResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
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
    }

    /*
    Configuration for camera for barcode scanning.
    Zoom in if barcode is detected.*/
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
                    it.setAnalyzer(Executors.newSingleThreadExecutor(), {
                        // Launch image analyzer to check for any barcodes
                        imageProxy -> processImageProxy(imageProxy)})
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this@BarcodeScannerActivity, cameraSelector, preview, imageAnalyzer)

                Log.d("BarcodeScannerActivity", "camera successfully launched.")
            } catch (e: Exception) {
                Log.d("BarcodeScannerActivity", "camera failed to launch: ${e}")
            }
        }, ContextCompat.getMainExecutor(this@BarcodeScannerActivity))
    }

    fun setZoom(zoomRatio: Float): Boolean {
        if (camera == null || camera?.cameraControl == null) return false
        camera!!.cameraControl.setZoomRatio(zoomRatio)
        return true
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy){
        val mediaImage = imageProxy.image
        if (mediaImage != null){
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanBarcodes(image).addOnCompleteListener{
                imageProxy.close()
            }
        }
    }

    /*
    Barcode Options: Barcodes of different products have different format.
    NutriPath will use:
    UPC-A: North American 12-digit barcode for standard retail products
    EAN-13: International 13-digit barcode for retail items outside NA
    QRCode */
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
                Log.e("BarcodeScannerActivity", "Scanning for barcodes...")
                for (barcode in barcodes) {
                    val bounds = barcode.boundingBox
                    val corners = barcode.cornerPoints
                    val rawValue = barcode.rawValue
                    val valueType = barcode.valueType
                    when (valueType) {
                        Barcode.FORMAT_UPC_A -> {
                            // call api
                        }

                        Barcode.FORMAT_EAN_13 -> {
                            // call api
                        }

                        Barcode.FORMAT_QR_CODE -> {
                            // TO DO?
                        }
                    }
                }
            }.addOnFailureListener { e ->
                Log.e("BarcodeScannerActivity", "Barcode scanning failed", e)
            }

    }
    private fun requestPermissions() {
        cameraResultLauncher.launch(REQUIRED_PERMISSIONS)
    }
}