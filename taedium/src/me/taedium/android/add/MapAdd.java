package me.taedium.android.add;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.taedium.android.ApplicationGlobals;
import me.taedium.android.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class MapAdd extends MapActivity {
    private static final String TAG = "MAPADD";
    private MapView mapview;
    private MyLocationOverlay myLoc;
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.add_mapview);
        
        // Get mapview and use zoom controls
        mapview = (MapView)findViewById(R.id.mvMapAdd);
        mapview.setBuiltInZoomControls(true);
       
        // Add onClickListener to the search button
        final EditText mapSearchBox = (EditText) findViewById(R.id.etSearchMap);
        ImageButton search = (ImageButton) findViewById(R.id.ibSearchMap);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // hide virtual keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mapSearchBox.getWindowToken(), 0);

                new SearchClicked(mapSearchBox.getText().toString()).execute();
                mapSearchBox.setText("", TextView.BufferType.EDITABLE);
            }
        });
        
        Button done = (Button) findViewById(R.id.bMapFinished);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapAdd.this, AddLocation.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", ((AddOverlay)mapview.getOverlays().get(0)).getLat());
                bundle.putDouble("long", ((AddOverlay)mapview.getOverlays().get(0)).getLong());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
       
        Location here = ApplicationGlobals.getInstance().getCurrentLocation();
        if (here != null) {
            MapController mc = mapview.getController();
            GeoPoint point = new GeoPoint((int)(here.getLatitude() * 1e6), (int)(here.getLongitude() * 1e6));
            mc.setCenter(point);
            mc.setZoom(17);
        }
        
        Drawable marker = getResources().getDrawable(R.drawable.map_marker);

        marker.setBounds(0, 0, marker.getIntrinsicWidth(),
        marker.getIntrinsicHeight());

        mapview.getOverlays().add(new AddOverlay(marker));

        myLoc = new MyLocationOverlay(this, mapview);
        mapview.getOverlays().add(myLoc);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_S) {
            mapview.setSatellite(!mapview.isSatellite());
            return(true);
        }
        else if (keyCode == KeyEvent.KEYCODE_Z) {
            mapview.displayZoomControls(true);
            return(true);
        }

        return(super.onKeyDown(keyCode, event));
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    /**
     * Asynchronous class for GeoCoding an address the user is searching for
     * @author ahal
     */
    private class SearchClicked extends AsyncTask<Void, Void, Boolean> {
        private String toSearch;
        private Address address;

        public SearchClicked(String toSearch) {
            this.toSearch = toSearch;
            Toast.makeText(MapAdd.this, toSearch, 1000);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> results = geocoder.getFromLocationName(toSearch, 1);
                if (results.size() == 0) {
                    return false;
                }

                address = results.get(0);
                GeoPoint p = new GeoPoint((int)(address.getLatitude() * 1E6), (int)(address.getLongitude() * 1E6));
                mapview.getController().setCenter(p);
                mapview.getController().setZoom(17);
                ((AddOverlay)mapview.getOverlays().get(0)).setOverlayItem(p);
            } catch (Exception e) {
                Log.d(TAG, "Exception while Geocoding address: " + e.toString());
                return false;
            }
            return true;
        }
    }

    /**
     * Overlay class which allows setting and dragging overlays 
     * @author ahal
     */
    private class AddOverlay extends ItemizedOverlay<OverlayItem> {
        private List<OverlayItem> items = new ArrayList<OverlayItem>();
        private Drawable marker = null;
        private OverlayItem inDrag = null;
        private ImageView dragImage = null;
        private int xDragImageOffset = 0;
        private int yDragImageOffset = 0;
        private int xDragTouchOffset = 0;
        private int yDragTouchOffset = 0;
        private boolean isTap = false;

        public AddOverlay(Drawable marker) {
            super(marker);
            this.marker = marker;
    
            dragImage = (ImageView)findViewById(R.id.ivDragImg);
            xDragImageOffset = dragImage.getDrawable().getIntrinsicWidth() / 2;
            // TODO 73 is the height of the search bar... calculate this properly
            yDragImageOffset = dragImage.getDrawable().getIntrinsicHeight() - 73;
            populate();
        }

        @Override
        protected OverlayItem createItem(int i) {
            return(items.get(i));
        }

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            super.draw(canvas, mapView, shadow);
            boundCenterBottom(marker);
        }
         
        @Override
        public int size() {
            return(items.size());
        }

        @Override
        public boolean onTouchEvent(MotionEvent event, MapView mapView) {
            final int action = event.getAction();
            final int x = (int)event.getX();
            final int y = (int)event.getY();
            boolean result = false;
    
            if (action == MotionEvent.ACTION_DOWN) {
                isTap = true;
                if (items.size() > 0) {
                    Point p = new Point(0,0);
                    OverlayItem item = items.get(0);
                    mapview.getProjection().toPixels(item.getPoint(), p);
            
                    if (hitTest(item, marker, x-p.x, y-p.y)) {
                        isTap = false;
                        result = true;
                        inDrag = item;
                        items.remove(inDrag);
                        populate();
                
                        xDragTouchOffset = 0;
                        yDragTouchOffset = 0;
                
                        setDragImagePosition(p.x, p.y);
                        dragImage.setVisibility(View.VISIBLE);
                
                        xDragTouchOffset = x - p.x;
                        yDragTouchOffset = y - p.y;
                    }
                }
            } else if (action == MotionEvent.ACTION_MOVE) {
                isTap = false;
                if (inDrag != null) {
                    setDragImagePosition(x, y);
                    result = true;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                if (inDrag != null) {
                    dragImage.setVisibility(View.GONE);
        
                    GeoPoint pt = mapview.getProjection().fromPixels(x-xDragTouchOffset, y-yDragTouchOffset);
                    OverlayItem toDrop = new OverlayItem(pt, inDrag.getTitle(), inDrag.getSnippet());
            
                    items.add(toDrop);
                    populate();
            
                    inDrag = null;
                    result = true;
                } else if (isTap){
                    GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
                    if (items.size() > 0) {
                        items.remove(0);
                    }
                    items.add(new OverlayItem(p, "", ""));
                    populate();
                }
            }
            return(result || super.onTouchEvent(event, mapView));
        }
        
        public void setOverlayItem(GeoPoint p) {
            if (items.size() > 0) {
                items.remove(0);
            }
            items.add(new OverlayItem(p, "", ""));
            populate();
        }
        
        public double getLat() {
            if (items.size() > 0) {
                return ((double)(items.get(0).getPoint().getLatitudeE6())) / ((double)1E6);
            }
            return 0;
        }
        
        public double getLong() {
            if (items.size() > 0) {
                return ((double)(items.get(0).getPoint().getLongitudeE6())) / ((double)1E6);
            }
            return 0;
        }
    
        private void setDragImagePosition(int x, int y) {
            RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams)dragImage.getLayoutParams();
    
            lp.setMargins(x-xDragImageOffset-xDragTouchOffset, y-yDragImageOffset-yDragTouchOffset, 0, 0);
            dragImage.setLayoutParams(lp);
        }
    }
}
