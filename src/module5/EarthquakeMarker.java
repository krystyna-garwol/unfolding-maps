package module5;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Krystyna Garwol
 *
 */
public abstract class EarthquakeMarker extends CommonMarker {

	protected boolean isOnLand;
	protected float radius;
	protected static final float kmPerMile = 1.6f;
	public static final float THRESHOLD_MODERATE = 5;
	public static final float THRESHOLD_LIGHT = 4;
	public static final float THRESHOLD_INTERMEDIATE = 70;
	public static final float THRESHOLD_DEEP = 300;

	//Abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
	
	//Constructor
	public EarthquakeMarker (PointFeature feature) {
		super(feature.getLocation());
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2*magnitude );
		setProperties(properties);
		this.radius = 1.75f*getMagnitude();
	}


	//Draws marker on the map - abstract method defined in the parent class
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.pushStyle();
		colorDetermine(pg);
		drawEarthquake(pg, x, y);
		
		// IMPLEMENT: add X over marker if within past day		
		String age = getStringProperty("age");
		if ("Past Hour".equals(age) || "Past Day".equals(age)) {
			pg.strokeWeight(2);
			int buffer = 2;
			pg.line(x-(radius+buffer), 
					y-(radius+buffer), 
					x+radius+buffer, 
					y+radius+buffer);
			pg.line(x-(radius+buffer), 
					y+(radius+buffer), 
					x+radius+buffer, 
					y-(radius+buffer));
			
		}
		pg.popStyle();
	}

	//Shows the title of the earthquake if this marker is selected
	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		pg.fill(0, 0, 0);
		pg.text(getTitle(), x, y);
		pg.fill(255, 255, 255);
	}


	//Return the "threat circle" radius, or distance up to which this earthquake can affect things, for this
	// earthquake. DISCLAIMER: this formula is for illustration purposes only and is not intended to be used for
	// safety-critical or predictive applications.
	public double threatCircle() {	
		double miles = 20.0f * Math.pow(1.8, 2*getMagnitude()-5);
		double km = (miles * kmPerMile);
		return km;
	}

	//Determine color of marker based on earthquake's depth
	private void colorDetermine(PGraphics pg) {
		float depth = getDepth();
		if (depth < THRESHOLD_INTERMEDIATE) {
			pg.fill(255, 255, 0);
		}
		else if (depth < THRESHOLD_DEEP) {
			pg.fill(0, 0, 255);
		}
		else {
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
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	public boolean isOnLand() {
		return isOnLand;
	}

}
