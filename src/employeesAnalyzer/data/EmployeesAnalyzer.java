/**
 * Class get path to file and analyze it.
 *
 * For proper path format and proper text file format, see Main.java
 * For proper date formats, see Entity.java
 *
 * @author Vladimir Vladimirov
 */

package employeesAnalyzer.data;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class EmployeesAnalyzer {

    private static DatesDTO findMatchingPeriod(Entity entity1, Entity entity2){

        LocalDate[] startEnd = findStartAndEndDates(entity1, entity2);
        LocalDate startingDate = startEnd[0];
        LocalDate endingDate = startEnd[1];

        int weekendDays = findWeekendDaysInPeriodInDays(entity1, entity2);

        Duration duration = Duration.between(startingDate.atStartOfDay(), endingDate.atStartOfDay());
        int diff = (int) duration.toDays() + 1; // including ending date
        if (diff < 0){
            diff = 0;
        }

        DatesDTO datesDTO = new DatesDTO(diff, weekendDays, startingDate, endingDate);

        return datesDTO;
    }

    //return first date as starting, second as ending
    //is no matching period -> endDate will be before startDate
    private static LocalDate[] findStartAndEndDates(Entity entity1, Entity entity2){

        LocalDate startingDate;
        LocalDate endingDate;

        if (entity1.getDateFrom().isBefore(entity2.getDateFrom())){
            startingDate = entity2.getDateFrom();
        }else{
            startingDate = entity1.getDateFrom();
        }

        if (entity1.getDateTo().isAfter(entity2.getDateTo())){
            endingDate = entity2.getDateTo();
        }else{
            endingDate = entity1.getDateTo();
        }

        return new LocalDate[]{startingDate, endingDate};
    }

    private static int findWeekendDaysInPeriodInDays(Entity entity1, Entity entity2){

        LocalDate[] dates = findStartAndEndDates(entity1, entity2);

        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];

        LocalDate iterateDate = startDate;
        int weekendDays = 0;

        while (iterateDate.isBefore(endDate) || iterateDate.equals(endDate) ){
            DayOfWeek day = iterateDate.getDayOfWeek();
            if (day.getValue() == 6 || day.getValue() == 7){
                weekendDays++;
            }
            iterateDate = iterateDate.plusDays(1);
        }

        return weekendDays;
    }

    //throws exception in case of overlapping
    private static void checkForOverlappingEntities(List<Entity> entities) throws FileAnalyzerException{

        for (int i = 0; i < entities.size() - 1; i++) {
            for (int j = i + 1; j < entities.size(); j++) {

                if (entities.get(i).getEmpId() == entities.get(j).getEmpId()
                    && entities.get(i).getProjectId() == entities.get(j).getProjectId()){

                    int days = findMatchingPeriod(entities.get(i), entities.get(j)).getWorkedDays();

                    if (days > 0){
                        throw new FileAnalyzerException("Overlapping dates in entities for the same employee and project");
                    }
                }
            }
        }
    }

    synchronized public static List<EmplWorkedTogetherDTO> findEmployeesWorkedMostTogether(List<Entity> entities) throws FileAnalyzerException {

        if (entities.size() < 2) {
            throw new FileAnalyzerException("Your file should contain more than 2 entities");
        }

        checkForOverlappingEntities(entities);

        ArrayList<EmplWorkedTogetherDTO> emplWorkedTogetherDTOList = new ArrayList<>();

        //find all project that had been worked together
        for (int i = 0; i < entities.size() - 1; i++) {
            for (int j = i + 1; j < entities.size(); j++) {

                if (entities.get(i).getEmpId() == entities.get(j).getEmpId()){
                    continue;
                }

                if (entities.get(i).getProjectId() == entities.get(j).getProjectId()){

                    DatesDTO datesDTO = findMatchingPeriod(entities.get(i), entities.get(j));

                    if (datesDTO.getWorkedDays() > 0){
                        emplWorkedTogetherDTOList.add(
                                new EmplWorkedTogetherDTO(
                                        entities.get(i).getEmpId(),
                                        entities.get(j).getEmpId(),
                                        entities.get(i).getProjectId(),
                                        datesDTO.getWorkedDays(),
                                        datesDTO.getWeekendDays(),
                                        datesDTO.getStartPeriod(),
                                        datesDTO.getEndPeriod()
                                )
                        );
                    }
                }
            }
        }

        //find employees worked most together
        HashMap<String, Integer> coupleHours = new HashMap<>();

        for (EmplWorkedTogetherDTO emplWorkedTogetherDTO : emplWorkedTogetherDTOList) {

            String couple = emplWorkedTogetherDTO.getEmployee1() + "," + emplWorkedTogetherDTO.getEmployee2();
            String reverseCouple = emplWorkedTogetherDTO.getEmployee2() + "," + emplWorkedTogetherDTO.getEmployee1();
            if (!coupleHours.containsKey(couple) && !coupleHours.containsKey(reverseCouple)){
                coupleHours.put(couple, 0);
            }
            if (coupleHours.containsKey(reverseCouple)){
                coupleHours.put(reverseCouple, coupleHours.get(reverseCouple) + emplWorkedTogetherDTO.getWorkedDays());
            }else {
                coupleHours.put(couple, coupleHours.get(couple) + emplWorkedTogetherDTO.getWorkedDays());
            }
        }

        TreeMap<Integer, String> orderedByHoursEntities = new TreeMap<>();
        Iterator it = coupleHours.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Integer> entry = (Map.Entry)it.next();
            orderedByHoursEntities.put(entry.getValue(), entry.getKey());
        }

        String winningCouple = orderedByHoursEntities.lastEntry().getValue();
        long winEmpl1 = Long.parseLong(winningCouple.split(",")[0]) ;
        long winEmpl2 = Long.parseLong(winningCouple.split(",")[1]);

        //generate list with employees and their projects
        List<EmplWorkedTogetherDTO> winningEmplList = new ArrayList<>();

        for(EmplWorkedTogetherDTO emplWorkedTogetherDTO : emplWorkedTogetherDTOList){
            if ((emplWorkedTogetherDTO.getEmployee1() == winEmpl1 && emplWorkedTogetherDTO.getEmployee2() == winEmpl2)
             || (emplWorkedTogetherDTO.getEmployee1() == winEmpl2 && emplWorkedTogetherDTO.getEmployee2() == winEmpl1)){

                winningEmplList.add(emplWorkedTogetherDTO);

            }
        }

        return winningEmplList;
    }
}
