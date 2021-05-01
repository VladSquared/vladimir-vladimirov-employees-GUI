package employeesAnalyzer.data;

public class WinningEmplProjectsDTO {
    private long projectId;
    private int totalDays;
    private int weekendDays;

    public WinningEmplProjectsDTO(long projectId, int totalDays, int weekendDays) {
        this.projectId = projectId;
        this.totalDays = totalDays;
        this.weekendDays = weekendDays;
    }

    public long getProjectId() {
        return projectId;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public int getWeekendDays() {
        return weekendDays;
    }
}
