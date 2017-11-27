package vi.smartsecuritysystem;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private Button familyAccountsBtn;
    private Button guestAccountsBtn;
    private Button controlBtn;
    private Button userLogsBtn;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            email = extras.getString("email");
        }

        setupButtons();

    }

    private void setupButtons(){
        familyAccountsBtn = (Button) findViewById(R.id.family_accounts_btn);
        guestAccountsBtn = (Button) findViewById(R.id.guest_accounts_btn);
        controlBtn = (Button) findViewById(R.id.control_btn);
        userLogsBtn = (Button) findViewById(R.id.user_logs_btn);

        familyAccountsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FamilyAccountsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        guestAccountsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuestAccountsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        controlBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        userLogsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserLogsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_logout:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
