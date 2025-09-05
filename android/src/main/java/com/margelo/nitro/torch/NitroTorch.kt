package com.margelo.nitro.torch

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import com.facebook.proguard.annotations.DoNotStrip
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.margelo.nitro.NitroModules
import com.margelo.nitro.core.Promise

@DoNotStrip
class NitroTorch : HybridNitroTorchSpec() {
  private val context = NitroModules.applicationContext ?: throw Exception("Context is null")

  private var flashState: Boolean = false

  override fun turnOn() {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
      val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
        cameraManager.getCameraCharacteristics(id)
          .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
      } ?: return
      cameraManager.setTorchMode(cameraId, true)
      flashState = true
    } catch (e: CameraAccessException) {
      e.printStackTrace()
    }
  }

  override fun turnOff() {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
      val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
        cameraManager.getCameraCharacteristics(id)
          .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
      } ?: return
      cameraManager.setTorchMode(cameraId, false)
      flashState = false
    } catch (e: CameraAccessException) {
      e.printStackTrace()
    }
  }

  override fun isOn(): Boolean {
    return flashState
  }

  override fun hasPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
      context, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
  }

  override fun requestPermission(): Promise<Boolean> {
    return Promise.async {
      if (hasPermission()) return@async true
      return@async false
    }
  }
}
