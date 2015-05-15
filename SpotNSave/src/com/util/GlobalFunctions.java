package com.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GlobalFunctions {
	// static ProgressDialog progress;

	public interface HttpResponseHandler {
		void handle(String response, boolean failre);
	}

	public static void postApiCall(final Context context, final String url,
			RequestParams params, AsyncHttpClient httpClient,
			final HttpResponseHandler handler) {

		httpClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String failureResponse) {
				// TODO Auto-generated method stub

				super.onFailure(arg0, failureResponse);
				System.out.println("fail" + failureResponse + "url is" + url);
				handler.handle(failureResponse, false);

				// errorToast(context);
			}

			@Override
			public void onSuccess(String response) {

				System.out.println("response" + response);
					handler.handle(response, true);

			}
		});
	}

}
