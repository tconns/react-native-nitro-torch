# react-native-nitro-torch

Native camera torch/flashlight control for React Native built with Nitro Modules.

## Overview

This module provides native-level camera torch (flashlight) control for both Android and iOS. It exposes simple JS/TS APIs to turn on/off the device's camera flash and manage camera permissions.

## Features

- 🔦 Turn camera torch on/off
- 📱 Check current torch state
- 🔐 Check and request camera permissions
- 🚀 Built with Nitro Modules for native performance and autolinking support
- 📱 Cross-platform support (iOS & Android)
- ⚡ Zero-config setup with autolinking

## Requirements

- React Native >= 0.76
- Node >= 18
- `react-native-nitro-modules` must be installed (Nitro runtime)

## Installation

```bash
npm install react-native-nitro-torch react-native-nitro-modules
# or
yarn add react-native-nitro-torch react-native-nitro-modules
```

## Configuration

### Android Setup

#### 1. AndroidManifest.xml Configuration

Add the following permissions and features to `android/app/src/main/AndroidManifest.xml`:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    
    <!-- Camera permission - Required for torch control -->
    <uses-permission android:name="android.permission.CAMERA" />
    
    <!-- Flashlight permission - Additional permission for torch -->
    <uses-permission android:name="android.permission.FLASHLIGHT" />
    
    <!-- Hardware features - Optional but recommended -->
    <uses-feature 
        android:name="android.hardware.camera" 
        android:required="false" />
    <uses-feature 
        android:name="android.hardware.camera.flash" 
        android:required="false" />
    
    <application
        android:name=".MainApplication"
        android:label="@string/app_name"
        android:icon="@mipmap/ic_launcher"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:allowBackup="false"
        android:theme="@style/AppTheme">
        <!-- Your activities -->
    </application>
    
</manifest>
```

#### 2. Runtime Permission Handling

The module automatically handles runtime permissions for Android 6.0+ (API 23+). However, you can also handle them manually:

```ts
import { PermissionsAndroid, Platform } from 'react-native'

const requestCameraPermission = async () => {
  if (Platform.OS === 'android') {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.CAMERA,
        {
          title: 'Camera Permission',
          message: 'This app needs camera access to control the flashlight',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        }
      )
      return granted === PermissionsAndroid.RESULTS.GRANTED
    } catch (err) {
      console.warn(err)
      return false
    }
  }
  return true // iOS doesn't need runtime permission request for camera
}
```

### iOS Setup

#### 1. Info.plist Configuration

Add camera usage description to `ios/YourApp/Info.plist`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <!-- Your existing keys -->
    <key>CFBundleName</key>
    <string>$(PRODUCT_NAME)</string>
    
    <!-- Camera usage description - Required for torch control -->
    <key>NSCameraUsageDescription</key>
    <string>This app needs camera access to control the flashlight</string>
    
    <!-- Alternative more detailed description -->
    <!-- 
    <key>NSCameraUsageDescription</key>
    <string>$(PRODUCT_NAME) uses the camera to control the device's flashlight/torch feature. No photos or videos are taken.</string>
    -->
    
    <!-- Your other keys -->
</dict>
</plist>
```

#### 2. Permission Request Flow

On iOS, the permission request is handled automatically when you first try to access the camera:

```ts
import { NitroTorchModule } from 'react-native-nitro-torch'

// Check current permission status
const checkPermission = () => {
  const hasPermission = NitroTorchModule.hasPermission()
  console.log('Has camera permission:', hasPermission)
  return hasPermission
}

// Request permission (will show system dialog)
const requestPermission = async () => {
  const granted = await NitroTorchModule.requestPermission()
  console.log('Permission granted:', granted)
  return granted
}
```

### Troubleshooting Configuration

#### Android Issues

**Permission not working:**

- Ensure `android:name="android.permission.CAMERA"` is correctly spelled
- Check that your app's target SDK version supports runtime permissions
- Make sure you're requesting permission before using torch

**Torch not available:**

