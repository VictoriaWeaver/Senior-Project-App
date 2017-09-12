package vi.smartsecuritysystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class FamilyAccountsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_accounts);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        DBHelper db = new DBHelper(this);

        dbTestOps(db);

    }


    private void dbTestOps(DBHelper db) {
        /**
         * CRUD Operations
         * */
        // Inserting users
        User u = new User("Victoria", true, true);
        db.addUser(u);
//        db.addUser(new User("Prathibha", false, true));
//        db.addUser(new User("Ram", false, true));
//        db.addUser(new User("Kevin", false, false));

        //Reading users
//        List<User> users = db.getAllUsers();
//
//        for (User un : users) {
//            String log = "Id: " + un.getID() + " ,Name: " + un.getName() + " ,Admin: "
//                    + un.isAdmin() + " ,Family: " + un.isFamily() + "\n";
//
//            System.out.print(log);
//        }

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
