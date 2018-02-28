package optimisticapps.v_taskmangement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SimpleCursorAdapterColor extends SimpleCursorAdapter {

    public SimpleCursorAdapterColor(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView timeText;
        DateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        timeText = view.findViewById(R.id.taskTime);
        if (timeText.getText().toString().compareTo(date) < 0){
            timeText.setTextColor(0xC87D0112);
        }
        return view;
    }
}