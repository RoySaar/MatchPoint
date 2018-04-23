package saar.roy.matchpoint.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.Notification;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Roy-PC on 23-Apr-18.
 */

    public class NotificaitonAdapter extends ArrayAdapter<Notification> {

        private AssetManager assetManager = getContext().getAssets();
        private LayoutInflater lif;

        public NotificaitonAdapter(Context context, List<Notification> notifications) {
            super(context, 0, notifications);
            lif = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        public List<Notification> getNotifications() {
            List<Notification> notifications = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                notifications.add(getItem(i));
            }
            return notifications;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Notification notification = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_invite, parent, false);
            }
            final Typeface tvFont = Typeface.createFromAsset(assetManager, "fonts/assistant_semibold.ttf");
            return convertView;
        }
}
