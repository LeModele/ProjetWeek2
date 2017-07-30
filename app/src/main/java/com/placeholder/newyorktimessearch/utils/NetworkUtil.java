package com.placeholder.newyorktimessearch.utils;

import com.placeholder.newyorktimessearch.erreur.ErrorHandler;

import java.io.IOException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    // ERROR MESSAGES
    public static final String DEFAULT_ERROR_MESSAGE = "Oops, quelque ne vas pas .  Redemarer l'application.";
    public static final String NO_CONNECTION_ERROR_MESSAGE = "Oops, Veriffier votre connxion internet. Please try again later.";

  public static boolean isOnline() {
    Runtime runtime = Runtime.getRuntime();
    try {
      Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
      int exitValue = ipProcess.waitFor();
      return (exitValue == 0);
    } catch (IOException e) {
      ErrorHandler.handleAppException(e, "isOnLine() -- IOException");
    } catch (InterruptedException e) {
      ErrorHandler.handleAppException(e, "isOnLine() -- InterruptedException");
    }
    return false;
  }

  public static boolean checkConnexion(Context c) {
    ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
  }
}
