package optimisticapps.v_taskmangement;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import optimisticapps.v_taskmangement.db.DBAdapter;

public class MainActivity extends AppCompatActivity {
    DBAdapter myDb;
    private ListView mTaskListView;
    private TextView etDate;
    private TextView currentTaskTime;
    private View currentView;
    private Calendar calendar = Calendar.getInstance();
    private Cursor cursor;
    private int counter = 0;
    private  AlarmManager alarmManager = null;
    private HashMap<String,Integer> ids = new HashMap<>();
    private HashMap<String,Notification> notifications = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTaskListView = (ListView)findViewById(R.id.list_todo);
        //Show the current date
        etDate = (TextView) findViewById(R.id.currentDate);
        SimpleDateFormat date = new SimpleDateFormat( "EEEE, MMMM d, yyyy" );
        etDate.setText( date.format( new Date()));
        //create notification channel
        createChannel();
        //Prepare for updateUI method
        myDb = new DBAdapter(this);
        updateUI();
    }

    private void createChannel(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "ScheduleChannel";
        String name = "ScheduleApp";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.enableLights(true);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);
    }

    private void scheduleNotification(Notification notification, int hour, int minute) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, counter++, notificationIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 00);
        ids.put(hour+":"+minute,counter);
        notifications.put(hour+":"+minute,notification);
        if (alarmManager == null)
            alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours
    }

    private Notification getNotification(String content) {
        return new Notification.Builder(getApplicationContext(), "ScheduleChannel")
                .setContentTitle(content)
                .setSmallIcon(R.mipmap.app_logo_rounded)
                .setAutoCancel(true).build();
    }

    protected void onDestroy() {
        super.onDestroy();
        cursor.close(); // avoid memory leak warning
    }

    public void addTask(View view) {
        switch (view.getId()) {
            case R.id.add_task:
                myDb.open();
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("What do you want to do ?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                myDb.insertRow_void(task,"__:__");
                                myDb.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
        }
    }

    private void updateUI() {
        myDb.open();
        cursor = myDb.getAllRows();
        String[] fromFieldsName = new String[] {DBAdapter.KEY_NAME,DBAdapter.KEY_TIME};
        int[] toViewID = new int[] {R.id.task_title,R.id.taskTime};
        SimpleCursorAdapterColor cursorAdapter = new SimpleCursorAdapterColor(this, R.layout.item_todo,cursor,fromFieldsName,toViewID);
        mTaskListView.setAdapter(cursorAdapter);
        myDb.close();
    }

    public void deleteTask(View view) {
        myDb.open();
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title); //get the name of the task
        String taskName = String.valueOf(taskTextView.getText());
        TextView taskTimeView = parent.findViewById(R.id.taskTime); //get the time of the task
        String taskTime = String.valueOf(taskTimeView.getText());
        myDb.deleteRowByNameAndTime(taskName,taskTime);
        myDb.close();
        if (taskTime.compareTo("__:__")!=0) {cancelSchedule(taskName,taskTime);} // cancel the intent
        updateUI();
        // Toast notification
        Context context = getApplicationContext();
        CharSequence text = "Task ' " + taskName + " ' has been removed";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void cancelSchedule(String name, String time){
        try{
            Intent notificationIntent = new Intent(this, NotificationPublisher.class);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notifications.get(time));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ids.get(time), notificationIntent, 0);
            alarmManager.cancel(pendingIntent);

        }catch (Exception ex){
            Context context = getApplicationContext();
            CharSequence text = "error";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void addTime (View view){
        myDb.open();
        currentView = (View) view.getParent();
        currentTaskTime = currentView.findViewById(R.id.taskTime); // current task time field
        new TimePickerDialog(MainActivity.this,onTimeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            String minute;
            String hour;
            if (i<10){hour = "0" + i;}
            else {hour = "" + i;}
            if (i1<10){minute = "0" + i1;}
            else {minute = "" + i1;}
            currentTaskTime.setText(hour + ":" + minute);
            addTimeAid(hour, minute);
        }
    };

    public void addTimeAid(String hour, String minute){
        TextView taskTextView = currentView.findViewById(R.id.task_title);
        String taskName = String.valueOf(taskTextView.getText());
        long id = myDb.getRowByName(taskName);
        myDb.updateRow(id, hour + ":" + minute);
        myDb.close();
        scheduleNotification(getNotification(taskName), Integer.parseInt(hour), Integer.parseInt(minute)); //-3 : it is optimization problem with this api
        updateUI();
    }
}
