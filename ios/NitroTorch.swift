//
//  NitroTorch.swift
//  NitroTorch
//
//  Created by tconns94 on 8/21/2025.
//

import AVFoundation

class NitroTorch: HybridNitroTorchSpec {
  private var device: AVCaptureDevice?
  private var flashState: Bool = false

  override init() {
    super.init()
    device = AVCaptureDevice.default(for: .video)
  }

  func turnOn() {
    guard let device = device, device.hasTorch else { return }
    do {
      try device.lockForConfiguration()
      device.torchMode = .on
      device.unlockForConfiguration()
      flashState = device.torchMode == .on
    } catch {
      print("Failed to turn on torch: \(error)")
    }
  }

  func turnOff() {
    guard let device = device, device.hasTorch else { return }
    do {
      try device.lockForConfiguration()
      device.torchMode = .off
      device.unlockForConfiguration()
      flashState = device.torchMode == .on
    } catch {
      print("Failed to turn off torch: \(error)")
    }
  }

  func isOn() -> Bool {
    // Sync state with actual device state
    if let device = device, device.hasTorch {
      flashState = device.torchMode == .on
    }
    return flashState
  }

  func hasPermission() -> Bool {
    let status = AVCaptureDevice.authorizationStatus(for: .video)
    return status == .authorized
  }

  func requestPermission() async -> Bool {
    return await withCheckedContinuation { cont in
      AVCaptureDevice.requestAccess(for: .video) { granted in
        cont.resume(returning: granted)
      }
    }
  }
}
