package barqsoft.footballscores.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.scoresAdapter;

/**
 * Created by amhamogus on 2/10/16.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballRemoteViewFactory(this, intent);
    }

    class FootballRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

        int count;
        Context appContext;
        Cursor cursor;

        public FootballRemoteViewFactory(Context context, Intent intent) {
            appContext = context;
        }

        @Override
        public void onCreate() {

            // Get today's date
            String[] todayDate = new String[1];
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 0);
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            todayDate[0] = mformat.format(calendar.getTime());

            // Get matches based on today's date
            cursor = getContentResolver().query(
                    DatabaseContract.scores_table.buildScoreWithDate(),
                    null,
                    null,
                    todayDate,
                    null
            );
            count = cursor.getCount();

            // Check if we have matches for today
            if (!cursor.moveToNext()) {
                Log.d("AMHA", "failed to get data from the content provider");
            } else {
                Log.d("AMHA", "Cursor count = " + cursor.getCount());
            }
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews view =
                    new RemoteViews(appContext.getPackageName(), R.layout.widget_list_item);

            // Todo: map data to views here
            cursor.moveToPosition(position);
            view.setTextViewText(R.id.widget_home_team, cursor.getString(scoresAdapter.COL_HOME));
            view.setTextViewText(R.id.widget_away_team, cursor.getString(scoresAdapter.COL_AWAY));

            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
