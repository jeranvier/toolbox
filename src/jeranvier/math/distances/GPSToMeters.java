package jeranvier.math.distances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GPSToMeters {
	
	//c.f. http://www.movable-type.co.uk/scripts/latlong.html
	
	public static double convertToMeters(double[] point1, double[] point2){
		int R = 6371000; // km
		double dLat = Math.toRadians(point2[0]-point1[0]);
		double dLon = Math.toRadians(point2[1]-point1[1]);
		double lat1 = Math.toRadians(point1[0]);
		double lat2 = Math.toRadians(point2[0]);

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		return R * c;
	}
	
	public static double convertToAproximateMeters(double[] point1, double[] point2){
		int R = 6371000; // m
		double dLon = Math.toRadians(point2[1]-point1[1]);
		double lat1 = Math.toRadians(point1[0]);
		double lat2 = Math.toRadians(point2[0]);
		return Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2) * Math.cos(dLon)) * R;
	}
	public static void main(String[] arg){
		System.out.println(GPSToMeters.convertToMeters(new double[]{46.5195309,6.5654666}, new double[]{46.519542,6.5660513}));
		System.out.println(GPSToMeters.convertToAproximateMeters(new double[]{46.5154553872,6.61630054183}, new double[]{46.5152910181,6.61614271065}));
		System.out.println(GPSToMeters.convertToMeters(new double[]{46.574767256269105,6.8367049793023495}, new double[]{46.5154553872,6.61630054181}));
	}
}