- Add `<uses-feature android:name="android.hardware.camera.flash" android:required="false" />`
- Some emulators don't support torch - test on real device

**Build errors:**

- Clean and rebuild: `cd android && ./gradlew clean && cd .. && npx react-native run-android`

#### iOS Issues

**Permission dialog not showing:**

- Check that `NSCameraUsageDescription` exists in Info.plist
- Make sure the description string is not empty
- Clean and rebuild: `cd ios && xcodebuild clean && cd .. && npx react-native run-ios`

**Torch not working in simulator:**

- iOS Simulator doesn't have torch hardware - test on real device

**Build errors:**

- Delete derived data: `rm -rf ~/Library/Developer/Xcode/DerivedData`
- Clean build folder in Xcode: Product → Clean Build Folder

## Quick Usage

```ts
import { turnOn, turnOff, isOn, ensurePermission } from 'react-native-nitro-torch'

// Ensure permissions first
const hasPermission = await ensurePermission()
if (hasPermission) {
  // Turn on the torch
  turnOn()
  
  // Check if torch is on
  const torchIsOn = isOn()
  console.log('Torch is on:', torchIsOn)
  
  // Turn off the torch
  turnOff()
}
```

## API Reference

### Torch Control

#### `turnOn(): void`

Turns on the camera torch/flashlight.

#### `turnOff(): void`

Turns off the camera torch/flashlight.

#### `isOn(): boolean`

Returns the current torch state.

### Permission Management

#### `ensurePermission(): Promise<boolean>`

Checks and requests camera permission if needed. Returns `true` if permission is granted.

- **iOS**: Uses `AVCaptureDevice.requestAccess` for camera permission
- **Android**: Uses `PermissionsAndroid.request` for camera permission

### Advanced Usage

#### Direct module access

For advanced use cases, you can access the hybrid object directly:

```ts
import { NitroTorchModule } from 'react-native-nitro-torch'

// Check if permission is already granted
const hasPermission = NitroTorchModule.hasPermission()

// Request permission manually
const granted = await NitroTorchModule.requestPermission()

// Direct torch control
NitroTorchModule.turnOn()
NitroTorchModule.turnOff()
const state = NitroTorchModule.isOn()
```

## Basic Example

```ts
import { turnOn, turnOff, isOn, ensurePermission } from 'react-native-nitro-torch'

const toggleTorch = async () => {
  const hasPermission = await ensurePermission()
  if (!hasPermission) {
    console.log('Camera permission denied')
    return
  }
  
  if (isOn()) {
    turnOff()
  } else {
    turnOn()
  }
}
```

## Platform Support

| Feature | iOS | Android |
|---------|-----|---------|
| Turn on/off torch | ✅ | ✅ |
| Check torch state | ✅ | ✅ |
| Permission check | ✅ | ✅ |
| Permission request | ✅ | ✅ |

## Best Practices

1. **Always check permissions**: Use `ensurePermission()` before torch operations
2. **Handle errors gracefully**: Wrap torch operations in try-catch blocks
3. **Clean up**: Turn off torch when app goes to background
4. **Battery consideration**: Prolonged torch usage can drain battery quickly
5. **User experience**: Provide clear feedback about torch state to users

## Migration Notes

When updating spec files in `src/specs/*.nitro.ts`, regenerate Nitro artifacts:

```bash
npx nitro-codegen
```

## Contributing

See `CONTRIBUTING.md` for contribution workflow. Run `npx nitro-codegen` after editing spec files.

## Project Structure

- `android/` — Native Android implementation (Kotlin)
- `ios/` — Native iOS implementation (Swift)
- `src/` — TypeScript API exports
- `nitrogen/` — Generated Nitro artifacts

## Acknowledgements

Special thanks to the following open-source projects which inspired and supported the development of this library:

- [mrousavy/nitro](https://github.com/mrousavy/nitro) – for the Nitro Modules architecture and tooling

## License

MIT © [Thành Công](https://github.com/tconns)

<a href="https://www.buymeacoffee.com/tconns94" target="_blank">
  <img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" width="200"/>
</a>
