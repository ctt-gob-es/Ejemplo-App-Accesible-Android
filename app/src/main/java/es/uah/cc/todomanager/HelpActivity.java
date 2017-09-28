package es.uah.cc.todomanager;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.ResponseCache;
import java.util.Locale;

public class HelpActivity extends AppCompatActivity {

    private static final String BASE_URL = "file:///android_asset/html/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        // Loads the hel.html local file.
        WebView webView = (WebView) findViewById(R.id.help_view);
        String url = BASE_URL + getString(R.string.help_file_name);
        webView.loadUrl(url);
    }

}

