package me.taedium.android.view;

import java.util.ArrayList;
import java.util.List;

import me.taedium.android.R;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapDetail extends MapActivity {
    private MapView mapview;
    private List<Overlay> mapOverlays;
    private Drawable drawable;
    private MapOverlay overlay;
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.view_mapview);
        mapview = (MapView)findViewById(R.id.mvMapView);
        mapview.setBuiltInZoomControls(true);
        
        final double longitude = getIntent().getExtras().getDouble("long");
        final double latitude = getIntent().getExtras().getDouble("lat");
        
        Button directions = (Button)findViewById(R.id.bGetDirections);
        directions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?f=d&daddr=" + latitude + "," + longitude));
                intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
                startActivity(intent);
            }
        });
        
        MapController mc = mapview.getController();
        GeoPoint point = new GeoPoint((int)(latitude * 1e6), (int)(longitude * 1e6));
        mc.setCenter(point);
        mc.setZoom(16);
        
        mapOverlays = mapview.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.map_marker);
        overlay = new MapOverlay(drawable);
        
        OverlayItem overlayitem = new OverlayItem(point, "", "");
        overlay.addOverlay(overlayitem);
        mapOverlays.add(overlay);
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    private class MapOverlay extends ItemizedOverlay<OverlayItem> {
        private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

        public MapOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
        }

        @Override
        protected OverlayItem createItem(int i) {
          return mOverlays.get(i);
        }

        @Override
        public int size() {
            return mOverlays.size();
        }
        
        public void addOverlay(OverlayItem overlay) {
            mOverlays.add(overlay);
            populate();
        }
    }
}
