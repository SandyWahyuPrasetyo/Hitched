package edu.uco.weddingcrashers.hitched;

import android.support.v4.app.Fragment;

/**
 * Created by PC User on 3/3/2016.
 */
public class MusicListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MusicListFragment();
    }
}