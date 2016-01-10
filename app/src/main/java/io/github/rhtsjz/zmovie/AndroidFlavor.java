package io.github.rhtsjz.zmovie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zsj on 16-1-10.
 */
public class AndroidFlavor implements Parcelable{
    String versionName;
    String versionNumber;
    int image; // drawable reference id

    public AndroidFlavor(String versionName, String versionNumber, int image) {
        this.versionName = versionName;
        this.versionNumber = versionNumber;
        this.image = image;
    }

    private AndroidFlavor(Parcel parcel) {
        versionNumber = parcel.readString();
        versionNumber = parcel.readString();
        image = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(versionName);
        parcel.writeString(versionNumber);
        parcel.writeInt(image);
    }

    public final Parcelable.Creator<AndroidFlavor> CREATOR =
            new Parcelable.ClassLoaderCreator<AndroidFlavor>(){
                @Override
                public AndroidFlavor createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    return new AndroidFlavor(parcel);
                }

                @Override
                public AndroidFlavor createFromParcel(Parcel parcel) {
                    return new AndroidFlavor(parcel);
                }

                @Override
                public AndroidFlavor[] newArray(int i) {
                    return new AndroidFlavor[i];
                }
            };


    public String toString(){
        return versionName + "--" + versionNumber + "--" + image;
    }
}
