# Notes App

A modern, feature-rich notes application built with Kotlin and Jetpack Compose. The app provides a beautiful and intuitive interface for creating, managing, and organizing your notes.

## Features

### Core Functionality
- ✏️ Create and edit notes with title and content
- 🎨 Customize note colors
- 🔍 Search through notes in real-time
- ❌ Swipe to delete notes with undo option
- � Responsive and adaptive UI design

### UI/UX Features
- 🌟 Beautiful animations and transitions
  - Scale animations for notes and FAB
  - Fade and slide animations for list items
  - Smooth swipe-to-delete gesture
  - Page refresh animation on note deletion
- 🎯 Empty state indication with custom illustration
- 🔔 Toast notifications for user actions
- 📝 Material Design components

### Technical Features
- 💾 SQLite database for persistent storage
- 🏗️ MVVM architecture
- 🎯 Kotlin Coroutines for async operations
- 🎨 Jetpack Compose for modern UI
- 🔄 State management with remember and mutableState

## Tech Stack

https://github.com/user-attachments/assets/2a174c00-b1a8-498f-8263-5ca04f4a9251

![WhatsApp Image 2025-05-06 at 9 28 32 AM](https://github.com/user-attachments/assets/652b699a-286f-436e-b521-8d580cc85199)
![Home Screen](https://github.com/user-attachments/assets/f8dcd2f9-a82c-4bb1-b69c-879f1565b904)
![Home Screen Empty](https://github.com/user-attachments/assets/eeabbf00-30ac-4a01-a2c4-ca1da54d53ff)
![Editor](https://github.com/user-attachments/assets/549262d9-5fe4-461d-9a4a-aa0c0e0f4dd7)


- **Language**: Kotlin
- **Minimum SDK**: 21
- **Target SDK**: 33
- **UI Framework**: Jetpack Compose
- **Database**: SQLite
- **Architecture**: MVVM

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 8 or newer
- Android SDK with minimum API 21

### Installation
1. Clone the repository:
```bash
git clone https://github.com/yourusername/Notes_App.git
```

2. Open the project in Android Studio

3. Sync project with Gradle files

4. Run the app on an emulator or physical device

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/notes_app/
│   │   │   ├── MainActivity.kt
│   │   │   ├── Note.kt
│   │   │   └── NotesDatabase.kt
│   │   └── res/
│   │       ├── drawable/
│   │       │   └── empty_notes.xml
│   │       └── values/
│   │           └── colors.xml
│   └── androidTest/
└── build.gradle
```

## Contributing

https://github.com/user-attachments/assets/f273a251-0af2-4ed1-b9e2-6d0398b3a231



Feel free to submit issues and enhancement requests!

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Material Design for design guidelines
- Jetpack Compose for modern Android UI toolkit
- SQLite for local database management
![WhatsApp Image 2025-05-06 at 9 28 32 AM](https://github.com/user-attachments/assets/f266795a-5183-44ae-a1b8-2c966b227fbf)
