package com.esens.covoituragelibre;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WebViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {

    public static final String ARG_URL = "url";

    private String mUrl;
    private WebView wvMainWeb = null;


    private OnFragmentInteractionListener mListener;

    public WebViewFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url url of the webview
     * @return A new instance of fragment WebViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
        }


    }


    @Override
    public void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        wvMainWeb.saveState(outState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_web_view, container, false);


        wvMainWeb = (WebView)mView.findViewById(R.id.main_webview);
        if(wvMainWeb != null){


            wvMainWeb.setWebChromeClient(new WebChromeClient());
            WebSettings webSettings = wvMainWeb.getSettings();
            webSettings.setJavaScriptEnabled(true);



            wvMainWeb.setWebViewClient(new WebViewClient() {
                @Override

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e("WebViewClient", description);
                    new AlertDialog.Builder(view.getContext()).setMessage("Impossible de charger la page").setPositiveButton("ok", null).show();

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

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if(Uri.parse(url).getHost() != null){
                        if (Uri.parse(url).getHost().equals(Uri.parse(mUrl).getHost())) {

                            Intent intent = new Intent(getActivity() , WebViewActivity.class);
                            intent.putExtra(WebViewActivity.WEB_URL_INTENT, url);
                            intent.putExtra(WebViewActivity.WEB_TITLE_INTENT, "test");
                            startActivity(intent);

                            return false;
                        }
                    }

                    // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;

                }
            });



            wvMainWeb.loadUrl(mUrl);



        }


        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //TODO: See wtf is doing this methods
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
