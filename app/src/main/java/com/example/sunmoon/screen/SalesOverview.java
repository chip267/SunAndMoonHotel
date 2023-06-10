package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunmoon.MainActivity;
import com.example.sunmoon.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SalesOverview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private LineChart lineChart;
    private TextView textviewNum;
    private TextView textviewToday;
    private TextView textviewMonthly;
    private TextView textviewWeekly;
    ImageView imageViewBack;
    private AppCompatButton saleReportButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_overview);
        spinner = findViewById(R.id.spinner_timesale);
        lineChart = findViewById(R.id.lineChart);
        textviewNum = findViewById(R.id.tv_numrevenue);
        textviewToday = findViewById(R.id.tv_numsale);
        textviewWeekly=findViewById(R.id.tv_numweekly);
        textviewMonthly=findViewById(R.id.tv_nummonthly);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        showRevenueToday();
        showCompareWeek();
        showCompareMonth();
        imageViewBack = findViewById(R.id.imageViewBackhome);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesOverview.this, Home.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        saleReportButton = findViewById(R.id.btn_Report);
        saleReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesOverview.this, SalesReport.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void showCompareMonth() {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference().child("Booking");
        Query salesQuery = bookingsRef.orderByChild("checkoutDate");
        salesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalSalesCurrentMonth = 0;
                int totalSalesPreviousMonth = 0;
                Calendar calendar = Calendar.getInstance();
                int currentMonth = calendar.get(Calendar.MONTH);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String checkoutDate = snapshot.child("checkoutDate").getValue(String.class);
                    Date date = convertStringToDate(checkoutDate);
                    calendar.setTime(date);
                    int bookingMonth = calendar.get(Calendar.MONTH);

                    int sales = snapshot.child("total").getValue(Integer.class);

                    if (bookingMonth == currentMonth) {
                        totalSalesCurrentMonth += sales;
                    } else if (bookingMonth == currentMonth - 1) {
                        totalSalesPreviousMonth += sales;
                    }
                }
                float percentageDifference;
                if (totalSalesPreviousMonth == 0) {
                    percentageDifference = totalSalesCurrentMonth > 0 ? Float.POSITIVE_INFINITY : 0;
                } else {
                    percentageDifference = ((float) totalSalesCurrentMonth / totalSalesPreviousMonth) * 100 - 100;
                }
                String percentageDifferenceString;
                if (percentageDifference > 0) {
                    percentageDifferenceString = String.format("+%.1f%%", percentageDifference);
                } else {
                    percentageDifferenceString = String.format("%.1f%%", percentageDifference);
                }
                textviewMonthly.setText(percentageDifferenceString);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showCompareWeek() {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference().child("Booking");
        Query salesQuery = bookingsRef.orderByChild("checkoutDate");
        salesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalSalesCurrentWeek = 0;
                int totalSalesLastWeek = 0;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                Date startOfWeek = calendar.getTime();
                calendar.add(Calendar.DAY_OF_WEEK, 7);
                Date endOfWeek = calendar.getTime();
                calendar.add(Calendar.DAY_OF_WEEK, -14);
                Date startOfLastWeek = calendar.getTime();
                calendar.add(Calendar.DAY_OF_WEEK, 7);
                Date endOfLastWeek = calendar.getTime();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String checkoutDate = snapshot.child("checkoutDate").getValue(String.class);
                    Date date = convertStringToDate(checkoutDate);
                    if (date.after(startOfWeek) && date.before(endOfWeek)) {
                        int sales = snapshot.child("total").getValue(Integer.class);
                        totalSalesCurrentWeek += sales;
                    }
                    if (date.after(startOfLastWeek) && date.before(endOfLastWeek)) {
                        int sales = snapshot.child("total").getValue(Integer.class);
                        totalSalesLastWeek += sales;
                    }
                }
                float percentageDifference;
                if (totalSalesLastWeek == 0) {
                    percentageDifference = totalSalesCurrentWeek > 0 ? Float.POSITIVE_INFINITY : 0;
                } else {
                    percentageDifference = ((float) totalSalesCurrentWeek / totalSalesLastWeek) * 100 - 100;
                }
                String percentageDifferenceString;
                if (percentageDifference > 0) {
                    percentageDifferenceString = String.format("+%.1f%%", percentageDifference);
                } else {
                    percentageDifferenceString = String.format("%.1f%%", percentageDifference);
                }
                textviewWeekly.setText(percentageDifferenceString);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRevenueToday() {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference().child("Booking");
        Query salesQuery = bookingsRef.orderByChild("checkoutDate");
        salesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalSalesToday = 0;
                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                String today = convertDateToString(currentDate);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String checkoutDate = snapshot.child("checkoutDate").getValue(String.class);
                    if (checkoutDate.equals(today)) {
                        int totalSales = snapshot.child("total").getValue(Integer.class);
                        totalSalesToday += totalSales;
                    }
                }
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                symbols.setGroupingSeparator('.');
                DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                String totalSalesOfTodayString = decimalFormat.format(totalSalesToday) + " VND";
                textviewToday.setText(totalSalesOfTodayString);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        if (text.equals("Week")) {
            showLineChartForWeek();
        } else if (text.equals("Month")) {
            showLineChartForMonth();
        } else if (text.equals("Year")) {
            showLineChartForYear();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void showLineChartForWeek() {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference().child("Booking");
        Query salesQuery = bookingsRef.orderByChild("checkoutDate");
        salesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> salesByDate = new HashMap<>();
                List<String> datesWithSales = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                Date startOfWeek = calendar.getTime();
                calendar.add(Calendar.DAY_OF_WEEK, 7);
                Date endOfWeek = calendar.getTime();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String checkoutDate = snapshot.child("checkoutDate").getValue(String.class);
                    Date date = convertStringToDate(checkoutDate);
                    if (date.before(startOfWeek) || date.after(endOfWeek)) {
                        continue;
                    }
                    int totalSales = snapshot.child("total").getValue(Integer.class);
                    if (salesByDate.containsKey(checkoutDate)) {
                        int currentTotal = salesByDate.get(checkoutDate);
                        salesByDate.put(checkoutDate, currentTotal + totalSales);
                    } else {
                        salesByDate.put(checkoutDate, totalSales);
                        datesWithSales.add(checkoutDate);
                    }
                }
                Collections.sort(datesWithSales, new Comparator<String>() {
                    @Override
                    public int compare(String date1, String date2) {
                        Date d1 = convertStringToDate(date1);
                        Date d2 = convertStringToDate(date2);
                        return d1.compareTo(d2);
                    }
                });
                List<Entry> entries = new ArrayList<>();
                int index = 0;
                List<Date> dateRange = getCurrentWeekDateRange();
                for (Date date : dateRange) {
                    String formattedDate = convertDateToString(date);
                    int totalSales = salesByDate.containsKey(formattedDate) ? salesByDate.get(formattedDate) : 0;
                    entries.add(new Entry(index, totalSales));
                    index++;
                }
                LineDataSet dataSet = new LineDataSet(entries, "Total Sales");
                dataSet.setColor(ContextCompat.getColor(SalesOverview.this, R.color.lightbrown));
                dataSet.setCircleColor(ContextCompat.getColor(SalesOverview.this, R.color.lightbrown));
                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);
                lineChart.invalidate();
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
                    @Override
                    public String getFormattedValue(float value) {
                        int index = (int) value;
                        if (index >= 0 && index < dateRange.size()) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(dateRange.get(index));
                            return dateFormat.format(calendar.getTime());
                        }
                        return "";
                    }
                });
                int totalSalesOfWeek = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String checkoutDate = snapshot.child("checkoutDate").getValue(String.class);
                    Date date = convertStringToDate(checkoutDate);
                    if (date.before(startOfWeek) || date.after(endOfWeek)) {
                        continue;
                    }
                    int revenue = snapshot.child("total").getValue(Integer.class);
                    totalSalesOfWeek += revenue;
                }
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                symbols.setGroupingSeparator('.');
                DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                String totalSalesOfWeekString = decimalFormat.format(totalSalesOfWeek) + " VND";
                textviewNum.setText(totalSalesOfWeekString);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showLineChartForMonth() {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference().child("Booking");
        Query salesQuery = bookingsRef.orderByChild("checkoutDate");
        salesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> salesByDate = new HashMap<>();
                List<String> datesWithSales = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                List<Entry> entries = new ArrayList<>();
                int index = 0;
                List<Date> dateRange = getCurrentMonthDateRange();
                for (Date date : dateRange) {
                    String formattedDate = convertDateToString(date);
                    int totalRevenue = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String checkoutDate = snapshot.child("checkoutDate").getValue(String.class);
                        int revenue = snapshot.child("total").getValue(Integer.class);
                        if (checkoutDate.equals(formattedDate)) {
                            totalRevenue += revenue;
                        }
                    }
                    entries.add(new Entry(index, totalRevenue));
                    index++;
                }
                LineDataSet dataSet = new LineDataSet(entries, "Total Sales");
                dataSet.setColor(ContextCompat.getColor(SalesOverview.this, R.color.lightbrown));
                dataSet.setCircleColor(ContextCompat.getColor(SalesOverview.this, R.color.lightbrown));
                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);
                lineChart.invalidate();
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
                    @Override
                    public String getFormattedValue(float value) {
                        int index = (int) value;
                        if (index >= 0 && index < dateRange.size()) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(dateRange.get(index));
                            return dateFormat.format(calendar.getTime());
                        }
                        return "";
                    }
                });
                int totalRevenueOfMonth = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String checkoutDate = snapshot.child("checkoutDate").getValue(String.class);
                    Date date = convertStringToDate(checkoutDate);
                    calendar.setTime(date);
                    int bookingYear = calendar.get(Calendar.YEAR);
                    int bookingMonth = calendar.get(Calendar.MONTH);
                    if (currentYear == bookingYear && currentMonth == bookingMonth) {
                        int sales = snapshot.child("total").getValue(Integer.class);
                        totalRevenueOfMonth += sales;
                    }
                }
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                symbols.setGroupingSeparator('.');
                DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                String totalSalesOfMonthString = decimalFormat.format(totalRevenueOfMonth) + " VND";
                textviewNum.setText(totalSalesOfMonthString);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error, if any
            }
        });
    }
    private void showLineChartForYear() {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference().child("Booking");
        Query salesQuery = bookingsRef.orderByChild("checkoutDate");
        salesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> salesByMonth = new HashMap<>();
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                Date startOfYear = calendar.getTime();
                calendar.add(Calendar.YEAR, 1);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                Date endOfYear = calendar.getTime();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String checkoutDate = snapshot.child("checkoutDate").getValue(String.class);
                    int totalSales = snapshot.child("total").getValue(Integer.class);
                    Date date = convertStringToDate(checkoutDate);
                    calendar.setTime(date);
                    int month = calendar.get(Calendar.MONTH);
                    String monthString = getMonthString(month);
                    if (salesByMonth.containsKey(monthString)) {
                        int currentTotal = salesByMonth.get(monthString);
                        salesByMonth.put(monthString, currentTotal + totalSales);
                    } else {
                        salesByMonth.put(monthString, totalSales);
                    }
                }
                List<Entry> entries = new ArrayList<>();
                int index = 0;
                for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
                    String monthString = getMonthString(month);
                    int totalSales = salesByMonth.containsKey(monthString) ? salesByMonth.get(monthString) : 0;
                    entries.add(new Entry(index, totalSales));
                    index++;
                }
                LineDataSet dataSet = new LineDataSet(entries, "Total Sales");
                dataSet.setColor(ContextCompat.getColor(SalesOverview.this, R.color.lightbrown));
                dataSet.setCircleColor(ContextCompat.getColor(SalesOverview.this, R.color.lightbrown));
                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);
                lineChart.invalidate();
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        int monthIndex = (int) value;
                        int monthNumber = (monthIndex % 12) + 1;
                        return String.format("%02d", monthNumber);
                    }
                });
                xAxis.setLabelCount(12);
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);
                int totalSalesOfYear = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String checkoutDate = snapshot.child("checkoutDate").getValue(String.class);
                    Date date = convertStringToDate(checkoutDate);
                    if (date.before(startOfYear) || date.after(endOfYear)) {
                        continue;
                    }
                    int revenue = snapshot.child("total").getValue(Integer.class);
                    totalSalesOfYear += revenue; // Add sales to the total sales for the year
                }
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                symbols.setGroupingSeparator('.');
                DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                String totalSalesOfYearString = decimalFormat.format(totalSalesOfYear) + " VND";
                textviewNum.setText(totalSalesOfYearString);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private List<Date> getCurrentWeekDateRange() {
        List<Date> dateRange = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        int daysOffset = firstDayOfWeek - currentDayOfWeek;
        calendar.add(Calendar.DAY_OF_WEEK, daysOffset);
        for (int i = 0; i < 7; i++) {
            dateRange.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        return dateRange;
    }
    private List<Date> getCurrentMonthDateRange() {
        List<Date> dateRange = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int currentMonth = calendar.get(Calendar.MONTH);
        while (calendar.get(Calendar.MONTH) == currentMonth) {
            dateRange.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dateRange;
    }
    private String convertDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }
    private Date convertStringToDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String getMonthString(int month) {
        DateFormatSymbols dfs = new DateFormatSymbols();
        return dfs.getMonths()[month];
    }
}