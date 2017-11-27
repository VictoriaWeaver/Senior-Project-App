package vi.smartsecuritysystem;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;

public class ControlActivity extends AppCompatActivity {

    private static final String TAG = "ControlActivity";
    private static final int maxLines = 20;
    private Button unlockBtn;
    private Button lockBtn;
    private TextView statusView;
    private String piStatus;
    private String logAction = "";
    private User user;
    private String domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        unlockBtn = (Button) findViewById(R.id.remote_unlock_btn);
        lockBtn = (Button) findViewById(R.id.remote_lock_btn);
        statusView = (TextView) findViewById(R.id.status);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            String email = extras.getString("emailUser");
            if(email != null){
                DBHelper dbHelp = new DBHelper(this);
                user = dbHelp.getUser(email);
            }
        }

        Resources res = getResources();
        domain = res.getString(R.string.domain);

        getStatus();


        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getStatus();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();


        setListeners();

    }

    private void getStatus() {

        Background_get asyncTask = new Background_get();

        try {
            String x = asyncTask.execute("status.txt").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Resources res = getResources();
        String text = "";
        text = res.getString(R.string.status, piStatus);
        statusView.setText(text);
    }


    private void setListeners() {

        unlockBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //gpio high
                    ControlActivity.Background_get asyncTask = new ControlActivity.Background_get();
                    String x = asyncTask.execute("unlock.php").get();

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    logAction = "<USER> UNLOCK " + currentDateTimeString + "\n";
                    Log.w(TAG, logAction);

                    getStatus();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        lockBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //gpio low
                    ControlActivity.Background_get asyncTask = new ControlActivity.Background_get();
                    String x = asyncTask.execute("lock.php").get();

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    logAction = "<USER> LOCK " + currentDateTimeString + "\n";
                    Log.w(TAG, logAction);

                    getStatus();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

//    private boolean isFull() {
//        try (BufferedReader input = new BufferedReader(new InputStreamReader(
//                openFileInput("history.txt")));) {
//            String line;
//            int count = 0;
//            while ((line = input.readLine()) != null) {
//                count++;
//            }
//            return (count >= maxLines);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
//    }

    private class Background_get extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                if (params[0].equals("status.txt")) {
                    URL url = new URL(domain+"status.txt");

                    // Read all the text returned by the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    piStatus = "";
                    while ((str = in.readLine()) != null) {
                        // str is one line of text; readLine() strips the newline character(s)
                        piStatus += str;
                    }
                    in.close();


                } else {
                    String sanitized = URLEncoder.encode(user.getName(), "UTF-8");
                    URL url = new URL(domain + params[0] + "?name=" + sanitized);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.connect();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        result.append(inputLine).append("\n");

                    in.close();
                    connection.disconnect();

                    return result.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_logout:
                startActivity(new Intent(ControlActivity.this, LoginActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate our menu from the resources by using the menu inflater.
        getMenuInflater().inflate(R.menu.toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }


}
