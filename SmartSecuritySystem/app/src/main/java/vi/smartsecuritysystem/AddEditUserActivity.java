package vi.smartsecuritysystem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class AddEditUserActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private Button profileBtn;
    private ImageView profileImage;
    private String imgDecodableString;
    private Button doneBtn;
    private EditText nameEdit;
    private EditText accountExpDate;
    private RelativeLayout guestView;
    private boolean guestFlag = false;
    private Switch familySwitch;
    private Switch adminSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        profileImage = (ImageView) findViewById(R.id.profile_image);
        profileBtn = (Button) findViewById(R.id.profile_image_btn);
        nameEdit = (EditText) findViewById(R.id.user_name);
        adminSwitch = (Switch) findViewById(R.id.admin_btn);
        familySwitch = (Switch) findViewById(R.id.family_btn);
        guestView = (RelativeLayout) findViewById(R.id.guest_account_view);
        accountExpDate = (EditText) findViewById(R.id.guest_account_expiration_date);
        doneBtn = (Button) findViewById(R.id.done_btn);

        //set GuestFlag based on previous activity
        familySwitch.setChecked(!guestFlag);
        if(guestFlag){
            guestView.setVisibility(View.VISIBLE);
        } else{
            guestView.setVisibility(View.INVISIBLE);
        }

        setListeners();

    }


    private void setListeners() {
        profileBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });


        familySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    guestView.setVisibility(View.VISIBLE);
                } else{
                    guestView.setVisibility(View.INVISIBLE);
                }

            }
        });


        accountExpDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar mcurrentDate = Calendar.getInstance();

                DatePickerDialog mDatePicker = new DatePickerDialog(AddEditUserActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    /*      Your code   to get date and time    */
                        mcurrentDate.set(selectedyear, selectedmonth, selectedday);
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyy");
                        accountExpDate.setText(format.format(mcurrentDate.getTime()));
                    }
                }, mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH), mcurrentDate.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO Add user to database
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                profileImage.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
            } else {

                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();

        }
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

