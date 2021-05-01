/**
 * @author Vladimir Vladimirov
 */

package employeesAnalyzer.data;

import java.time.LocalDate;

public class DatesDTO {

    int workedDays;
    int weekendDays;
    LocalDate startPeriod;
    LocalDate endPeriod;

    public DatesDTO(int workedDays, int weekendDays, LocalDate startPeriod, LocalDate endPeriod) {
        this.workedDays = workedDays;
        this.weekendDays = weekendDays;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public int getWorkedDays() {
        return workedDays;
    }

    public int getWeekendDays() {
        return weekendDays;
    }

    public LocalDate getStartPeriod() {
        return startPeriod;
    }

    public LocalDate getEndPeriod() {
        return endPeriod;
    }

}
