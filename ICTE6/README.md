A simple app with three tabs in a bottom navigation bar with the center tab being a drawing view.

This took less than an hour to build.

The most difficult part was just trying to figure out how to include 
implementation 'com.google.android.material:material:1.7.0'
into the build.gradle.kts since it's not in the kotlin format
turns out I should've paid attention to the lightbulb prompt that asked to change it libs.material.v170

References:
- https://kotlinlang.org/docs/gradle-configure-project.html#-nelfua_415