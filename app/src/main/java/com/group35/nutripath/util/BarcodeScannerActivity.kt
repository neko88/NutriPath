package com.group35.nutripath.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.group35.nutripath.R
import java.util.concurrent.Executors


class BarcodeScannerActivity : AppCompatActivity() {
    private var camera: Camera? = null
    private lateinit var previewView: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)
        previewView = findViewById(R.id.previewView)
        launchCamera()
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
                    it.setAnalyzer(Executors.newSingleThreadExecutor(), BarcodeScannerAnalyzer())
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this@BarcodeScannerActivity, cameraSelector, preview, imageAnalyzer)
            } catch (e: Exception) {
                println("BarcodeScannerActivity : Camera binding failed : ${e}")
            }
        }, ContextCompat.getMainExecutor(this@BarcodeScannerActivity))
    }

    fun setZoom(zoomRatio: Float): Boolean {
        if (camera == null || camera?.cameraControl == null) return false
        camera!!.cameraControl.setZoomRatio(zoomRatio)
        return true
    }

    private class BarcodeScannerAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            imageProxy.close()
        }
    }

    /*
    Barcode Options: Barcodes of different products have different format.
    NutriPath will use:
    UPC-A: North American 12-digit barcode for standard retail products
    EAN-13: International 13-digit barcode for retail items outside NA
    QRCode */
    private fun scanBarcodes(image: InputImage) {
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
        val scanner = BarcodeScanning.getClient()
        val result = scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val bounds = barcode.boundingBox
                    val corners = barcode.cornerPoints
                    val rawValue = barcode.rawValue
                    val valueType = barcode.valueType
                    println("Barcode Detected")
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
            }
            .addOnFailureListener(
                return
            )
    }
}