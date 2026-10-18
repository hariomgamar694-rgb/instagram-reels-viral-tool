package com.example.data.model

enum class VerificationStatus(val labelEn: String, val labelHi: String) {
    VERIFIED_LIVE("Verified Live Data", "सत्यापित लाइव डेटा"),
    PUBLIC_SOURCE("Public Source", "सार्वजनिक स्रोत"),
    ADMIN_UPDATED("Admin Updated", "एडमिन अपडेटेड"),
    AI_SUGGESTION("AI Suggestion", "एआई सुझाव")
}

enum class SourceType(val displayName: String) {
    INSTAGRAM_AUDIO("Instagram Audio"),
    CREATOR_COMMUNITY("Creator Community"),
    CHART_METRIC("Viral Chart"),
    ALGORITHM_ANALYSIS("Algorithm Analysis"),
    PUBLIC_SOURCE("Public Source"),
    GEMINI_AI("Gemini AI")
}

enum class ContentLanguage(val code: String, val displayName: String, val nativeName: String) {
    HINDI("hi", "Hindi", "हिन्दी"),
    ENGLISH("en", "English", "English"),
    HINGLISH("hi-en", "Hinglish", "Hinglish (मिक्स)")
}

enum class CreatorNiche(val displayNameEn: String, val displayNameHi: String) {
    ENTERTAINMENT("Entertainment / Comedy", "मनोरंजन / कॉमेडी"),
    TECH_AI("Tech & AI", "टेक और एआई"),
    EDUCATION("Education & Facts", "शिक्षा और ज्ञान"),
    FITNESS("Fitness & Health", "फिटनेस और स्वास्थ्य"),
    FASHION("Fashion & Lifestyle", "फैशन और लाइफस्टाइल"),
    FINANCE("Finance & Crypto", "फाइनेंस और निवेश"),
    DEVOTIONAL("Bhakti / Devotional", "भक्ति / आध्यात्मिक"),
    FOOD("Food & Cooking", "फूड और कुकिंग"),
    TRAVEL("Travel & Vlog", "ट्रैवल और व्लॉग"),
    MOTIVATION("Motivation & Mindset", "मोटिवेशन और सफलता")
}

enum class CaptionStyle(val displayNameEn: String, val displayNameHi: String) {
    SHORT_PUNCHY("Short & Punchy (High Retention)", "छोटा और असरदार"),
    STORYTELLING("Storytelling & Emotional", "कहानी और जुड़ाव"),
    EDUCATIONAL("Educational / Value Drop", "ज्ञानवर्धक / स्टेप्स"),
    VIRAL_CURIOSITY("Curiosity & Mystery Hook", "सस्पेंस और उत्सुकता"),
    ENGAGEMENT_CTA("Comment & Share Magnet", "कमेंट और शेयर मैग्नेट")
}

enum class SavedItemType {
    CAPTION,
    SCRIPT,
    HASHTAG_SET,
    CONTENT_PLAN,
    AUDIO_FAVORITE
}
