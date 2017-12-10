package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import info.androidhive.loginandregistration.R;

/**
 * Created by CheolHwi on 2017-12-09.
 */

public class LendActivity extends Activity{
    private static final String TAG = LendActivity.class.getSimpleName();
    private Button btnLend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_lend);

        btnLend = (Button) findViewById(R.id.button_lend);


        //bteLend click event
    }

    /**
     * Function to find empty bikeport will post params(tag, bno)
     * to search bikes
     * */
    private void findEmptyBikeport(final int bno){

    }
    /* Function to lend bike will post params(tag, bno, email)
    * to update bikes
    * */
    private  void lendBike(final int bno, final String email){

    }
}
