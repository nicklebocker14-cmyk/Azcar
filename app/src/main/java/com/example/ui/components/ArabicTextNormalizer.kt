package com.example.ui.components

object ArabicTextNormalizer {
    private val TASHKEEL_REGEX = Regex("[\\u064B-\\u065F\\u0670\\u06D6-\\u06ED]")

    fun normalize(text: String): String {
        return text
            .replace(TASHKEEL_REGEX, "") // Remove harakat/tashkeel
            .replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('آ', 'ا')
            .replace('ٱ', 'ا')
            .replace('ة', 'ه')
            .replace('ى', 'ي')
            .replace('ئ', 'ي')
            .replace('ؤ', 'و')
            .trim()
            .lowercase()
    }

    fun containsQuery(source: String, query: String): Boolean {
        if (query.isBlank()) return true
        val normalizedSource = normalize(source)
        val normalizedQuery = normalize(query)
        return normalizedSource.contains(normalizedQuery)
    }
}
