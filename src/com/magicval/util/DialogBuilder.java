package com.magicval.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogBuilder {
	private DialogBuilder() {}
	
	public static void showAlert(Context context, String msg) {
		showAlert(context, msg, null);
	}
	
	public static void showAlert(Context context, String msg, OnClickListener ocl) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
		dlgAlert.setMessage(msg);
		dlgAlert.setTitle("Search failed");
		dlgAlert.setPositiveButton("OK", ocl);
		dlgAlert.create().show();
	}
}
