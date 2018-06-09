package saar.roy.matchpoint.services;

import java.util.ArrayList;

import saar.roy.matchpoint.data.Notification;

/**
 * Created by Roy-PC on 23-Apr-18.
 */

public class NotificationServices {
    private static NotificationServices instance;
    private ArrayList<Notification> notifications;

    public static NotificationServices getInstance() {
        if (instance == null)
            instance = new NotificationServices();
        return instance;
    }

    private NotificationServices(){
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }public int yes()
}
