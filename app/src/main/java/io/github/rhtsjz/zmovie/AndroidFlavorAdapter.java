package io.github.rhtsjz.zmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zsj on 16-1-10.
 */
public class AndroidFlavorAdapter extends ArrayAdapter<AndroidFlavor> {
    private static final String LOG_TAG = AndroidFlavorAdapter.class.getSimpleName();

    public AndroidFlavorAdapter(Context context, List<AndroidFlavor> androidFlavors) {
        super(context, 0, androidFlavors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AndroidFlavor androidFlavor = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.flavor_item,
                    parent,
                    false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.flavor_image);
        imageView.setImageResource(androidFlavor.image);

        TextView textView = (TextView) convertView.findViewById(R.id.flavor_text);
        textView.setText(androidFlavor.versionName + "-" + androidFlavor.versionNumber);

        return convertView;
    }
}
