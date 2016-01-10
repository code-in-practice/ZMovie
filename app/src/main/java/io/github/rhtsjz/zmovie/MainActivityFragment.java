package io.github.rhtsjz.zmovie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private AndroidFlavorAdapter flavorAdapter;

    AndroidFlavor[] androidFlavors = {
            new AndroidFlavor("Cupcake", "1.5", R.drawable.abc_ab_share_pack_mtrl_alpha),
            new AndroidFlavor("Donut", "1.6", R.drawable.abc_ab_share_pack_mtrl_alpha),
            new AndroidFlavor("Eclair", "2.0-2.1", R.drawable.abc_ab_share_pack_mtrl_alpha),
            new AndroidFlavor("Froyo", "2.2-2.2.3", R.drawable.abc_ab_share_pack_mtrl_alpha),
            new AndroidFlavor("GingerBread", "2.3-2.3.7", R.drawable.abc_ab_share_pack_mtrl_alpha),
            new AndroidFlavor("Honeycomb", "3.0-3.2.6", R.drawable.abc_ab_share_pack_mtrl_alpha),
            new AndroidFlavor("Ice Cream Sandwich", "4.0-4.0.4", R.drawable.abc_ab_share_pack_mtrl_alpha),
            new AndroidFlavor("Jelly Bean", "4.1-4.3.1", R.drawable.abc_ab_share_pack_mtrl_alpha),
            new AndroidFlavor("KitKat", "4.4-4.4.4", R.drawable.abc_ab_share_pack_mtrl_alpha),
            new AndroidFlavor("Lollipop", "5.0-5.1.1", R.drawable.abc_ab_share_pack_mtrl_alpha)
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        flavorAdapter = new AndroidFlavorAdapter(getActivity(), Arrays.asList(androidFlavors));

        GridView gridView = (GridView) rootView.findViewById(R.id.flavor_grid);
        gridView.setAdapter(flavorAdapter);
        return rootView;
    }
}
