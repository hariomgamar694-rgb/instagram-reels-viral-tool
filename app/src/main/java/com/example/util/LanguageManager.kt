package com.example.util

import com.example.data.model.ContentLanguage

object LanguageManager {

    fun getTitle(key: String, lang: ContentLanguage): String {
        val isHindi = lang == ContentLanguage.HINDI
        return when (key) {
            "app_name" -> "HR Viral AI"
            "tagline" -> if (isHindi) "स्मार्ट बनाएं। ऑर्गेनिक रूप से बढ़ें।" else "Create Smarter. Grow Organically."
            "nav_home" -> if (isHindi) "होम" else "Home"
            "nav_audio" -> if (isHindi) "ऑडियो" else "Audio"
            "nav_hashtags" -> if (isHindi) "हैशटैग्स" else "Hashtags"
            "nav_ai_writer" -> if (isHindi) "एआई राइटर" else "AI Writer"
            "nav_coach" -> if (isHindi) "ग्रोथ कोच" else "Coach"
            "nav_timing" -> if (isHindi) "बेस्ट टाइम" else "Best Time"
            "nav_analytics" -> if (isHindi) "एनालिटिक्स" else "Analytics"
            "nav_saved" -> if (isHindi) "सेव्ड" else "Saved"
            "nav_profile" -> if (isHindi) "प्रोफाइल" else "Profile"
            
            "section_trend_updates" -> if (isHindi) "आज के ट्रेंड अपडेट्स" else "Today's Trend Updates"
            "section_trending_audio" -> if (isHindi) "ट्रेंडिंग ऑडियो डिस्कवरी" else "Trending Audio Discovery"
            "section_hashtag_gen" -> if (isHindi) "स्मार्ट हैशटैग जनरेटर" else "Smart Hashtag Generator"
            "section_ai_captions" -> if (isHindi) "एआई कैप्शन और स्क्रिप्ट राइटर" else "AI Caption & Script Writer"
            "section_growth_coach" -> if (isHindi) "रील ग्रोथ कोच व ऑडिट" else "Reel Growth Coach & Audit"
            "section_posting_time" -> if (isHindi) "पोस्ट करने का सबसे सही समय" else "Best Posting Time Recommendations"
            "section_analytics" -> if (isHindi) "रील परफॉर्मेंस ट्रैकर" else "Reel Performance Tracker"
            "section_saved" -> if (isHindi) "सेव्ड कंटेंट वॉल्ट" else "Saved Content Vault"
            "section_settings" -> if (isHindi) "प्रोफाइल और सेटिंग्स" else "Profile & Settings"

            "btn_generate" -> if (isHindi) "एआई से जनरेट करें" else "Generate with AI"
            "btn_copy_all" -> if (isHindi) "सभी कॉपी करें" else "Copy All"
            "btn_copy" -> if (isHindi) "कॉपी करें" else "Copy"
            "btn_saved" -> if (isHindi) "सेव किया गया" else "Saved"
            "btn_save" -> if (isHindi) "वॉल्ट में सेव करें" else "Save to Vault"
            "btn_regenerate" -> if (isHindi) "दोबारा जनरेट करें" else "Regenerate"
            "btn_retry" -> if (isHindi) "पुनः प्रयास करें" else "Retry"
            "btn_refresh" -> if (isHindi) "रिफ्रेश करें" else "Refresh"
            "btn_open_insta" -> if (isHindi) "इंस्टाग्राम पर चेक करें" else "Check on Instagram"

            "label_niche" -> if (isHindi) "क्रिएटर नीश" else "Creator Niche"
            "label_topic" -> if (isHindi) "रील का टॉपिक / विषय" else "Reel Topic / Title"
            "label_language" -> if (isHindi) "भाषा" else "Language"
            "label_style" -> if (isHindi) "कैप्शन स्टाइल" else "Caption Style"
            "label_audience" -> if (isHindi) "टारगेट ऑडियंस" else "Target Audience"
            
            else -> key
        }
    }
}
