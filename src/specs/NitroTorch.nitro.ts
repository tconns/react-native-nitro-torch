import type { HybridObject } from 'react-native-nitro-modules'

export interface NitroTorch
  extends HybridObject<{ ios: 'swift'; android: 'kotlin' }> {
  turnOn(): void
  turnOff(): void
  isOn(): boolean
  hasPermission(): boolean
  requestPermission(): Promise<boolean>
}
