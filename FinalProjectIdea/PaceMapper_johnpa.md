# PaceMapper
John Paul Aglubat
\
May 20th 2025

## Brief Description

**PaceMapper** is to be a run tracking app designed to help users monitor their physical activity in real-time. By leveraging device sensors and Bluetooth-connected heart rate monitors, it provides detailed statistics such as current pace, steps taken, distance traveled, and heart rate. The app maps the user’s running route using GPS and offers graphical insights into their performance over time.

## Covered Topics

- **APIs:**
  - Google Maps SDK for Android (for live location tracking and route display)
  - MPAndroidChart (for rendering pace and heart rate graphs)

- **Device Sensors:**
  - Accelerometer (to assist in detecting movement and refining step detection)
  - Step Counter (Pedometer)
  - GPS (for geolocation and mapping)

- **External Sensor and Bluetooth:**
  - Bluetooth Low Energy (BLE) integration for a heart rate monitor (e.g., Fitbit)

## Detailed Scope Outline

### Core Features

- **Start/Stop Run Session:** Main interface button to begin and end a run.
- **Real-Time Metrics:**
  - Live tracking of pace, distance, and steps.
  - Heart rate from an external BLE sensor.
- **Live Route Display:** User’s run is tracked on a map.
- **Data Visualization:**
  - Pace over time
  - Heart rate over time

### APIs and Tools Used

- Google Maps SDK – For map display and route drawing.
- FusedLocationProviderClient (Android Location API) – For optimized GPS tracking.
- SensorManager – For step detection and accelerometer readings.
- BluetoothAdapter/BluetoothGatt – For managing BLE heart rate connections.
- MPAndroidChart – To render time-series graphs of pace and heart rate.

## User Interface Description

### Main Layout

- **Bottom navigation bar** with three tabs:
  1. **Active Sensor Tab:** Displays live statistics (pace, steps, heart rate, distance).
  2. **Map Tab:** Shows the user’s location and running path in real-time.
  3. **Graph Tab:** Displays real-time or post-run graphs for pace and heart rate over time.

### Design Considerations

- Clean and minimal UI with bold metrics.
- Color-coded indicators for heart rate zones.
- Real-time updates with smooth UI transitions.

### User Feedback

- Progress updates during run (toasts).
- Post-run summary screen with total distance, average pace, and graphical breakdowns.
- Error messages or warnings if GPS signal is lost or BLE sensor disconnects.

## Technical Challenges and Potential Solutions

| Challenge | Potential Solution |
|----------|--------------------|
| **Emulating Movement in Android Emulator** | Use Android Studio’s **Extended Controls > Location > Route playback** to simulate running paths. For step counting, **mock sensor data** via developer tools or add a debug mode with manual step input. |
| **BLE Integration Complexity** | Utilize Android’s BLE libraries and test with simulated HRM data or BLE test devices. Use Fitbit-compatible simulators or switch to generic BLE devices. |
| **Real-time Graph Updating** | Use efficient libraries like **MPAndroidChart** and avoid frequent redraws. Use buffering techniques for data plotting every few seconds rather than every millisecond. |
| **UI Responsiveness During Run** | Leverage Kotlin coroutines or background services for sensor data collection to prevent UI thread blocking. |

---

