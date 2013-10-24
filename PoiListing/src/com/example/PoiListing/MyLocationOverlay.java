package com.example.PoiListing;

import android.graphics.drawable.Drawable;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Created with IntelliJ IDEA.
 * User: cdm
 * Date: 13-10-14
 * Time: AM10:14
 * To change this template use File | Settings | File Templates.
 */
public class MyLocationOverlay extends ItemizedOverlay {


    public MyLocationOverlay(Drawable drawable, MapView mapView) {
        super(drawable, mapView);
    }

    public void setMyLocation(GeoPoint myLocation) {
        removeAll();
        OverlayItem item = new OverlayItem(myLocation, "", "");
        addItem(item);

    }


}
