This is an app with three tabs of functions

with a dashboard of three sensors:
- Step Counter (might need to really jerk the phone to get the step counter to count at least on the Galaxy A13)
- Accelerometer (all 3 axes)
- Proximity Sensor (either something is covering the screen or not)
There's also a camera tab


The app has three main parts: Sensors Dashboard, Camera, and Drawing Tab.
You can switch between them using a bottom menu.

Dashboard:
- Shows how many steps you've taken.
- Shows how your phone is moving (tilt/motion).
- Tells if something is close to the phone (proximity sensor).

Camera:
- Lets you take pictures using the front or back camera.
- You can save the pictures to your phone.

Draw:
- Lets you choose a photo from your gallery.
- You can draw on the photo with different colors.
- You can save the edited photo or reset it.

Other Features:

The app asks for permission when needed.

It works with different Android versions.

It's easy to use with clear buttons and layout.


This took about 5-6 hours to build. 

The most difficult part was debugging why the navcontroller wasn't being properly found. Apparently as shown in the 2nd link in the references, you need to use the supportfragmentmanager to properly find the fragmentcontainerview and find its navcontroller. 

References:
- https://developer.android.com/training/data-storage/use-cases#share-media-all
- https://stackoverflow.com/questions/50502269/illegalstateexception-link-does-not-have-a-navcontroller-set