/**
 * Contains the data from 1 row / entity in the text
 *
 * <p>acceptable data formats:
 * "yyyy-MM-d", "MM/d/yyyy", "yyyy/MM/dd", "dd-M-yyyy hh:mm:ss",
 * "dd MMMM yyyy zzzz", "E, dd MMM yyyy HH:mm:ss z", "M/D/YY",
 * "D/M/YY", "YY/M/D", "MMDDYY", "DDMMYY",
 * "YYMMDD", "dd-MMM-yyyy", "dd MMMM yyyy"
 *
 * Extendable with other data formats without "," character
 *
 * @author Vladimir Vladimirov
 */

package employeesAnalyzer.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Entity {

    private long empId;
    private long projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    private Entity(long empId, long projectId, LocalDate dateFrom, LocalDate dateTo) throws FileAnalyzerException {

        if (empId > 0) {
            this.empId = empId;
        }

        if (projectId > 0) {
            this.projectId = projectId;
        }

        if (dateFrom.isBefore(LocalDate.now()) && (dateFrom.isBefore(dateTo) || dateFrom.isEqual(dateTo))) {
            this.dateFrom = dateFrom;
        } else {
            throw new FileAnalyzerException("Incorrect DateFrom");
        }

        if (dateTo.isBefore(LocalDate.now()) || dateTo.isEqual(LocalDate.now())) {
            this.dateTo = dateTo;
        } else {
            throw new FileAnalyzerException("Incorrect DateTo");
        }

    }

    static Entity getEmployeeFromString(String employeeLineFromFile) throws FileAnalyzerException {

        long empId;
        long projectId;
        LocalDate dateFrom;
        LocalDate dateTo;

        try {
            String[] empData = employeeLineFromFile.split(",");
            for (int i = 0; i < empData.length; i++) {
                empData[i] = empData[i].trim();
            }

            empId = Long.parseLong(empData[0]);
            projectId = Long.parseLong(empData[1]);
            dateFrom = findDate(empData[2]);
            if (empData[3].toUpperCase().equals("NULL")) {
                dateTo = LocalDate.now();
            } else {
                dateTo = findDate(empData[3]);
            }
        } catch (Exception e) {
            throw new FileAnalyzerException("Invalid data. " + e.getMessage());
        }

        return new Entity(empId, projectId, dateFrom, dateTo);
    }

    private static LocalDate findDate(String date) {

        List<String> allowedDateFormats = List.of(
                "yyyy-MM-d", "MM/d/yyyy", "yyyy/MM/dd", "dd-M-yyyy hh:mm:ss",
                "dd MMMM yyyy zzzz", "E, dd MMM yyyy HH:mm:ss z", "M/D/YY",
                "D/M/YY", "YY/M/D", "MMDDYY", "DDMMYY",
                "YYMMDD", "dd-MMM-yyyy", "dd MMMM yyyy"
                );

        for(String format : allowedDateFormats){
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                LocalDate correctDate = LocalDate.parse(date, formatter);
                return correctDate;
            }catch (DateTimeParseException e){
            }

        }

        return LocalDate.parse(date);//it will cause DateTimeParseException
    }

    public long getEmpId() {
        return empId;
    }

    public long getProjectId() {
        return projectId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }
}
