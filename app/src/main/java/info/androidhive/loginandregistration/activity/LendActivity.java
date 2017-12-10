package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

/**
 * Created by CheolHwi on 2017-12-09.
 */

public class LendActivity extends Activity{
    private static final String TAG = LendActivity.class.getSimpleName();
    private static final String BIKENUM = "1";
    private TextView txtLend;
    private Button btnLend;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend);

        btnLend = (Button) findViewById(R.id.button_lend);
        txtLend = (TextView) findViewById(R.id.textView_lend);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        findEmptyBikeport(BIKENUM);
    }

    /**
     * Function to find empty bikeport will post params(tag, bno)
     * to search bikes
     * if there is empty bikeport(nfc,lend,borrow are no), return true, else return false.
     * */
    private void findEmptyBikeport(final String bno){
        // Tag used to cancel the request
        String tag_string_req = "req_findbikeport";

        pDialog.setMessage("Finding Bikeport ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Lend Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    //check for error node in json
                    if (!error) {//can get bikeinfo
                        JSONObject bikeinfo = jObj.getJSONObject("bikeinfo");
                        String nfc = bikeinfo.getString("nfc");
                        String lend = bikeinfo.getString("lend");
                        String borrow = bikeinfo.getString("borrow");

                        if (nfc.equals("no") && lend.equals("no") && borrow.equals("no")) {//there is empty bikeport
                            //bteLend click event
                            btnLend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Fetching user details from SQLite
                                    HashMap<String, String> user = db.getUserDetails();

                                    String email = user.get("email");

                                    lendBike(BIKENUM, email);
                                }
                            });
                        } else {//bikeport is full
                            txtLend.setText("NO Empty Bikeport");
                            btnLend.setVisibility(View.GONE);
                        }
                    } else {
                        // Error in finding bikeport. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Posting parameter to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("bno", bno);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    /* Function to lend bike will post params(tag, bno, email)
    * to update bikes
    * */
    private  void lendBike(final String bno, final String email){
        // Tag used to cancel the request
        String tag_string_req = "req_Lend";

        pDialog.setMessage("Lending Bike ...");
        showDialog();

        StringRequest strReq2 = new StringRequest(Request.Method.POST,
                AppConfig.URL_LEND2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Lend Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // lend successfully
                        Toast.makeText(getApplicationContext(), "Lend successfully", Toast.LENGTH_LONG ).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Posting parameters to lend2 url
                Map<String, String> params = new HashMap<String, String>();
                params.put("bno", bno);
                params.put("email", email);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq2, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
