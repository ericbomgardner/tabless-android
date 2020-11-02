package app.tabless

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var searchField: EditText? = null
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchField = findViewById<EditText>(R.id.search_field)
        this.searchField = searchField
        searchField.setOnEditorActionListener { _: TextView?, actionId: Int, event: KeyEvent? ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    loadSearch()
                    true
                }
                else -> false
            }
        }

        val webView = findViewById<WebView>(R.id.web_view)
        this.webView = webView

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
    }

    override fun onBackPressed() {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else if (webView?.visibility == View.VISIBLE) {
            webView?.visibility = View.INVISIBLE
            webView?.stopLoading()
            webView?.clearHistory()

            if (searchField?.requestFocus() == true) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT)
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun loadSearch() {
        val searchText = searchField?.text.toString().takeIf { it.length > 0 } ?: return

        // Hide keyboard
        val inputMethodManager =
            applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(searchField?.windowToken, 0)

        // Show and prepare web view
        webView?.visibility = View.VISIBLE
        val urlString = URLBuilder().createURLString(searchText)
        webView?.loadUrl(urlString)

        // Clear search field's text
        searchField?.text?.clear()
    }
}
