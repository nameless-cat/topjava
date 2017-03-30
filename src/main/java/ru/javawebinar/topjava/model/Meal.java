package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * GKislin
 * 11.01.2015.
 */
public class Meal {
    private static AtomicLong count = new AtomicLong(1);

    private long id;

    private LocalDateTime dateTime;

    private String description;

    private int calories;

    public Meal(LocalDateTime dateTime, String description, int calories)
    {
        this.id = count.getAndIncrement();
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public Meal(long id, LocalDateTime dateTime, String description, int calories)
    {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public long getId()
    {
        return id;
    }
}
