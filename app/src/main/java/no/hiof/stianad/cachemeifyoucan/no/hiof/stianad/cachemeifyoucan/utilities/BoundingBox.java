package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class BoundingBox
{
    private LatLng northEast;
    private LatLng southWest;
    private LatLngBounds boundingBox;

    public BoundingBox(LatLng latLng, double distance)
    {
        getBoundingBox(latLng, distance);
    }

    /*
        Federico A. Ramponi
        Oct 26 '08 at 20:21
        https://stackoverflow.com/questions/238260/how-to-calculate-the-bounding-box-for-a-given-lat-lng-location
     */
    private void getBoundingBox(LatLng latLng, double distance)
    {
        // Bounding box surrounding the point at given coordinates,
        // assuming local approximation of Earth surface as a sphere
        // of radius given by WGS84
        // http://en.wikipedia.org/wiki/Earth_radius
        double lat = Math.PI * latLng.latitude / 180.0;
        double lon = Math.PI * latLng.longitude / 180.0;
        double halfSide = 1000 * distance;

        // Radius of Earth at given latitude
        double radius = WGS84EarthRadius(lat);
        // Radius of the parallel at given latitude
        double pradius = radius * Math.cos(lat);

        double latMin = lat - halfSide / radius;
        double latMax = lat + halfSide / radius;
        double lonMin = lon - halfSide / pradius;
        double lonMax = lon + halfSide / pradius;

        northEast = new LatLng(180.0 * latMin / Math.PI, 180.0 * lonMin / Math.PI);
        southWest = new LatLng(180.0 * latMax / Math.PI, 180.0 * lonMax / Math.PI);

        boundingBox = new LatLngBounds(northEast, southWest);
    }

    /*
        Federico A. Ramponi
     */
    private static double WGS84EarthRadius(double lat)
    {
        double WGS84_a = 6378137.0; // Major semiaxis [m]
        double WGS84_b = 6356752.3; // Minor semiaxis [m]

        // http://en.wikipedia.org/wiki/Earth_radius
        double An = WGS84_a * WGS84_a * Math.cos(lat);
        double Bn = WGS84_b * WGS84_b * Math.sin(lat);
        double Ad = WGS84_a * Math.cos(lat);
        double Bd = WGS84_b * Math.sin(lat);
        return Math.sqrt((An * An + Bn * Bn) / (Ad * Ad + Bd * Bd));
    }

    public LatLng getNorthEast()
    {
        return northEast;
    }

    public LatLng getSouthWest()
    {
        return southWest;
    }

    public LatLngBounds getBoundingBox()
    {
        return boundingBox;
    }
}