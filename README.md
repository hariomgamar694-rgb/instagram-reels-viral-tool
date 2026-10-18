# HR Viral AI — Create Smarter. Grow Organically.

**HR Viral AI** is a native Android application built with Kotlin and Jetpack Compose specifically engineered for Instagram creators and Reel makers. It combines AI-powered viral hook & script generation, audio trend discovery, algorithmic hashtag clustering, posting time intelligence, retention coaching, and an offline content vault.

---

## 📱 Features Overview

### 1. Features That Work Immediately (Standalone & Offline Ready)
- **Home Creator Dashboard**: Real-time trend updates, daily retention rules, niche selector (10 niches including Entertainment, Tech, Bhakti, Fitness, Finance), and quick access to all modules.
- **Bilingual Interface**: Seamless toggle between Hindi (हिन्दी - Default), Hinglish, and English across every screen.
- **Trending Audio Discovery**: Curated real Instagram audio tracks categorized by genre (Bollywood, Bhakti, Punjabi, Comedy, Lo-Fi), verification status badges (`Verified Live Data`, `Public Source`, `Admin Updated`, `AI Suggestion`), and direct one-tap links to open official Instagram audio pages.
- **Smart Hashtag Generator**: Categorizes hashtags into 4 algorithmic clusters:
  1. Broad Topic Tags (General Explore reach)
  2. Specific Niche Tags (Targeted search)
  3. Community Tags (Active engagement)
  4. Regional/Language Tags (Indian and geographic reach)
  Includes **Copy All**, **Copy Selected** (interactive chips), and **Save to Vault**.
- **AI Caption & Script Writer (Smart Algorithmic Engine & Gemini)**:
  - Scroll-stopping First-3-Seconds Hook generator
  - Formatted captions with line-breaks and engagement CTAs
  - 3-Scene structured Reel script with visual cues, on-screen text, and voiceover
  - 7-Day Content Calendar Planner
  - AI Refinement actions: *Shorten*, *Expand*, *Rewrite*, *Make Funny*, *Make Inspirational*
- **Reel Growth Coach**: Diagnostic audit tool analyzing Watch Time %, Share-to-View ratio, and Save-to-View ratio against Instagram algorithmic benchmarks.
- **Best Posting Time Recommendations**: Niche-specific morning, lunch, and prime evening slots calculated for Indian Standard Time (IST).
- **Growth Analytics Tracker**: Local Room database to log and evaluate reel performance history over time.
- **Saved Vault**: Complete local offline persistence for saved captions, scripts, hashtag sets, and audio favorites.

### 2. Features Requiring External Keys or Production Approval
- **Live Gemini 3.5 Flash Generation**: Activated by adding your `GEMINI_API_KEY` in the AI Studio **Secrets** panel. (The app automatically detects key presence and falls back to smart structured generators if not provided).
- **Instagram Insights OAuth Connection**: Labeled transparently as "Standby / General Benchmarks Active". Live account analytics require Meta Graph API approval and creator OAuth.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.2.10
- **UI Framework**: Jetpack Compose (Material Design 3 with custom Dark Navy & Purple neon palette)
- **Architecture**: MVVM + Repository Pattern with reactive Kotlin Coroutines & `StateFlow`
- **Networking**: Retrofit 2 + Moshi + OkHttp (60s timeout for generative AI)
- **Local Persistence**: Android Room Database (KSP Symbol Processing)
- **Platform**: Android API 24 (Nougat) to API 36 (Android 16)
- **Edge-to-Edge**: Full edge-to-edge support with safe window insets

---

## 🚀 Running & Testing the App

### 1. Compile the Applet
Run the compile verification tool or execute Gradle:
```bash
gradle assembleDebug
```

### 2. Run Local Unit Tests
To verify business logic (Growth Coach algorithms, hashtag clustering, posting time windows):
```bash
gradle :app:testDebugUnitTest
```

### 3. Generate a Debug APK
```bash
gradle :app:assembleDebug
# Output APK will be at: app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔑 Configuring Gemini API Key (Optional)

1. Open the **Secrets panel** in Google AI Studio.
2. Add a secret named `GEMINI_API_KEY` with your Google Gemini API key.
3. The Secrets Gradle Plugin automatically injects it into `BuildConfig.GEMINI_API_KEY` at build time without hardcoding keys in code.

---

## 🛡️ Common Error Resolutions

- **"GEMINI_API_KEY not configured"**: The app gracefully uses its smart algorithmic engine. Simply add your key in the Secrets panel to activate live generative models.
- **Audio link doesn't open**: Ensure an internet connection is available; Instagram links open directly in the browser or native Instagram app.
