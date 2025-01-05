package com.sistem.penjualan.atk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class Dates {

    public String formatDate(String dateString) {
        try {
            Locale locale = Locale.forLanguageTag("id");
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM/dd/yy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy", locale);
            Date stringToDateFormat = originalFormat.parse(dateString);

            return targetFormat.format(stringToDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }
}