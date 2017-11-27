package vi.smartsecuritysystem;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserLogsActivity extends AppCompatActivity {

    private ListView historyList;
    private ArrayList<String> logs = new ArrayList<String>();
    private String domain;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logs);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Resources res = getResources();
        domain = res.getString(R.string.domain);

        user = getIntent().getStringExtra("emailUser");

        getHistory();

        if(logs.size() == 0){
            logs.add("No recent history.");
        }

        historyList = (ListView) findViewById(R.id.log_list);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logs);
        historyList.setAdapter(itemsAdapter);
    }


    private void getHistory(){
//        try (BufferedReader input = new BufferedReader(new InputStreamReader(
//                openFileInput(fileName))); ){
//            String line;
//            while ((line = input.readLine()) != null) {
//                logs.add(0, line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        UserLogsActivity.Background_get asyncTask = new UserLogsActivity.Background_get();

        try{
            String x = asyncTask.execute("history.txt").get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    private class Background_get extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL(domain + "history.txt");

                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    // str is one line of text; readLine() strips the newline character(s)
                    logs.add(0, str);
                }
                in.close();

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
                startActivity(new Intent(UserLogsActivity.this, LoginActivity.class));
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
