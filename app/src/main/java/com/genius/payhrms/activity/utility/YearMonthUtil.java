package com.genius.payhrms.activity.utility;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class YearMonthUtil {
    public static ArrayList<String> getPreviousCurrentNextYearList() {
        ArrayList<String> yearList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        yearList.add(String.valueOf(currentYear - 1));
        yearList.add(String.valueOf(currentYear));
        yearList.add(String.valueOf(currentYear + 1));

        return yearList;
    }

    public static ArrayList<String> getFinancialYearList() {
        ArrayList<String> yearList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        String PreviousYear = String.valueOf(currentYear - 1);
        String CurrentYear = String.valueOf(currentYear);
        String NextYear = String.valueOf(currentYear + 1);
        String NextNextYear = String.valueOf(currentYear + 2);
        yearList.add(PreviousYear+"-"+CurrentYear);
        yearList.add(CurrentYear+"-"+NextYear);
        yearList.add(NextYear+"-"+NextNextYear);
        return yearList;
    }


    public static ArrayList<String> getMonthNumberList() {
        ArrayList<String> monthList = new ArrayList<>();
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < 12; i++) {   // only 12 months
            monthList.add(months[i]);
        }
        return monthList;
    }
}
