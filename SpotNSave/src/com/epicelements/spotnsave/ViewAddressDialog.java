package com.epicelements.spotnsave;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.luttu.AppPrefes;
import com.luttu.GPSTracker;
import com.luttu.PictureOrentation;
import com.makeramen.RoundedImageView;
import com.util.ImageSmallerAction;

public class ViewAddressDialog extends Fragment {
	SupportMapFragment supportMapFragment;
	GoogleMap myMap;
	private LatLng coordinate;
	private AppPrefes appPrefes;
	ImageSmallerAction imageSmallerAction = new ImageSmallerAction();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.viewaddress, container, false);
		findid(view);
		appPrefes = new AppPrefes(getActivity(), "sns"); 
		GPSTracker gpsTracker = new GPSTracker(getActivity());
		myMap.setMyLocationEnabled(true);
		double lat = gpsTracker.getLatitude();
		double lng = gpsTracker.getLongitude();
		coordinate = new LatLng(lat, lng);
		maplocaton();
//		setmarker(coordinate);
		timer();
		return view;
	}
	private void timer() {
		// TODO Auto-generated method stub
		new CountDownTimer(1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				try {
					SosView locfragment = (SosView) getActivity();
					locfragment.getLocation();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}.start();
	}


	private void maplocaton() {
		// TODO Auto-generated method stub
		myMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

			@Override
			public void onMyLocationChange(Location arg0) {
				// TODO Auto-generated method stub

//				myMap.addMarker(new MarkerOptions().position(
//						new LatLng(arg0.getLatitude(), arg0.getLongitude()))
//						.title("It's Me!"));
			}
		});
	}

	private void findid(View rootView) {
		// TODO Auto-generated method stub
		supportMapFragment = (SupportMapFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.map_view);
		myMap = supportMapFragment.getMap();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			FragmentTransaction ft = getActivity().getSupportFragmentManager()
					.beginTransaction();
			ft.remove(supportMapFragment);
			ft.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	void setmarker(LatLng coordinate2) {
		// TODO Auto-generated method stub
		final View cluster = LayoutInflater.from(getActivity()).inflate(
				R.layout.roundede_place, null);
		RoundedImageView im_profile_round = (RoundedImageView) cluster
				.findViewById(R.id.im_profile_round);
		im_profile_round.setBorderWidth(0);
		im_profile_round.setOval(false);

        float scale = getResources().getDisplayMetrics().density;
        int imageSizeWidthPX = (int)((80 * scale) + 0.5);
        int imageSizeHeightPX = (int)((80 * scale) + 0.5);
		if (!appPrefes.getData("ggcmain").equals("")) {
			if (appPrefes.getData("ggcmain").equals("cam")) {
				im_profile_round.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimagemain"), im_profile_round,getActivity());
			} else if (appPrefes.getData("ggcmain").equals("gallery")) {
				im_profile_round.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction
						.decodeSampledBitmapFromGallery(
								appPrefes.getData("gimagemain"), 100, 100);
				im_profile_round.setImageBitmap(ImageSmallerAction.getCircleBitmap(bitmap,imageSizeWidthPX,imageSizeHeightPX));
			} 
		}
		cluster.measure(MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED), MeasureSpec
				.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		cluster.layout(0, 0, cluster.getMeasuredWidth(),
				cluster.getMeasuredHeight());
		Bitmap clusterBitmap = Bitmap.createBitmap(
				cluster.getMeasuredWidth(),
				cluster.getMeasuredHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(clusterBitmap);
		cluster.draw(canvas);
		myMap.clear();   
		myMap.addMarker(new MarkerOptions()
				.title("")
				.draggable(true)
				.position(coordinate2)
				.icon(BitmapDescriptorFactory
						.fromBitmap(clusterBitmap)));
		myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
	} 

//	private void setmarker(String location) {
//		// TODO Auto-generated method stub
//		myMap.addMarker(new MarkerOptions()
//				.title(location)
//				.snippet("")
//				.draggable(true)
//				.position(coordinate)
//				.icon(BitmapDescriptorFactory
//						.fromResource(R.drawable.map_pin_red)));
//		myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
//	}
}
