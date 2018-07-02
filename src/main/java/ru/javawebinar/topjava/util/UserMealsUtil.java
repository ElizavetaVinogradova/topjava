package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(22, 0), 2000);
        filteredWithExceeded.forEach(System.out::println);
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime,
                                                                    LocalTime endTime, int caloriesPerDay) {
        /*List<UserMealWithExceed> list = new ArrayList<>();
        List<UserMeal> subList = new ArrayList<>();
        HashMap<LocalDate, Integer> calToDate = new HashMap<>();
        Collections.sort(mealList, Comparator.comparing(UserMeal::getDateTime));
        for (UserMeal userMeal:mealList) {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                subList.add(new UserMeal(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories()));
        }
        for (UserMeal userMeal : subList) {
            if (!calToDate.containsKey(userMeal.getDateTime().toLocalDate())) {
                calToDate.put(userMeal.getDateTime().toLocalDate(), userMeal.getCalories());
            } else{
                Integer countCal = calToDate.get(userMeal.getDateTime().toLocalDate());
                countCal += userMeal.getCalories();
                calToDate.put(userMeal.getDateTime().toLocalDate(), countCal);
            }
        }
        for (UserMeal userMeal : subList) {
            if (calToDate.get(userMeal.getDateTime().toLocalDate())>caloriesPerDay){
                list.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),false));
            }else {
                list.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),true));
            }
        }
        list.forEach(s -> System.out.println(s.toString()));
        return list;*/



        //группировка листа в мап, где ключ - дата, значение - список значение по этой дате:
        Map<LocalDate, Integer> caloriesSumByDate = mealList.stream().collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream().filter(um -> TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(),
                        caloriesSumByDate.get(um.getDateTime().toLocalDate())>caloriesPerDay))
                .collect(Collectors.toList());

    }
}
