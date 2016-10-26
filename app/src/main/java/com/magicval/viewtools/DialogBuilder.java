package com.magicval.viewtools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

/**
 * Handy tool for building alert dialogs in a standard manner.
 * 
 * @author Paul Gibler
 *
 */
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
