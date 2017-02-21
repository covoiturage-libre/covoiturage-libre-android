package com.esens.covoituragelibre;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView wvMainWeb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        //Define action bar logo

        //getSupportActionBar().setIcon(R.drawable.covoituragelibre_logo);;

        ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.covoituragelibre_logo);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setIcon(R.drawable.covoituragelibre_logo);
        }


        //Init main webview content

        wvMainWeb = (WebView)this.findViewById(R.id.main_webview);

        if(wvMainWeb != null){

            wvMainWeb.setWebChromeClient(new WebChromeClient());
            WebSettings webSettings = wvMainWeb.getSettings();
            webSettings.setJavaScriptEnabled(true);



            wvMainWeb.setWebViewClient(new WebViewClient() {
                @Override

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e("WebViewClient", description);
                    new AlertDialog.Builder(view.getContext()).setMessage("Error msg").setPositiveButton("ok", null).show();

                }


                //Little trick to remove nav part
                // http://stackoverflow.com/questions/32828230/how-remove-header-from-webview-on-android
                public void onPageFinished(WebView view, String url)
                {
                    wvMainWeb.loadUrl("javascript:(function() { " +
                            "var navigation = document.getElementsByTagName('nav')[0];"
                            + "navigation.parentNode.removeChild(navigation);" +
                            "})()");
                }
                /*
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if(Uri.parse(url).getHost() != null){
                        if (Uri.parse(url).getHost().equals("devt2.esens.fr")) {
                            // This is my web site, so do not override; let my WebView load the page
                            return false;
                        }
                    }

                    // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;

                }*/
            });



            wvMainWeb.loadUrl("https://covoiturage-libre.fr/");



        }




    }
}
