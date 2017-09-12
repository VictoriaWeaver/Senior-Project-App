package vi.smartsecuritysystem;

/**
 * Created by victoria on 9/7/2017.
 */

public class User {

    private int _id;
    private String _name;
    private boolean _admin;
    private boolean _family;

    public User(){

    }

    public User(int id, String name, boolean family, boolean admin) {
        this._id = id;
        this._name = name;
        this._admin = admin;
        this._family = family;
    }

    public User(String name, boolean family, boolean admin) {
        this._name = name;
        this._admin = admin;
        this._family = family;
    }

    public int getID(){
        return this._id;
    }

    public String getName(){
        return this._name;
    }

    public boolean isAdmin(){
        return this._admin;
    }

    public boolean isFamily(){
        return this._family;
    }

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

}
