package vi.smartsecuritysystem;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

public class ControlActivity extends AppCompatActivity {

    private static final String TAG = "ControlActivity";
    private static final int maxLines = 20;
    private Button unlockBtn;
    private Button lockBtn;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        fileName = getString(R.string.log_file);

        unlockBtn = (Button) findViewById(R.id.remote_unlock_btn);
        lockBtn = (Button) findViewById(R.id.remote_lock_btn);

        setListeners();

    }


    private void setListeners() {

        unlockBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //gpio high
//                    new Background_get().execute("gpio=1");

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    String log = "<USER> UNLOCK " + currentDateTimeString + "\n";
                    Log.w(TAG, log);

                    FileOutputStream outputStream;
                    if(isFull()){
                        outputStream = openFileOutput(fileName, MODE_PRIVATE);
                    } else {
                        outputStream = openFileOutput(fileName, MODE_APPEND);
                    }
                    outputStream.write(log.getBytes());
                    outputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        lockBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //gpio low
//                    new Background_get().execute("gpio=0");

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    String log = "<USER> LOCK " + currentDateTimeString + "\n";
                    Log.w(TAG, log);

                    FileOutputStream outputStream;
                    if(isFull()){
                        outputStream = openFileOutput(fileName, MODE_PRIVATE);
                    } else {
                        outputStream = openFileOutput(fileName, MODE_APPEND);
                    }

                    outputStream.write(log.getBytes());
                    outputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean isFull(){
        try (BufferedReader input = new BufferedReader(new InputStreamReader(
                openFileInput(fileName))); ){
            String line;
            int count = 0;
            while ((line = input.readLine()) != null) {
                count++;
            }
            return (count >= maxLines);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

//    private class Background_get extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//
//                //change the ip to the pi's ip
//                URL url = new URL("http://192.168.1.177/?" + params[0]);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder result = new StringBuilder();
//                String inputLine;
//                while ((inputLine = in.readLine()) != null)
//                    result.append(inputLine).append("\n");
//
//                in.close();
//                connection.disconnect();
//                return result.toString();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
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
