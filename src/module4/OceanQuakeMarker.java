package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Krystyna Garwol
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {

	//Constructor
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		isOnLand = false;
	}

	//Drawing a different size of the circle on the map based on how deep the earthquake is. This method must be
	// implemented in here since it is an abstract method defined in the parent class
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		if(getMagnitude() <= THRESHOLD_LIGHT) {
			pg.rect(x, y, 10, 10);
		} else if(getMagnitude() > THRESHOLD_LIGHT && getMagnitude() <= THRESHOLD_MODERATE) {
			pg.rect(x, y, 16, 16);
		} else if(getMagnitude() > THRESHOLD_MODERATE && getMagnitude() <= THRESHOLD_INTERMEDIATE) {
			pg.rect(x, y, 22, 22);
		} else if(getMagnitude() > THRESHOLD_INTERMEDIATE && getMagnitude() <= THRESHOLD_DEEP) {
			pg.rect(x, y, 28, 28);
		} else {
			pg.rect(x, y, 34, 34);
		}
	}
}
