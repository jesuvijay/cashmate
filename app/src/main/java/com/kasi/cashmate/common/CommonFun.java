package com.kasi.cashmate.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kasi.cashmate.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author kasi
 */
public class CommonFun {

	public static String date_time_pattern = "yyyy-MM-dd HH:mm:ss";
	public static String date_pattern = "dd-MM-yyyy";
	public static String time_pattern = "HH:mm:ss";

	public static int str2Int(String arg) {
		int value = 0;
		try {
			value = Integer.parseInt(arg.trim());
		} catch (Exception ex) {
		}

		return value;
	}

	public static float str2Float(String arg) {
		float value = 0;
		try {
			value = Float.parseFloat(arg.trim());
		} catch (Exception ex) {
		}

		return value;
	}

	public static void hideKeyboard(Activity activity) {
		// Check if no view has focus:
		try {
			View view = activity.getCurrentFocus();
			if (view != null) {
				InputMethodManager inputManager = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(view.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception ex) {
		}
	}

	public static String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				date_time_pattern, Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getFormattedDate (String _date, String _format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(_format);
		Date date = null;
		try {
			date = new SimpleDateFormat(date_time_pattern).parse(_date);
			return dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String currencyFormat(Context context, float amount) {
//		DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
//		String formattedNumber = formatter.format(Double.parseDouble(amount));

		Locale indiaLocale = new Locale("en", "IN");
		String str = NumberFormat.getCurrencyInstance(indiaLocale).format(amount);
		str = str.replaceAll("Rs.", context.getResources().getString(R.string.Rs));

		return str;
	}

	public static String currencyValue(Context context, float amount) {
		DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
		String str = formatter.format(amount);

		return str;
	}
}
