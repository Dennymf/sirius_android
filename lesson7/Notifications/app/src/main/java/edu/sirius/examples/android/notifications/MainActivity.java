package edu.sirius.examples.android.notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();

    public static List<String> messages = new ArrayList<>();
    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.send_notification_1).setOnClickListener(v -> createSimpleNotification(MainActivity.this));
        findViewById(R.id.send_notification_2).setOnClickListener(v -> createGroupNotification(MainActivity.this));
        findViewById(R.id.send_notification_3).setOnClickListener(v -> createActionNotification(MainActivity.this));
        findViewById(R.id.send_notification_4).setOnClickListener(v -> createProgressNotification(MainActivity.this));
        findViewById(R.id.send_notification_5).setOnClickListener(v -> createChannelNotification(MainActivity.this));
    }

    void createSimpleNotification(Context context) {
        Intent notificationIntent = new Intent(context, HelperActivity.class);
        notificationIntent.putExtra("notification", true);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        String msg = "" + count + " - Создали обычную нотификацию. Для этого мы использовали Intent, PendingIntent, каналы для версий, старших O, и сохранили нотификацию в системном сервисе";
        ++count;
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setTicker("Последнее китайское предупреждение")
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setAutoCancel(true)
                .setContentTitle("Простая нотификация")
                .setContentText(msg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(getChannelId("my_channel_id", "My channel", "my_group_id", "My group"));
        }

        int defaults = 0;
        defaults |= Notification.DEFAULT_VIBRATE;
        defaults |= Notification.DEFAULT_SOUND;

        builder.setDefaults(defaults);

        Notification nc = builder.build();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(10, nc);
        }
    }

    void createGroupNotification(Context context) {
        Intent notificationIntent = new Intent(context, HelperActivity.class);
        notificationIntent.putExtra("notification", true);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 2, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        String msg = "" + count + " - Создали групповую нотификацию. Для этого мы использовали Intent, PendingIntent, каналы для версий, старших O, и сохранили нотификацию в системном сервисе";
        ++count;
        messages.add(msg);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setTicker("Последнее китайское предупреждение")
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setAutoCancel(true)
                .setUsesChronometer(true)
                .setContentTitle("Простая нотификация")
                .setContentText(msg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(getChannelId("my_channel_id", "My channel", "my_group_id", "My group"));
        }

        Notification.InboxStyle inbox = new Notification.InboxStyle(builder);

        int count = 0;
        int more = 0;
        for (String m : messages) {
            if (count <= 5) {
                inbox.addLine(m);
            } else {
                ++more;
            }
            ++count;
        };

        if (more > 0) {
            inbox.setSummaryText("+ " + more + " нотификаций");
        }
        Notification nc = inbox.build();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(11, nc);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    void createActionNotification(Context context) {
        Intent notificationIntent = new Intent(context, HelperActivity.class);
        notificationIntent.putExtra("notification", true);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                3, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent notificationTodo = new Intent(context, HelperActivity.class);
        notificationIntent.putExtra("task", true);
        PendingIntent piTodo = PendingIntent.getActivity(context,
                1, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String msg = "" + count + " - Надо что-то сделать";
        count++;

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setTicker("Последнее китайское предупреждение")
                .setWhen(System.currentTimeMillis())
                .addAction(new Notification.Action.Builder(R.drawable.ic_launcher_background, "Previous", piTodo).build())
                .setShowWhen(true)
                .setAutoCancel(true)
                .setContentTitle("To do")
                .setContentText(msg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(getChannelId("my_channel_id", "My default Channel", "my_group_id", "My default Group"));
        }

        int defaults = 0;
        defaults |= Notification.DEFAULT_VIBRATE;
        defaults |= Notification.DEFAULT_SOUND;
        builder.setDefaults(defaults);

        Notification nc = builder.build();

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(10, nc);
        }
    }

    void createChannelNotification(Context context) {
        Intent notificationIntent = new Intent(context, HelperActivity.class);
        notificationIntent.putExtra("notification", true);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        String msg = "" + count + " - Пока уже таки покормить рыбок, они почти сдохли, это специально длинный текст такой чтобы не влезло";
        count++;

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setTicker("Последнее китайское предупреждениеы")
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setAutoCancel(true)
                .setContentTitle("Напоминание")
                .setContentText(msg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(getChannelId("my_second_channel_id", "My second Channel", "my_second_group_id", "My second Group"));
        }
        int defaults = 0;
        defaults |= Notification.DEFAULT_VIBRATE;
        defaults |= Notification.DEFAULT_SOUND;

        builder.setDefaults(defaults);

        Notification nc = builder.build();

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(35, nc);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    String getChannelId(String channelId, String name, String groupId, String groupName) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            List<NotificationChannel> channels = nm.getNotificationChannels();
            if (channels.stream().anyMatch(ch -> ch.getId().equals(channelId))) {
                return channelId;
            }

            String group = getNotificationChannelGroupId(groupId, groupName);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, name, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);

            notificationChannel.setGroup(group);

            notificationChannel.setVibrationPattern(new long[]{ 1, 2, 3, 4, 5, 4, 3, 2, 1 });
            nm.createNotificationChannel(notificationChannel);

            return channelId;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.O)
    String getNotificationChannelGroupId(String groupId, String name) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            List<NotificationChannelGroup> groups = nm.getNotificationChannelGroups();
            if (groups.stream().anyMatch(g -> g.getId().equals(groupId))) {
                return groupId;
            }
            nm.createNotificationChannelGroup(new NotificationChannelGroup(groupId, name));
            return groupId;
        }
        return null;
    }

    void createProgressNotification(Context context) {
        String msg = "" + count + " - Нотификация с индикатором прогресса";
        count++;

        Intent notificationIntent = new Intent(context, HelperActivity.class);
        notificationIntent.putExtra("notification", true);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                10, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        final NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("To do")
                .setContentText(msg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(getChannelId("my_channel_id", "My default Channel", "my_group_id", "My default Group"));
        }

// Start a lengthy operation in a background thread
        new Thread(
                () -> {
                    int incr;
                    // Do the "lengthy" operation 20 times
                    for (incr = 0; incr <= 100; incr+=5) {
                        // Sets the progress indicator to a max value, the
                        // current completion percentage, and "determinate"
                        // state
                        builder.setProgress(100, incr, false);
                        // Displays the progress bar for the first time.
                        nm.notify(0, builder.build());
                        // Sleeps the thread, simulating an operation
                        // that takes time
                        try {
                            // Sleep for 5 seconds
                            Thread.sleep(5*1000);
                        } catch (InterruptedException e) {
                            Log.d(TAG, "sleep failure");
                        }
                    }
                    // When the loop is finished, updates the notification
                    builder.setContentText("Выполнение завершено")
                            // Removes the progress bar
                            .setProgress(0,0,false);
                    nm.notify(123, builder.build());
                }
// Starts the thread by calling the run() method in its Runnable
        ).start();
    }


}
