# Drawing and Signature App

This Android application provides an environment for drawing and saving artwork. It has been designed to be easy to use and flexible, with essential functionality to create, customize, and store drawings.

## Features:

1. **Drawing Canvas (SignaturePad)**:
   - A flexible canvas to draw freehand sketches or signatures.

2. **Adjustable Pen Size**:
   - Use a `SeekBar` to set the pen size dynamically (0–50 dp), with real-time display of the selected size.

3. **Customizable Pen Color**:
   - A color picker dialog (`AmbilWarnaDialog`) lets users select from a wide spectrum of colors.

4. **Eraser**:
   - Quickly clear all drawings from the canvas using the erase button.

5. **Save as Image**:
   - Save the canvas content as a PNG file in the device's gallery under a folder named `MyPaintings`. Each image is timestamped for unique identification.

6. **Empty Canvas Detection**:
   - The app prevents saving if no content is present on the canvas, notifying the user with a toast message.

7. **Pre-Built and Ready-to-Run**:
   - The application is fully functional, downloadable, and ready to build, install, and use.

## Additional Libraries Used:

1. **AmbilWarna Color Picker**:
   - Library for an interactive color picker dialog.
   - GitHub: [AmbilWarna](https://github.com/yukuku/ambilwarna)
   - Added to enable easy and intuitive pen color selection.

2. **Android Signature Pad**:
   - Used for drawing smooth signatures.
   - GitHub: [Android Signature Pad](https://github.com/warting/android-signaturepad)

## Availability:

The code for this application is:
- **Downloadable**: The complete source code is structured and ready for use.
- **Buildable**: With a standard Android development setup (Android Studio, Gradle, Java), the app can be built without any additional setup.
- **Fully Functional**: Once built, it works out-of-the-box as a standalone drawing application.

This simple app can serve as a template for larger projects or be used as-is for tasks requiring freehand drawing or digital signatures.
