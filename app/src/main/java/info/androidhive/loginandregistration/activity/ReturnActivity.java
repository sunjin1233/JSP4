package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;

/**
 * Created by CheolHwi on 2017-12-09.
 */

public class ReturnActivity extends Activity {
    private static final String TAG = ReturnActivity.class.getSimpleName();
    private static final String BIKENUM = "1";
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String email = user.get("email");

        //take bike by bnoandemail
        returnBikeByBnoannEmail(BIKENUM, email);
    }

    private void returnBikeByBnoannEmail(final String bno, final String email){
        // Tag used to cancel the request
        String tag_string_req = "req_returnBikeByBnoandEmail";

        pDialog.setMessage("Returning Bike ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RETURN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Return Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    //check for error node in json
                    if (!error) {//no error   
                        JSONObject bikereturninfoinfo = jObj.getJSONObject("bikereturninfo");
                        String nfc = bikereturninfoinfo.getString("nfc");
                        String lend = bikereturninfoinfo.getString("lend");
                        String borrow = bikereturninfoinfo.getString("borrow");

                        if (nfc.equals("no") && lend.equals("yes") && borrow.equals("no")) {//return succeed
                            Toast.makeText(getApplicationContext(),"Return Succeed", Toast.LENGTH_LONG).show();
                        } else {//No Borrow User
                            Toast.makeText(getApplicationContext(),"Wrong Request:No Borrow Bike", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Error in returning bike. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Return Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Posting parameter to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("bno", bno);
                params.put("email", email);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
