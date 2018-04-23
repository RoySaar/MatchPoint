package saar.roy.matchpoint.ui;

import android.content.Context;

import dmax.dialog.SpotsDialog;
import saar.roy.matchpoint.R;

/**
 * Created by Roy-PC on 22-Apr-18.
 */

public class SpotsDialogHandler {
    private static SpotsDialogHandler instance = null;
    private SpotsDialog dialog;
    private String text;

    public static SpotsDialogHandler getInstance() {
        if (instance == null) {
            instance = new SpotsDialogHandler();
        }
        return instance;
    }

    private SpotsDialogHandler(){
    }

    public void show(Context context){
        dialog = new SpotsDialog(context,R.style.LoadingDialog);
        dialog.show();
    }

    public void show(Context context,int style){
        dialog = new SpotsDialog(context,style);
        dialog.show();
    }

    public void hide(){
        dialog.hide();
    }

    public void setText (String text) {
        this.text = text;
    }

    public boolean isShowing(){
        if (dialog == null)
            return false;
        else
        return dialog.isShowing();
    }

}
