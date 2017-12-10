package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

public class MainActivity extends Activity {

	private TextView txtName;
	private TextView txtEmail;
	private TextView txtPhone;
	private Button btnLogout;
	private Button btnLend;
	private Button btnTake;
	private Button btnBorrow;
	private Button btnReturn;

	private SQLiteHandler db;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		txtName = (TextView) findViewById(R.id.name);
		txtEmail = (TextView) findViewById(R.id.email);
		txtPhone = (TextView) findViewById(R.id.textView2);

		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnLend = (Button) findViewById(R.id.btnLend);
		btnTake = (Button) findViewById(R.id.btnTake);
		btnBorrow = (Button) findViewById(R.id.btnBorrow);
		btnReturn = (Button) findViewById(R.id.btnReturn);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		// Fetching user details from SQLite
		HashMap<String, String> user = db.getUserDetails();

		String name = user.get("name");
		String email = user.get("email");
		String phone = user.get("phone");

		// Displaying the user details on the screen
		txtName.setText(name);
		txtEmail.setText(email);
		txtPhone.setText(phone);

		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});

		// Link to Lend
		btnLend.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LendActivity.class);
				startActivity(i);
				finish();
			}
		});

		// Link to Take
		btnTake.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						TakeActivity.class);
				startActivity(i);
				finish();
			}
		});

		// Link to Borrow
		btnBorrow.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						BorrowActivity.class);
				startActivity(i);
				finish();
			}
		});

		// Link to Lend
		btnReturn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						ReturnActivity.class);
				startActivity(i);
				finish();
			}
		});
	}

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
