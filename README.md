# Java OOP - Unfolding Maps
This project has been built while undertaking the Object Oriented Programming in Java course provided on Coursera by
University of San Diego.
My aim was to improve my programming skills, understand OOP fundamentals and get more comfortable with Java.

The application is using:
* Unfolding Maps library to create interactive maps and geo-visualizations
* Processing Library


### Earthquakes
This map displays land and ocean earthquakes grouped by their magnitude and cities.
* When user hovers over city or earthquake marker the information about that city or earthquake is displayed.
* When user clicks on the earthquake marker, all other markers disappear from the map except for the city markers that are within the threat circle of this earthquake.
* When user clicks on the city marker, all other markers disappear from the map except for the earthquakes that contain
that city in their threat circle.
* Any subsequent click, anywhere on the map, brings the map to its initial state.

Extensions added:
* When user clicks on the city marker, the pop up box appears under the map legend on the left hand side, with the name
of the city and the country the city is located in. The number of nearby earthquakes is also displayed along with the
average magnitude of those earthquakes. If there are no earthquakes within the city threat circle, only city and country
 names are printed.
* Any subsequent click, anywhere on the map, brings the map to its initial state.
* When user hits the key 'p' on the keyboard, all cities with the population greater than 1 million, disappear from the
map.
* By hitting any other keyboard button, the map it's brought to its initial state.


<div>
<p>Left Image: all earthquakes showing on-hover information message box.</p>
<p>Right Image: cities with population greated than 1m hidden.</p>
</div>

<div float="left">
  <img width="500" alt="all earthquakes" src="https://user-images.githubusercontent.com/72078274/104127034-bfcbd980-5357-11eb-86f7-49906fbcf21e.JPG"> 
  <img width="500" alt="cities with population greated than 1m hidden" src="https://user-images.githubusercontent.com/72078274/104127041-c9edd800-5357-11eb-88d8-91516cf078e9.JPG"> 
</div>


<div>
<p>Left Image: pop up with average magnitude.</p>
<p>Right Image: pop up with city and country name only.</p>
</div>

<div float="left">
  <img width="500" alt="pop up with average magnitude" src="https://user-images.githubusercontent.com/72078274/104127038-c65a5100-5357-11eb-87e5-5050239680cf.JPG"> 
  <img width="500" alt="pop up with city and country name only" src="https://user-images.githubusercontent.com/72078274/104127040-c78b7e00-5357-11eb-9775-f1711f53e3e6.JPG"> 
</div>
   


### Airports
This map displays all/majority of the world's airports.

Extensions added:
* When user clicks on the airport marker, all other airport markers disappear from the map, except for the marker
clicked and the markers that belong to the same country as clicked marker.


<div>
<p>Left Image: all airports.</p>
<p>Right Image: only one country airports displayed when clicked on the airport marker belonging to that country.</p>
</div>

<div float="left">
  <img width="500" alt="all airports" src="https://user-images.githubusercontent.com/72078274/104127002-88f5c380-5357-11eb-9e5f-eadc0a486a52.JPG">
  <img width="500" alt="hidden airports" src="https://user-images.githubusercontent.com/72078274/104127004-8abf8700-5357-11eb-95fb-64ecc1b359de.JPG">
</div>



### Life Expectancy
This map adds colours to different countries based on life expectancy.
No extensions added.

<img width="500" alt="life-expectancy" src="https://user-images.githubusercontent.com/72078274/104126990-767b8a00-5357-11eb-90ce-f4264363f454.JPG">