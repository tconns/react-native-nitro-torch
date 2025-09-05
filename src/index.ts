import { PermissionsAndroid, Platform } from 'react-native'
import { NitroModules } from 'react-native-nitro-modules'
import type { NitroTorch as NitroTorchSpec } from './specs/NitroTorch.nitro'

const NitroTorchModule =
  NitroModules.createHybridObject<NitroTorchSpec>('NitroTorch')

export const turnOn = () => {
  NitroTorchModule.turnOn()
}

export const turnOff = () => {
  NitroTorchModule.turnOff()
}

export const isOn = (): boolean => {
  return NitroTorchModule.isOn()
}

export const ensurePermission = async () => {
  if (Platform.OS === 'ios') {
    const granted = await NitroTorchModule.requestPermission()
    return granted
  } else {
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.CAMERA
    )
    return granted === PermissionsAndroid.RESULTS.GRANTED
  }
}

// Export the hybrid object itself for advanced use cases
export { NitroTorchModule }
