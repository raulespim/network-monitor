# Secure Network Monitor

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6.0-brightgreen)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

A modern Android application for real-time network monitoring with enterprise-grade security measures.

## 🔑 Key Features

### 🛡️ Security First Approach
- **Real-time Network State Monitoring**
- **Connection Speed Analysis** (Download/Upload)
- **Advanced Error Handling** with user-friendly feedback
- **Military-grade Security Protocols**

## 🔒 Security Features

| Category | Implementation Details |
|------------|------------------------|
| **Security Implementation** | |
| **TLS 1.3** | Enforced via custom SSLContext with perfect forward secrecy |
| **Certificate Pinning** | SHA-256 pinning for all external endpoints |
| **Encrypted SharedPreferences** | AES-256 encrypted SharedPreferences for sensitive data |
| **Secure HTTP Client** | Custom OkHttpClient with restricted protocols |
| **Core Functionality** | |
| **Real-time Network Monitoring** | Continuous tracking of network connectivity |
| **Connection Speed Measurement** | Measures download/upload speeds accurately |
| **Error Handling** | Robust error management and fallback mechanisms |
| **UI State Management** | Seamless state handling for network interactions |
| **Architecture** | |
| **Clean Architecture Layers** | Separation of concerns for maintainability |
| **MVVM/MVI Pattern** | Ensures clear UI-to-business logic communication |
| **SOLID Principles** | Enhances modularity and code reusability |
| **Coroutines & Flow** | Efficient asynchronous programming model |
| **Hilt DI** | Dependency injection for better scalability |
| **Performance** | |
| **Speed Test Tracing** | Measures network performance efficiently |
| **StrictMode Integration** | Identifies potential performance issues |
| **Lifecycle-aware Components** | Ensures resource efficiency and avoids leaks |
| **Proper Threading** | Optimized threading for UI and background tasks |

## 🚀 Installation

### 1. Requirements
- Android Studio Giraffe+
- JDK 17
- Minimum Android SDK: 26
- Target Android SDK: 35

### 2. Required Permissions

`<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
`<uses-permission android:name="android.permission.INTERNET"/>`


## 🔜 Future Improvements

### 🛠️ Technical Enhancements
    - Android NDK Integration
        Native crypto operations for key management
    - Enhanced Certificate Pinning 
        Dynamic pinning configuration

`📜 License

MIT License
Copyright (c) 2025 [Raul Espim]
Permission is hereby granted...`