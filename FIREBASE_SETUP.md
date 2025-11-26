# Firebase Setup Guide

## Steps to Connect Firebase to Your App

### 1. Create a Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" or select an existing project
3. Follow the setup wizard

### 2. Add Android App to Firebase
1. In Firebase Console, click "Add app" and select Android
2. Enter your package name: `com.example.gymguys`
3. Download the `google-services.json` file

### 3. Add google-services.json to Your Project
1. Copy the downloaded `google-services.json` file
2. **Replace** the existing `app/google-services.json` file (currently a placeholder)
3. Make sure the file is in: `app/` folder (same level as `build.gradle.kts`)

**⚠️ Important:** A placeholder `google-services.json` file has been created to prevent build errors. You MUST replace it with your actual file from Firebase Console for the app to work properly.

### 4. Enable Authentication in Firebase Console
1. Go to Firebase Console → Authentication
2. Click "Get started"
3. Enable "Email/Password" authentication method
4. Click "Save"

### 5. Enable Firestore (Optional - for storing user data)
1. Go to Firebase Console → Firestore Database
2. Click "Create database"
3. Start in test mode (for development)
4. Choose a location for your database

## Important Notes
- The `google-services.json` file is already configured in `build.gradle.kts`
- Firebase Authentication is already integrated in `AuthViewModel.kt`
- Sign Up and Sign In screens are connected to Firebase
- User data (fullName, email) is saved to Firestore after signup

## Testing
After setup, you can:
1. Sign up with a new email and password
2. Sign in with existing credentials
3. User data will be saved to Firestore automatically

