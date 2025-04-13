This is simple app that uses the Camera of the phone to take a picture and display said taken picture. 
The picture can be saved into the pictures folder of the phone or otherwise reset to clear the display.

It took about 3 hours to build primarily due to trying to get it to work on Android 10 which has odd interaction with the WRITE_EXTERNAL_STORAGE permissions.

The most challenging part was trying to get the save image function to work on an Android 10 device while getting permission denied, while already having WRITE_EXTERNAL_STORAGE permissions. 

References:
https://canvas.uw.edu/courses/1800612/files/133480130?module_item_id=23654999
https://stackoverflow.com/questions/64221188/write-external-storage-when-targeting-android-10