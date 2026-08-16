package com.lewa.app.myapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

/** Hosts the web project bundled in assets/www. */
public class MainActivity extends AppCompatActivity {
  private WebView web;

  @SuppressLint("SetJavaScriptEnabled")
  @Override protected void onCreate(Bundle saved){
    super.onCreate(saved);
    web = new WebView(this);
    setContentView(web, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    WebSettings s = web.getSettings();
    s.setJavaScriptEnabled(true);
    s.setDomStorageEnabled(true);
    s.setMediaPlaybackRequiresUserGesture(false);
    s.setAllowFileAccess(false);
    s.setAllowContentAccess(false);

    // Keep navigation inside the app; hand anything else to the system.
    web.setWebViewClient(new WebViewClient(){
      @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r){
        String u = r.getUrl().toString();
        return !(u.startsWith("file:///android_asset/") || u.startsWith("about:"));
      }
    });

    getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true){
      @Override public void handleOnBackPressed(){
        if (web.canGoBack()) { web.goBack(); }
        else { setEnabled(false); getOnBackPressedDispatcher().onBackPressed(); }
      }
    });

    if (saved != null) { web.restoreState(saved); }
    else { web.loadUrl("file:///android_asset/www/index.html"); }
  }

  @Override protected void onSaveInstanceState(Bundle out){ super.onSaveInstanceState(out); web.saveState(out); }
  @Override protected void onDestroy(){ if (web != null) { web.destroy(); } super.onDestroy(); }
}
