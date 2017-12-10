package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import info.androidhive.loginandregistration.R;

/**
 * Created by CheolHwi on 2017-12-09.
 */

public class ReturnActivity extends Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_return);
    }
}
