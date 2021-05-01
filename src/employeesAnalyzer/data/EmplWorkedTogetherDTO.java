/**
 * @author Vladimir Vladimirov
 */

package employeesAnalyzer.data;

import java.time.LocalDate;

public class EmplWorkedTogetherDTO {
    long employee1;
    long employee2;
    long workedOnProject;
    int workedDays;
    int weekendDays;
    LocalDate startPeriod;
    LocalDate endPeriod;

    public EmplWorkedTogetherDTO(long employee1, long employee2, long workedOnProject, int workedDays, int weekendDays,
                                 LocalDate startPeriod, LocalDate endPeriod) {
        this.employee1 = employee1;
        this.employee2 = employee2;
        this.workedOnProject = workedOnProject;
        this.workedDays = workedDays;
        this.weekendDays = weekendDays;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public long getEmployee1() {
        return employee1;
    }

    public long getEmployee2() {
        return employee2;
    }

    public long getWorkedOnProject() {
        return workedOnProject;
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
