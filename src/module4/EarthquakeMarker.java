package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Krystyna Garwol
 *
 */
	//The way EarthquakeMarker class is called is based o the Java 'inside out' object creation concept.
public abstract class EarthquakeMarker extends SimplePointMarker {

	protected boolean isOnLand;
//	protected float radius;
	public static final float THRESHOLD_MODERATE = 5;
	public static final float THRESHOLD_LIGHT = 4;
	public static final float THRESHOLD_INTERMEDIATE = 70;
	public static final float THRESHOLD_DEEP = 300;

	//Abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	
	//Constructor
	public EarthquakeMarker (PointFeature feature) {
		super(feature.getLocation(), feature.getProperties());

		//We are dealing with two objects: the PointFeature (input) and the EarthquakeMarker (constructed output).
		//The EarthquakeMarker does not yet have any properties, so using any of its getters will cause an exception.
		//To construct it, you need to extract the properties from the incoming PointFeature, store them into properties,
		//and use setProperties (from the AbstractMarker class) to load them into the constructed EarthquakeMarker object.
//		java.util.HashMap<String, Object> properties = feature.getProperties();
//		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
//		properties.put("radius", 2 * magnitude );
//		setProperties(properties);
//		this.radius = 1.75f*getMagnitude();
	}

	//Method that draws marker on the map.
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		colorDetermine(pg);
		drawEarthquake(pg, x, y);

		String age = this.properties.get("age").toString().toLowerCase();
		if(age.equals("past day")) {
			pg.strokeWeight(2);
			pg.line(x-8, y+8, x+8, y-8);
			pg.line(x-8, y-8, x+8, y+8);
		}
		pg.popStyle();
	}

	//Determine color of marker based on earthquake's depth
	private void colorDetermine(PGraphics pg) {
		if(getDepth() > 0 && getDepth() <= THRESHOLD_INTERMEDIATE) {
			pg.fill(255, 255, 0);
		} else if(getDepth() > THRESHOLD_INTERMEDIATE && getDepth() <= THRESHOLD_DEEP) {
			pg.fill(0, 0, 255);
		} else {
			pg.fill(255, 0, 0);
		}
	}

	//Getters
	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());
	}
	public String getTitle() {
		return (String) getProperty("title");
	}
	public boolean isOnLand() {
		return isOnLand;
	}
}
