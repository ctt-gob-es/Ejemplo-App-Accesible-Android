package es.uah.cc.todomanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpActivity extends AppCompatActivity {

    private static final String BASE_URL = "file:///android_asset/html/";
    private static  final String HELP_FILE = "help.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        WebView webView = (WebView) findViewById(R.id.help_view);
        webView.loadUrl(BASE_URL + HELP_FILE);
    }
}
