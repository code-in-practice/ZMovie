package io.github.rhtsjz.zmovie;

/**
 * Created by zsj on 16-1-10.
 */
public class AndroidFlavor {
    String versionName;
    String versionNumber;
    int image; // drawable reference id

    public AndroidFlavor(String versionName, String versionNumber, int image) {
        this.versionName = versionName;
        this.versionNumber = versionNumber;
        this.image = image;
    }
}
