package app.tabless

import java.net.URLEncoder

class URLBuilder {
    private val searchURLBase = "https://www.google.com/search?q="

    /// Whether input text should be treated as a web URL
    fun shouldTreatAsWebURL(text: String): Boolean {
        return canTreatAsWebURL(text) && text.contains(".")
    }

    /// If text looks like a URL, creates that URL
    /// Otherwise, treats it like a search query and returns a search URL
    fun createURLString(text: String): String {
        if (shouldTreatAsWebURL(text)) {
            val webURL = createWebURL(text)
            if (webURL != null) {
                return webURL
            }
        }
        return createSearchURL(text)
    }

    private fun createWebURL(text: String): String? {
        return asWebURL(text)
    }

    /// Whether a valid web URL can be created from the string
    private fun canTreatAsWebURL(text: String): Boolean {
        return asWebURL(text) != null
    }

    /// Representation of the string as a web URL
    private fun asWebURL(text: String): String? {
        val urlString: String
        if (!text.startsWith("http")) {
            urlString = "https://${text}"
        } else {
            urlString = text
        }
        return urlString
    }

    private fun createSearchURL(text: String): String {
        val searchQuery = URLEncoder.encode(text, "UTF-8")
        return "${searchURLBase}${searchQuery}"
    }
}