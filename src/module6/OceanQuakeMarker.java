package module6;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		isOnLand = false;
	}


	//Drawing a different size of the circle on the map based on how deep the earthquake is. This method must be
	// implemented in here since it is an abstract method defined in the parent class
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.strokeWeight(0);
		pg.rect(x-radius, y-radius, 2*radius, 2*radius);
	}


}
