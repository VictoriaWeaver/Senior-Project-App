package vi.smartsecuritysystem;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GuestAccountsActivity extends AppCompatActivity {

    private FloatingActionButton addUser;
    private RecyclerView userRecyclerView;
    private RecyclerView.Adapter userAdapter;
    private RecyclerView.LayoutManager userLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_accounts);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        addUser = (FloatingActionButton) findViewById(R.id.add_user_btn);
        addUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(GuestAccountsActivity.this, AddEditUserActivity.class));
            }
        });

        userRecyclerView = (RecyclerView) findViewById(R.id.guest_accounts_list);
        displayUsers(userRecyclerView);
    }


    private void displayUsers(RecyclerView userRecyclerView) {

        DBHelper dbHelp = new DBHelper(this);
        List<User> lst =  dbHelp.getAllUsers();

        List<String> usernames = new ArrayList<String>();

        for(User u : lst){
            if(!u.isFamily()) {
                usernames.add(u.getName());
            }
        }

        // Temporary list in while DB is in development
        usernames.add("Bob");
        //usernames.add("Linda");
        //usernames.add("Tina");
        //usernames.add("Gene");
        //usernames.add("Louise");


        userRecyclerView.setHasFixedSize(true);

        userLayoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(userLayoutManager);

        userAdapter = new UserAdapter(GuestAccountsActivity.this, usernames);
        userRecyclerView.setAdapter(userAdapter);

    }

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
