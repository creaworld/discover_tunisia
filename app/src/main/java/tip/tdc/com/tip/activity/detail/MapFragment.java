package tip.tdc.com.tip.activity.detail;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpackingapp.vietnam.travel.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.model.Place;

import static com.google.android.gms.internal.zzagr.runOnUiThread;

public class MapFragment extends Fragment implements OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {


    private GoogleMap mMap;
    private List<Place> places;
    FCity mFCity;

    public void setPlaces(List<Place> places, FCity mFCity) {
        this.places = places;
        this.mFCity = mFCity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        initializeMapView(v);

        return v;
    }


    public void initializeMapView(final View v) {
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        MySupportMapFragment mapFragment;
        mapFragment = (MySupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.setListener(new MySupportMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    //v.findViewById(R.id.nestedScrollView).requestDisallowInterceptTouchEvent(true);
                }
            });
        new OnMapAndViewReadyListener(mapFragment, this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        
        mMap = googleMap;

        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);


        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        //mMap.setOnMarkerClickListener(this);
        //mMap.setOnInfoWindowClickListener(this);
        //mMap.setOnMarkerDragListener(this);
//        mMap.setOnInfoWindowCloseListener(this);
//        mMap.setOnInfoWindowLongClickListener(this);
//        mMap.setOnCameraMoveListener(this);
//        mMap.setOnCameraMoveStartedListener(this);
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Map with lots of markers.");

        updateMapBounder();

    }

    private void updateMapBounder() {
        if (mMap == null)
            return;
        if (places == null || places.size() == 0)
            return;


        AsyncTask asyncTask = new AsyncTask<String, String, String>() {

            LatLngBounds bounds;

            /**
             * Before starting background do some work.
             * */
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(String... params) {
                // TODO fetch url data do bg process.

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                int numberPlaceInclude = 1;

                try {
                    builder.include(new LatLng(Double.parseDouble(mFCity.getLatitude()), Double.parseDouble(mFCity.getLongitude())));
                    numberPlaceInclude++;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < places.size(); i++) {
                    if (places.get(i).getLatitude() == null || places.get(i).getLatitude().length() == 0 || places.get(i).getLongitude() == null || places.get(i).getLongitude().length() == 0) {
                        continue;
                    }
                    try {
                        if (places.get(i).getLatLng() == null) {
                            places.get(i).setLatLng(new LatLng(Double.parseDouble(places.get(i).getLongitude()), Double.parseDouble(places.get(i).getLatitude())));
                        }

                        builder.include(places.get(i).getLatLng());
                        numberPlaceInclude++;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (numberPlaceInclude > 0) {
                    bounds = builder.build();

                }
                return null;
            }

            /**
             * Update list ui after process finished.
             */
            protected void onPostExecute(String file_url) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Updating parsed JSON data into ListView
                         */
                        if (bounds != null) {
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, Integer.parseInt(mFCity.getZoom())));
                        }
                    }
                });
            }

        };
        asyncTask.execute(new String[]{});


    }
}


class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
    // "title" and "snippet".
    private final View mWindow;

    private final View mContents;

    CustomInfoWindowAdapter(FragmentActivity activity) {
        mWindow = activity.getLayoutInflater().inflate(R.layout.custom_info_window, null);
        mContents = activity.getLayoutInflater().inflate(R.layout.custom_info_contents, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
//            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
//                // This means that getInfoContents will be called.
//                return null;
//            }
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return mContents;
    }

    private void render(Marker marker, View view) {
        ((ImageView) view.findViewById(R.id.badge)).setImageResource(R.drawable.marker_4);
        ((ImageView) view.findViewById(R.id.badge)).setVisibility(View.GONE);

        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }
    }
}
