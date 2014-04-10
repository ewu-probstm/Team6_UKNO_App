package com.ewu.UKNOapp.util;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
 
public class CalendarView extends LinearLayout {
	
    private static final int WEEK_7 = 7;
    private static final int MAX_WEEK = 6;
    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
        // dp-->px
        final float scale = context.getResources().getDisplayMetrics().density;
        final int padding = (int) (scale * 5);
        final int paddingR = (int) (scale * 15);
        final int paddintT = (int) (scale * 20);
        titleFontPx = scale * 30;
        // get today's date
        mToday = Calendar.getInstance();
        // title
        Log.v("CalendarView", "title part, show year and month");
        mTitle = new TextView(context);
        mTitle.setGravity(Gravity.CENTER_HORIZONTAL); //show in center
        mTitle.setTypeface(null, Typeface.BOLD); //bold font
        mTitle.setText("DEBUG");
        mTitle.setPadding(0, 0, 0, paddintT);
        addView(mTitle, new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        dayFontPx = mTitle.getTextSize(); //keep current font size
        defaultColor = mTitle.getTextColors().getDefaultColor();
        // week part
        Log.v("CalendarView", "week part,Å@sun mon tue wed thr fri sat");
        mWeekLayout = new LinearLayout(context); // week layout
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, beginningWeek); // set head of week
        SimpleDateFormat formatter = new SimpleDateFormat("E"); // format to get day
        for (int i = 0; i < WEEK_7; i++) {
            TextView textView = new TextView(context);
            textView.setText(formatter.format(cal.getTime())); // show day in text
            textView.setGravity(Gravity.CENTER_HORIZONTAL); // show in center
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                0, LayoutParams.WRAP_CONTENT);
            llp.weight = 1;
            mWeekLayout.addView(textView, llp);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        addView(mWeekLayout, new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        // calendar part, max 6 rows
        Log.v("CalendarView", "max 6 rows");
        for (int i = 0; i < MAX_WEEK; i++) {
            LinearLayout weekLine = new LinearLayout(context);
            mWeeks.add(weekLine);
            // create date view for one week
            for (int j = 0; j < WEEK_7; j++) {
                TextView dayView = new TextView(context);
                dayView.setText(String.valueOf((i * WEEK_7) + (j + 1))); // TODO:DEBUG
                dayView.setGravity(Gravity.RIGHT); // gravity right
                dayView.setPadding(0, padding, paddingR, padding);
                LinearLayout.LayoutParams llp =
                    new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
                llp.weight = 1;
                weekLine.addView(dayView, llp);
            }
            this.addView(weekLine, new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
        init(mToday.get(Calendar.YEAR), mToday.get(Calendar.MONTH));
    }
    // show configure
    public void init(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.clear(); // clear first
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int todayYear = mToday.get(Calendar.YEAR);
        int todayMonth = mToday.get(Calendar.MONTH);
        int todayDay = mToday.get(Calendar.DAY_OF_MONTH);
        // title, year month config
        //String formatString = mTitle.getContext().getString(R.string.format_month_year); // month_year format string
        mTitle.setTextSize(titleFontPx);
        // year month format
        SimpleDateFormat formatter = new SimpleDateFormat("M");
        mTitle.setText(formatter.format(cal.getTime()));
        // week part, sun mon tue wed thr fri sat
        Log.v("CalendarView", "Week Sun Mon Tue Wed Thr Fri Sat");
        Calendar week = Calendar.getInstance();
        week.set(Calendar.DAY_OF_WEEK, beginningWeek); // set head of the week
        SimpleDateFormat weekFormatter = new SimpleDateFormat("E"); // format for day
        for (int i = 0; i < WEEK_7; i++) {
            TextView textView = (TextView) mWeekLayout.getChildAt(i);
            textView.setText(weekFormatter.format(week.getTime())); // show day in text
            week.add(Calendar.DAY_OF_MONTH, 1);
        }
        // calc blank in begining of the calendar
        int skipCount; // number of blanks
        int firstDayMonthWeek = cal.get(Calendar.DAY_OF_WEEK); // day
        if (beginningWeek > firstDayMonthWeek) {
            skipCount = firstDayMonthWeek - beginningWeek + WEEK_7;
        } else {
            skipCount = firstDayMonthWeek - beginningWeek;
        }
        int lastDay = cal.getActualMaximum(Calendar.DATE); //last date of the month
        // create date
        int dayCounter = 1;
        for (int i = 0; i < MAX_WEEK; i++) {
            LinearLayout weekLayout = mWeeks.get(i);
            weekLayout.setBackgroundColor(defaultBackgroundColor);
            for (int j = 0; j < WEEK_7; j++) {
                TextView dayView = (TextView) weekLayout.getChildAt(j);
                if (i == 0 && skipCount > 0) {
                    dayView.setText(" ");
                    skipCount--;
                } else if (dayCounter <= lastDay) {
                    dayView.setText(String.valueOf(dayCounter));
                    // red font on today
                    if (todayYear == year
                        && todayMonth == month
                        && todayDay == dayCounter) {
                        dayView
                            .setTextColor(todayColor); // red font
                        dayView.setTypeface(null,
                            Typeface.BOLD); // bold
                        weekLayout
                            .setBackgroundColor(todayBackgroundColor); // background = gray
                    } else {
                        dayView.setTextColor(defaultColor);
                        dayView.setTypeface(null,
                            Typeface.NORMAL);
                    }
                    dayCounter++;
                } else {
                    dayView.setText(" ");
                }
            }
        }
    }
    /** keep first day of a week */
    public int beginningWeek = Calendar.SUNDAY;
    /** today's font color */
    public int todayColor = Color.RED;
    /** this week background color */
    public int todayBackgroundColor = Color.LTGRAY;
    /** regular week background color */
    public int defaultBackgroundColor = Color.TRANSPARENT;
    /** regular days font color */
    public int defaultColor;
    /** year and month font size */
    public float titleFontPx;
    /** date font size */
    public float dayFontPx;
    private TextView mTitle;
    private LinearLayout mWeekLayout;
    private ArrayList<LinearLayout> mWeeks = new ArrayList<LinearLayout>();
    private Calendar mToday;
}


