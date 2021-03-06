package vi.smartsecuritysystem;

import android.widget.ImageView;

/**
 * Created by victoria on 9/7/2017.
 */

public class User {

    private int _id;
    private String _name;
    private boolean _admin;
    private boolean _family;
    private String _email;
    private String _password;
    private String _image;

    public User(){
        this._id = 0;
        this._name = "null";
        this._family = false;
        this._admin = false;
        this._email = "null";
        this._password = "null";
    }

    public User(int id, String name, boolean family, boolean admin, String email, String password, String image) {
        this._id = id;
        this._name = name;
        this._admin = admin;
        this._family = family;
        this._email = email;
        this._password = password;
        this._image = image;

    }

    public User(String name, boolean family, boolean admin, String email, String password) {
        this._name = name;
        this._admin = admin;
        this._family = family;
        this._email = email;
        this._password = password;
    }

    public int getID(){
        return this._id;
    }

    public String getName(){
        return this._name;
    }

    public String getImage() { return this._image; }

    public boolean isAdmin(){
        return this._admin;
    }

    public boolean isFamily(){
        return this._family;
    }

    public String getEmail() { return this._email; }

    public String getPassword() { return this._password; }

    public void setID(int id){
        this._id = id;
    }

    public void setName(String name){
        this._name = name;
    }

    public void setAdmin(boolean admin){
        this._admin = admin;
    }

    public void setFamily(boolean family){
        this._family = family;
    }

    public void setEmail(String email){
        this._email = email;
    }

    public void setPassword(String password){
        this._password = password;
    }

    public void setImage(String b){ this._image = b; }

}
