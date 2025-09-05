# 🚀 react-native-nitro-torch v0.1.0

## Initial Release

Native camera torch/flashlight control for React Native built with Nitro Modules.

## ✨ Features

- 🔦 Turn camera torch on/off
- 📱 Check current torch state  
- 🔐 Automatic permission handling
- 📱 Cross-platform support (iOS & Android)
- ⚡ Built with Nitro Modules for native performance
- 🔧 TypeScript support

## 📦 Installation

```bash
npm install react-native-nitro-torch react-native-nitro-modules
```

## 🔧 Quick Start

```ts
import { turnOn, turnOff, isOn, ensurePermission } from 'react-native-nitro-torch'

const hasPermission = await ensurePermission()
if (hasPermission) {
  turnOn()        // Turn on torch
  isOn()          // Check state
  turnOff()       // Turn off torch
}
```

## � Configuration

**Android** - Add to `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.FLASHLIGHT" />
```

**iOS** - Add to `Info.plist`:

```xml
<key>NSCameraUsageDescription</key>
<string>This app needs camera access to control the flashlight</string>
```

## 📱 Platform Support

| Feature | iOS | Android |
|---------|-----|---------|
| Torch control | ✅ | ✅ |
| Permission handling | ✅ | ✅ |

## 📄 License

MIT © [Thành Công](https://github.com/tconns)
