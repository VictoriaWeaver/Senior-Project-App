package vi.smartsecuritysystem;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddEditUserActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;

    private Button choosePhotoBtn;
    private ImageView profileImage;
    private String imgDecodableString;
    private Button doneBtn;
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText accountExpDate;
    private RelativeLayout guestView;
    private boolean guestFlag = false;
    private Switch familySwitch;
    private Switch adminSwitch;
    private Bitmap bitmap;
    private String domain;
    private String user;

    boolean userIsAdmin = true;
    boolean edit;
    String edit_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        profileImage = (ImageView) findViewById(R.id.profile_image);
        choosePhotoBtn = (Button) findViewById(R.id.choose_photo_btn);
        nameEdit = (EditText) findViewById(R.id.user_name);
        emailEdit = (EditText) findViewById(R.id.user_email);
        passwordEdit = (EditText) findViewById(R.id.user_password);
        adminSwitch = (Switch) findViewById(R.id.admin_btn);
        familySwitch = (Switch) findViewById(R.id.family_btn);
        guestView = (RelativeLayout) findViewById(R.id.guest_account_view);
        accountExpDate = (EditText) findViewById(R.id.guest_account_expiration_date);
        doneBtn = (Button) findViewById(R.id.done_btn);

        if (!userIsAdmin) {
            adminSwitch.setEnabled(false);
        }

        //set GuestFlag based on previous activity
        familySwitch.setChecked(!guestFlag);
        if (guestFlag) {
            guestView.setVisibility(View.VISIBLE);
        } else {
            guestView.setVisibility(View.INVISIBLE);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            edit = extras.getBoolean("edit");
            user = extras.getString("emailUser");
            String email = extras.getString("email");
            if (edit) {
                edit_email = email;
                DBHelper dbHelp = new DBHelper(this);
                User u = dbHelp.getUser(email);
                emailEdit.setText(email);
                String userImage = u.getImage();
                File imgFile = new File(userImage);
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getPath());
                profileImage.setImageBitmap(bitmap);
                imgDecodableString = u.getImage();
                nameEdit.setText(u.getName());
                adminSwitch.setChecked(u.isAdmin());
                familySwitch.setChecked(u.isFamily());
            }
        }

        Resources res = getResources();
        domain = res.getString(R.string.domain);

        setListeners();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {

                }
                break;
        }
    }


    private void setListeners() {

        choosePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(AddEditUserActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddEditUserActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        adminSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (userIsAdmin) {
                    if (isChecked) {
                        AlertDialog alertDialog = new AlertDialog.Builder(AddEditUserActivity.this).create();
                        alertDialog.setTitle("Warning");
                        alertDialog.setMessage("Are you sure you want to grant this user Admin rights?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        adminSwitch.setChecked(false);
                                    }
                                });
                        alertDialog.show();
                        familySwitch.setChecked(true);
                    }
                }

            }
        });

        familySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (adminSwitch.isChecked()) {
                    //Admins must be family accounts
                    AlertDialog alertDialog = new AlertDialog.Builder(AddEditUserActivity.this).create();
                    alertDialog.setMessage("Admin users must be Family Users");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    familySwitch.setChecked(true);
                } else {
                    if (!isChecked) {
                        guestView.setVisibility(View.VISIBLE);
                    } else {
                        guestView.setVisibility(View.INVISIBLE);
                    }
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
                //TODO check if fields are valid
                int status = isValidUser();
                if (status == 0) {

                    String saltedPassword = DBHelper.SALT + passwordEdit.getText().toString();
                    String hashedPassword = DBHelper.generateHash(saltedPassword);
                    DBHelper dbHelp = new DBHelper(getApplicationContext());

                    int next_id;
                    if (!edit) {
                        next_id = dbHelp.getID();
                    } else {
                        next_id = dbHelp.getUser(edit_email).getID();
                    }


                    bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                    File cachePath = new File(imgDecodableString);
                    try {
                        cachePath.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(cachePath);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    User u = new User(next_id, nameEdit.getText().toString(), !familySwitch.getShowText(), adminSwitch.getShowText(),
                            emailEdit.getText().toString(), hashedPassword, imgDecodableString);
                    if (!edit) {
                        dbHelp.addUser(u);
                    } else {
                        dbHelp.updateUser(u);
                    }

//                    try {
//
//                        String sanitized = nameEdit.getText().toString().replaceAll(" ", "_");
//                        AddEditUserActivity.Background_get asyncTask = new AddEditUserActivity.Background_get();
//                        String x = asyncTask.execute("name=" + sanitized).get();
//
//                        //transfer imageInByte to rpi3
//                        bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
//                        asyncTask = new AddEditUserActivity.Background_get();
//                        x = asyncTask.execute(sanitized).get();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    Intent intent = new Intent(AddEditUserActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("emailUser", user);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else if (status == 1) {
                    Context context = getApplicationContext();
                    CharSequence text = "Please complete all fields";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Please enter a valid email address";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

    }

    private int isValidUser() {
        if (nameEdit.getText().toString().isEmpty() || passwordEdit.getText().toString().isEmpty() || emailEdit.getText().toString().isEmpty()) {
            return 1;
        }
        if (!emailEdit.getText().toString().contains("@")) {
            return -1;
        }
        return 0;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && null != data) {

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

            case R.id.action_logout:
                startActivity(new Intent(AddEditUserActivity.this, LoginActivity.class));
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


    private class Background_get extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            if (params[0].contains("name=")) {
                try {

                    URL url = new URL(domain + "UserImages/addUser.php?" + params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        result.append(inputLine).append("\n");

                    in.close();
                    connection.disconnect();

                    return "";

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            else {
//                try {
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    byte[] imageInByte = baos.toByteArray();
//
//                    URL url = new URL(domain + "UserImages/" + params[0] + "/" + params[0] + ".jpg");
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//                    //TODO: copy image into user.jpg
////                    DataOutputStream  out = new DataOutputStream(connection.getOutputStream());
////                    out.write(imageInByte);
////                    out.close();
//
//                    return "";

//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            return null;
        }
    }

}

