package vi.smartsecuritysystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victoria on 9/20/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.CustomViewHolder> {
    private List<User> userList;
    private Context mContext;

    public UserAdapter(Context context, List<User> userList) {
        this.userList = userList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_row_layout, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view, mContext);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        String userName = userList.get(i).getName();
        String userEmail = userList.get(i).getEmail();
        byte[] b = userList.get(i).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(b , 0, b.length);
        //Render image using Picasso library
//        if (!TextUtils.isEmpty(feedItem.getThumbnail())) {
//            Picasso.with(mContext).load(feedItem.getThumbnail())
//                    .error(R.drawable.placeholder)
//                    .placeholder(R.drawable.placeholder)
//                    .into(customViewHolder.imageView);
//        }

        //Setting text view title
        customViewHolder.emailView.setText(userEmail);
        customViewHolder.textView.setText(userName);
        customViewHolder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;
        protected TextView emailView;
        protected Button editBtn;
        protected Button deleteBtn;
        protected String userEmail;

        public CustomViewHolder(View view, final Context context) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textView = (TextView) view.findViewById(R.id.title);
            this.deleteBtn = (Button) view.findViewById(R.id.delete_user);
            this.editBtn = (Button) view.findViewById(R.id.edit_user);
            this.emailView = (TextView) view.findViewById(R.id.email);

            Intent intent = ((Activity) context).getIntent();
            this.userEmail = intent.getStringExtra("emailUser");


            this.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uEmail = emailView.getText().toString();
                    Intent i = new Intent(v.getContext(), AddEditUserActivity.class);
                    Bundle b = new Bundle();
                    b.putString("emailUser", userEmail);
                    b.putString("email",uEmail);
                    b.putBoolean("edit", true);
                    i.putExtras(b);
                    v.getContext().startActivity(i);
                }
            });

            this.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uEmail = emailView.getText().toString();
                    Intent i = new Intent(v.getContext(), MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString("emailUser", userEmail);
                    b.putBoolean("delete", true);
                    b.putString("emailDel",uEmail);
                    i.putExtras(b);
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}