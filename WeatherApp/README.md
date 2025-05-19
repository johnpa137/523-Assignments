# Weather App HW3

This is a basic weather app. 
It lets you type in a city name and see the current weather using data from OpenWeatherMap. 
It shows the following information:
- City Name
- Temperature
- Weather Condition
- Min and Max Temperature
- Sunrise time
- Sunset time
- Wind Speed
- Pressure
- and Humidity

Also:

- It displays a weather icon based on the condition
- Gives helpful error messages if:
  - No city is entered
  - City isn’t found
  - No internet connection
- If there's an internet connection at the beginning it will attempt to display weather based on current geolocation of the user


I spent about 6-7 hours or so on this app.  

I faced the following challenges:
- Getting Ktor to work correctly in Android and figuring out the right dependencies, realized it was the lib.versions.toml that I had to update. I got around this by using the homework template in HW2, but didn't want to start over this time and so had to actually figure it out. I feel this is not covered well in class.
- Handling different responses from the API (it crashes if you try to read an error like it’s valid data!)
- Making the layout look somewhat good, thankful openweathermap has a weather icon API and I didn't have to go through have to switch custom graphics for the imageview. 
- Trying to figure out the import with Figma, at the end I just downloaded the icons and backgrounds as SVG's 
- I did get around having to create a some of the drawables by using emoji's hopefully that's acceptable. 
- There appears to be an issue trying to implement play_services_location with the lib.versions.toml and I couldn't figure out why it kept getting an unresolved reference so I just left it out and used a full string in the build.gradle.kts

References:
- https://openweathermap.org/weather-data
- https://ktor.io/
- https://coil-kt.github.io/coil/
- https://stackoverflow.com/questions/34139048/cannot-resolve-manifest-permission-access-fine-location
- https://en.wikipedia.org/wiki/Absolute_zero
- https://www.pbs.org/wgbh/nova/zero/hot.html
- https://en.wikipedia.org/wiki/Speed_of_light