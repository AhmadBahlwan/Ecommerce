package com.shopping.admin.user;

import com.shopping.library.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter{

    public void export(List<User> users, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String [] csvHeader = {"User Id", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
        String[] fields = {"id", "email", "firstName", "lastName", "roles", "enabled"};

        csvWriter.writeHeader(csvHeader);
        for (User user: users) {
            csvWriter.write(user, fields);
        }
        csvWriter.close();
    }
}
