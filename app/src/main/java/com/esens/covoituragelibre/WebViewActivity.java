package com.esens.covoituragelibre;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {


    public static String WEB_URL_INTENT= "url";
    public static String WEB_TITLE_INTENT= "title";

    private WebView wvMainWeb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        //Récupération des infos INTENT
        Intent mIntent = getIntent(); // gets the previously created intent
        if(mIntent != null){
            this.setTitle(mIntent.getStringExtra(WEB_TITLE_INTENT));
        }



        //Init main webview content
        wvMainWeb = (WebView)this.findViewById(R.id.main_activity_webview);

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



            wvMainWeb.loadUrl(mIntent.getStringExtra(WEB_URL_INTENT));



        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;

            /*case R.id.action_refresh:
                reloadWebView();
                return true;*/
        }

        return super.onOptionsItemSelected(item);
    }

    /*
*Gestion du retour arriere avec le navigateur
*/
    private void goBack(){

        if(wvMainWeb!=null && wvMainWeb.canGoBack()){
            wvMainWeb.goBack();
        }else{
            NavUtils.navigateUpFromSameTask(this);
        }

    }

    public void reloadWebView(){

        if(wvMainWeb!=null){
            wvMainWeb.reload();
        }
    }




}
