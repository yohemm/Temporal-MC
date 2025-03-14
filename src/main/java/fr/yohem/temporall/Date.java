package fr.yohem.temporall;

import java.util.Arrays;
import java.util.List;

public class Date {
  private int year = 2001;
  private int month = 10;
  private int day = 9;
  public static List<String> monthsStrings = Arrays.asList("janvier", "février", "mars", "avril", "mai", "juin",
      "juillet",
      "aout",
      "septembre", "octobre", "novembre", "decembre");

  static public int maxDayOfAnMonth(int month, int year) {
    int maxOfMonth = 30;
    switch (month) {
      case 1:
      case 3:
      case 5:
      case 7:
      case 8:
      case 10:
      case 12:
        maxOfMonth = 31;
        break;
      case 2: {
        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
          maxOfMonth = 29;
        else
          maxOfMonth = 28;
      }
        break;
    }
    return maxOfMonth;
  }

  public Date(int date) {
    this(date / 10000, date % 10000 / 100, date % 100);
  }

  public Date(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  private int getNbDaysForNextMonth() {
    return maxDayOfAnMonth(month, year) - day + 1;
  }

  public void addDay(long nbDays) {

    long dayToAdd = nbDays;
    long daySub = 0;
    int tempYear = year;
    int tempMonth = month;
    int tempDay = day;

    while (dayToAdd > 0) {
      int maxOfMonth = maxDayOfAnMonth(tempMonth, tempYear);
      if (maxOfMonth < dayToAdd + (tempDay)) {
        daySub += maxOfMonth - (tempDay);
        dayToAdd -= maxOfMonth - (tempDay);
        tempDay = 0;
        tempMonth++;
        if (tempMonth > 12) {
          tempMonth = 1;
          tempYear++;
        }
      } else {
        tempDay += dayToAdd;
        dayToAdd = 0;
      }
    }
    day = tempDay;
    year = tempYear;
    month = tempMonth;
  }

  static public int getDataDate(Date date) {
    return date.year * 10000 + date.month * 100 + (int) date.day;
  }

  static Date clone(Date oldDate) {
    return new Date(Date.getDataDate(oldDate));
  }

  static long diffDataDate(Date olderDate, Date earlierDate) {
    return getDataDate(earlierDate) - getDataDate(olderDate);
  }

  static long diffDate(Date olderDate, Date earlierDate) {
    Date older = clone(olderDate);
    Date earlier = clone(earlierDate);
    int res = 0;
    while (!older.equals(earlier)) {
      int toAdd = 0;
      if (older.year != earlier.year || older.month != earlier.month) {
        toAdd = older.getNbDaysForNextMonth();
      } else {
        toAdd = earlier.day - older.day;
      }
      older.addDay(toAdd);
      res += toAdd;
    }
    return res;
  }

  @Override
  public String toString() {
    return day + " " + monthsStrings.get(month - 1) + " " + year;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + year;
    result = prime * result + month;
    result = prime * result + day;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Date other = (Date) obj;
    if (year != other.year)
      return false;
    if (month != other.month)
      return false;
    if (day != other.day)
      return false;
    return true;
  }

}
