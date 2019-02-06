package com.ripani.perren.amherdt.birrapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.ripani.perren.amherdt.birrapp.modelo.LocalDao;
import com.ripani.perren.amherdt.birrapp.modelo.MyDataBase;

public class NotificationReceiver extends BroadcastReceiver {



    public void onReceive(Context context, Intent intent) {


        Bundle b = intent.getExtras();
        long idLocal = (long) b.get("idLocal");
        String nombreLocal = (String) b.get("nombreLocal");

        //deberia ir al perfil del local
        Intent destino = new Intent(context, MainActivity.class);
        //deberia ir al perfil del local


        destino.putExtra("idLocal", idLocal);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 99, destino, PendingIntent.FLAG_UPDATE_CURRENT);



        Notification notification = new NotificationCompat.Builder(context, "CANAL01")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
                .setContentTitle("Tu local ha sido creado")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("ID: " + idLocal)
                        .addLine("Nombre:" + nombreLocal))
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        manager.notify(99, notification);


    }
}
