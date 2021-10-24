/*
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.swingx.calendar;


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.jdesktop.swingx.InteractiveTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Tests CalendarUtils.
 * 
 * @author Jeanette Winzenburg
 * @author Eugen Hanussek https://github.com/homebeaver (use Locale.GERMANY for Calendar used in Germany)
 */
@RunWith(JUnit4.class)
public class CalendarUtilsTest extends InteractiveTestCase {
	
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(CalendarUtilsTest.class.getName());
    
    /**
     * default calendar instance
     */
    private Calendar todayGermany;
    private Calendar todayUS;
    private Calendar midJune;

    @Override
    protected void setUp() throws Exception {
        todayGermany = Calendar.getInstance(Locale.GERMANY);
        todayUS = Calendar.getInstance(Locale.US);
        midJune = Calendar.getInstance(Locale.GERMANY);
        midJune.set(Calendar.DAY_OF_MONTH, 14);
        midJune.set(Calendar.MONTH, Calendar.JUNE);
        midJune.getTimeInMillis();
    }
 
    @Before
    public void setUpJ4() throws Exception {
        setUp();
    }
    
    @After
    public void tearDownJ4() throws Exception {
        tearDown();
    }

    @Test
    public void testGetYearInDecade() {
        midJune.set(Calendar.YEAR, 2015);
        assertEquals(5, CalendarUtils.get(midJune, CalendarUtils.YEAR_IN_DECADE));
    }
    
    @Test
    public void testSetYearInDecade() {
        midJune.set(Calendar.YEAR, 2015);
        CalendarUtils.set(midJune, CalendarUtils.YEAR_IN_DECADE, 7);
        assertEquals(7, CalendarUtils.get(midJune, CalendarUtils.YEAR_IN_DECADE));
    }
    
    @Test
    public void testAddDecadeField() {
        todayGermany.set(Calendar.YEAR, 2015);
        CalendarUtils.add(todayGermany, CalendarUtils.DECADE, 1);
        assertEquals(2025, todayGermany.get(Calendar.YEAR));
    }
    
    @Test
    public void testAddDecadeFieldNegative() {
        todayGermany.set(Calendar.YEAR, 2015);
        CalendarUtils.add(todayGermany, CalendarUtils.DECADE, -1);
        assertEquals(2005, todayGermany.get(Calendar.YEAR));
    }
    
    @Test
    public void testAddNativeField() {
        todayGermany.set(Calendar.YEAR, 2015);
        CalendarUtils.add(todayGermany, Calendar.YEAR, 10);
        assertEquals(2025, todayGermany.get(Calendar.YEAR));
    }
    
    @Test
    public void testSetDecadeField() {
        todayGermany.set(Calendar.YEAR, 2015);
        CalendarUtils.set(todayGermany, CalendarUtils.DECADE, 2020);
        assertEquals(2025, todayGermany.get(Calendar.YEAR));
    }
    
    @Test
    public void testSetNativeField() {
        todayGermany.set(Calendar.YEAR, 2015);
        CalendarUtils.set(todayGermany, Calendar.YEAR, 2025);
        assertEquals(2025, todayGermany.get(Calendar.YEAR));
    }
    
    @Test
    public void testGetDecadeField() {
        todayGermany.set(Calendar.YEAR, 2025);
        assertEquals(2020, CalendarUtils.get(todayGermany, CalendarUtils.DECADE));
    }
    
    @Test
    public void testGetNativeField() {
        todayGermany.set(Calendar.YEAR, 2015);
        assertEquals(2015, CalendarUtils.get(todayGermany, Calendar.YEAR));
    }
//    
//    @Test
//    public void testSetNativeFieldNegative() {
//        Calendar clone = (Calendar) todayGerman.clone();
//        clone.add(Calendar.MONTH, -13);
//        todayGerman.set(Calendar.MONTH, -13);
//        assertEquals(clone.getTime(), todayGerman.getTime());
//        assertEquals("adjusted date" + todayGerman.getTime(),-1, todayGerman.get(Calendar.MONTH));
//    }
//    
    
    @Test
    public void testStartOfDecade() {
        todayGermany.set(Calendar.YEAR, 2005);
        CalendarUtils.startOfDecade(todayGermany);
        assertTrue(CalendarUtils.isStartOfYear(todayGermany));
        assertEquals(2000, todayGermany.get(Calendar.YEAR));
    }

    @Test
    public void testStartOfDecadeWithReturn() {
        midJune.add(Calendar.YEAR, 15);
        Date startOf10YearsFuture = CalendarUtils.startOfDecade(todayGermany, midJune.getTime());
        CalendarUtils.startOfDecade(midJune);
        assertTrue(CalendarUtils.isStartOfDecade(todayGermany));
        assertEquals(midJune.getTime(), startOf10YearsFuture);
    }
    
    @Test
    public void testIsStartOfDecade() {
        todayGermany.set(Calendar.YEAR, 2000);
        CalendarUtils.startOfYear(todayGermany);
        assertTrue(CalendarUtils.isStartOfDecade(todayGermany));
        todayGermany.add(Calendar.YEAR, 1);
        assertFalse(CalendarUtils.isStartOfDecade(todayGermany));
    }
    
    @Test
    public void testStartOfDecadeByField() {
        todayGermany.set(Calendar.YEAR, 2005);
        CalendarUtils.startOf(todayGermany, CalendarUtils.DECADE);
        assertTrue(CalendarUtils.isStartOfYear(todayGermany));
        assertEquals(2000, todayGermany.get(Calendar.YEAR));
    }
    
    @Test
    public void testIsStartOfDecadeByField() {
        todayGermany.set(Calendar.YEAR, 2000);
        CalendarUtils.startOfYear(todayGermany);
        assertTrue(CalendarUtils.isStartOf(todayGermany, CalendarUtils.DECADE));
        todayGermany.add(Calendar.YEAR, 1);
        assertFalse(CalendarUtils.isStartOf(todayGermany, CalendarUtils.DECADE));
    }
    
    @Test
    public void testSameByDecadeField() {
        Date now = todayGermany.getTime();
        CalendarUtils.startOfDecade(todayGermany);
        Date start = todayGermany.getTime();
        assertTrue(CalendarUtils.isSame(todayGermany, now, CalendarUtils.DECADE));
        assertEquals("Calendar unchanged by same decade query", start, todayGermany.getTime());
        todayGermany.add(Calendar.YEAR, -1);
        assertFalse(CalendarUtils.isSame(todayGermany, now, CalendarUtils.DECADE));
    }
    
    @Test
    public void testSameByDayField() {
        Date now = todayGermany.getTime();
        CalendarUtils.endOfDay(todayGermany);
        Date end = todayGermany.getTime();
        assertTrue(CalendarUtils.isSame(todayGermany, now, Calendar.DAY_OF_MONTH));
        assertEquals(end, todayGermany.getTime());
        todayGermany.add(Calendar.DAY_OF_MONTH, 1);
        assertFalse(CalendarUtils.isSame(todayGermany, now, Calendar.DAY_OF_MONTH));
    }
    
    @Test
    public void testStartOfYearField() {
        CalendarUtils.startOf(midJune, Calendar.YEAR);
        assertTrue(CalendarUtils.isStartOfYear(midJune));
        assertTrue(CalendarUtils.isStartOf(midJune, Calendar.YEAR));
    }
    
    @Test
    public void testStartOfDayField() {
        CalendarUtils.startOf(midJune, Calendar.DAY_OF_MONTH);
        assertTrue(CalendarUtils.isStartOfDay(midJune));
        assertTrue(CalendarUtils.isStartOf(midJune, Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testStartOfMonthField() {
        CalendarUtils.startOf(midJune, Calendar.MONTH);
        assertTrue(CalendarUtils.isStartOfMonth(midJune));
        assertTrue(CalendarUtils.isStartOf(midJune, Calendar.MONTH));
    }
    
    @Test
    public void testStartOfYear() {
        int year = midJune.get(Calendar.YEAR);
        CalendarUtils.startOfYear(midJune);
        assertEquals(Calendar.JANUARY, midJune.get(Calendar.MONTH));
        assertTrue(CalendarUtils.isStartOfMonth(midJune));
        assertEquals(year, midJune.get(Calendar.YEAR));
    }
    
    @Test
    public void testStartOfYearWithReturn() {
        midJune.add(Calendar.YEAR, 10);
        Date startOf10YearsFuture = CalendarUtils.startOfYear(todayGermany, midJune.getTime());
        CalendarUtils.startOfYear(midJune);
        assertTrue(CalendarUtils.isStartOfMonth(todayGermany));
        assertEquals("start of year with return must be same as changing start-of-year", 
                startOf10YearsFuture, midJune.getTime());
    }
    
    @Test
    public void testIsStartOfYear() {
        CalendarUtils.startOfYear(midJune);
        assertTrue(CalendarUtils.isStartOfYear(midJune));
        midJune.add(Calendar.MILLISECOND, -1);
        Date changed = midJune.getTime();
        assertFalse(CalendarUtils.isStartOfYear(midJune));
        assertEquals("calendar must be unchanged", changed, midJune.getTime());
    }
    
    @Test
    public void testWeekOfYearInFeb() {
        todayGermany.set(2008, Calendar.FEBRUARY, 1);
        Date firstOfFeb = todayGermany.getTime();
        CalendarUtils.startOfDay(todayGermany);
        assertTrue(CalendarUtils.isSameDay(todayGermany, firstOfFeb));
        assertTrue(CalendarUtils.isStartOfMonth(todayGermany));
        Date startOfFirstOfFeb = todayGermany.getTime();
        CalendarUtils.startOfWeek(todayGermany);
        assertTrue("expected calendar before firstOfFeb " 
                + todayGermany.getTime() + " / " + startOfFirstOfFeb , 
                todayGermany.getTime().before(startOfFirstOfFeb));
    }
    
    /**
     * test to characterize startofweek behaviour is we are in a 
     * calendar with minimalDays > 1.
     */
    @Test
    public void testWeekOfYearInDecember() {
        // a date before the first week of the month
        todayGermany.set(2007, Calendar.DECEMBER, 1);
        Date firstOfDecember = todayGermany.getTime();
        CalendarUtils.startOfWeek(todayGermany);
//        int weekOfYear = todayGerman.get(Calendar.WEEK_OF_YEAR);
        todayGermany.setTime(firstOfDecember);
        CalendarUtils.endOfMonth(todayGermany);
        // we crossed the year boundary
        assertEquals(1, todayGermany.get(Calendar.WEEK_OF_YEAR));
    }

    /**
     * test to characterize startofweek behaviour is we are in a 
     * calendar with minimalDays > 1.
     */
    @Test
    public void testStartOfWeekBeforeFirstWeekOfMonth() {
        // a date before the first week of the month
        todayGermany.set(2008, Calendar.FEBRUARY, 1);
        LOG.config("2008-02-01 WEEK_OF_MONTH expected:<0> is: "+todayGermany.get(Calendar.WEEK_OF_MONTH));
        assertEquals(0, todayGermany.get(Calendar.WEEK_OF_MONTH));
        CalendarUtils.startOfWeek(todayGermany);
        assertEquals(Calendar.JANUARY, todayGermany.get(Calendar.MONTH));
    }
    
    /**
     * test to characterize startofweek behaviour is we are in a 
     * calendar with minimalDays > 1
     * @see https://github.com/homebeaver/SwingSet/issues/4
     */
    @Test
    public void testStartOfWeekBeforeFirstWeekOfYear() {
        LOG.config("Locale.GERMANY:"+Locale.GERMANY + " Calendar in GERMANY:"+todayGermany);
        LOG.config("FirstDayOfWeek in "+Locale.GERMANY + "="+todayGermany.getFirstDayOfWeek());
        LOG.config("MinimalDaysInFirstWeek in "+Locale.GERMANY + "="+todayGermany.getMinimalDaysInFirstWeek());
    	assertEquals(2, todayGermany.getFirstDayOfWeek());   // 2 = MONDAY
    	assertEquals(4, todayGermany.getMinimalDaysInFirstWeek());
        
        // a date before the first week of the year
        todayGermany.set(2010, Calendar.JANUARY, 1);
        LOG.config("2010-01-01 WEEK_OF_MONTH expected:<0> is: "+todayGermany.get(Calendar.WEEK_OF_MONTH));
        assertEquals(0, todayGermany.get(Calendar.WEEK_OF_MONTH));
        LOG.config("2010-01-01 WEEK_OF_YEAR expected:<53> is: "+todayGermany.get(Calendar.WEEK_OF_YEAR));
        assertEquals(53, todayGermany.get(Calendar.WEEK_OF_YEAR));
        CalendarUtils.startOfWeek(todayGermany);
        assertEquals(Calendar.DECEMBER, todayGermany.get(Calendar.MONTH));
    }
    
    @Test
    public void testSameDay() {
        Date now = todayGermany.getTime();
        CalendarUtils.endOfDay(todayGermany);
        Date end = todayGermany.getTime();
        assertTrue(CalendarUtils.isSameDay(todayGermany, now));
        assertEquals(end, todayGermany.getTime());
        todayGermany.add(Calendar.DAY_OF_MONTH, 1);
        assertFalse(CalendarUtils.isSameDay(todayGermany, now));
    }
    
    @Test
    public void testAreEqual() {
        assertTrue(CalendarUtils.areEqual(null, null));
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        assertFalse(CalendarUtils.areEqual(now, null));
        assertFalse(CalendarUtils.areEqual(null, now));
        assertTrue(CalendarUtils.areEqual(now, now));
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        assertFalse(CalendarUtils.areEqual(now, calendar.getTime()));
    }
    @Test
    public void testIsStartOfWeek() {
        CalendarUtils.startOfWeek(midJune);
        assertTrue(CalendarUtils.isStartOfWeek(midJune));
        midJune.add(Calendar.MILLISECOND, -1);
        Date date = midJune.getTime();
        assertFalse(CalendarUtils.isStartOfWeek(midJune));
        assertEquals("calendar must be unchanged", date, midJune.getTime());
    }
 
    /**
     */
    @Test
    public void testIsEndOfWeek() {
        CalendarUtils.endOfWeek(midJune);
        assertTrue(CalendarUtils.isEndOfWeek(midJune));
        midJune.add(Calendar.MILLISECOND, 1);
        Date date = midJune.getTime();
        assertFalse(CalendarUtils.isEndOfWeek(midJune));
        assertEquals("calendar must be unchanged", date, midJune.getTime());
    }

    /**
     */
    @Test
    public void testEndOfWeek() {
        int week = midJune.get(Calendar.WEEK_OF_YEAR);
        CalendarUtils.endOfWeek(midJune);
        assertEquals(week, midJune.get(Calendar.WEEK_OF_YEAR));
        midJune.add(Calendar.MILLISECOND, 1);
        assertEquals(week + 1 , midJune.get(Calendar.WEEK_OF_YEAR));
    }

    /**
     */
    @Test
    public void testEndOfWeekWithReturn() {
        Date date = midJune.getTime();
        Date start = CalendarUtils.endOfWeek(midJune, date);
        assertTrue(CalendarUtils.isEndOfWeek(midJune));
        assertEquals(start, midJune.getTime());
    }
    /**
     */
    @Test
    public void testStartOfWeekWithReturn() {
        Date date = midJune.getTime();
        Date start = CalendarUtils.startOfWeek(midJune, date);
        assertTrue(CalendarUtils.isStartOfWeek(midJune));
        assertEquals(start, midJune.getTime());
    }

    @Test
    public void testStartOfWeekFromMiddle() {
        int day = Calendar.WEDNESDAY;
        todayGermany.set(Calendar.DAY_OF_WEEK, day);
        int week = todayGermany.get(Calendar.WEEK_OF_YEAR);
        CalendarUtils.startOfWeek(todayGermany);
        assertEquals(week, todayGermany.get(Calendar.WEEK_OF_YEAR));
        assertEquals(todayGermany.getFirstDayOfWeek(), todayGermany.get(Calendar.DAY_OF_WEEK));
    }
    
    @Test
    public void testStartOfWeekFromFirst() {
        todayGermany.set(Calendar.DAY_OF_WEEK, todayGermany.getFirstDayOfWeek());
        int week = todayGermany.get(Calendar.WEEK_OF_YEAR);
        CalendarUtils.startOfWeek(todayGermany);
        assertEquals(week, todayGermany.get(Calendar.WEEK_OF_YEAR));
        assertEquals(todayGermany.getFirstDayOfWeek(), todayGermany.get(Calendar.DAY_OF_WEEK));
    }
    
    @Test
    public void testStartOfWeekFromLast() {
        todayGermany.set(Calendar.DAY_OF_WEEK, todayGermany.getFirstDayOfWeek());
        int week = todayGermany.get(Calendar.WEEK_OF_YEAR);
        todayGermany.add(Calendar.DATE, 6);
        // sanity
        assertEquals(week, todayGermany.get(Calendar.WEEK_OF_YEAR));
        CalendarUtils.startOfWeek(todayGermany);
        assertEquals(week, todayGermany.get(Calendar.WEEK_OF_YEAR));
        assertEquals(todayGermany.getFirstDayOfWeek(), todayGermany.get(Calendar.DAY_OF_WEEK));
    }
    
    @Test
    public void testStartOfWeekFromFirstJan() {
        todayGermany.set(Calendar.MONTH, Calendar.JANUARY);
        todayGermany.set(Calendar.DATE, 1);
        if (todayGermany.get(Calendar.DAY_OF_WEEK) == todayGermany.getFirstDayOfWeek()) {
            todayGermany.add(Calendar.YEAR, -1);
        }
        int week = todayGermany.get(Calendar.WEEK_OF_YEAR);
        CalendarUtils.startOfWeek(todayGermany);
        assertEquals(week, todayGermany.get(Calendar.WEEK_OF_YEAR));
        assertEquals(todayGermany.getFirstDayOfWeek(), todayGermany.get(Calendar.DAY_OF_WEEK));
    }
    
    @Test
    public void testStartOfWeekUS() {
        int day = Calendar.WEDNESDAY;
        assertFalse(day == todayUS.getFirstDayOfWeek());
        int week = todayUS.get(Calendar.WEEK_OF_YEAR);
        CalendarUtils.startOfWeek(todayUS);
        assertEquals(week, todayUS.get(Calendar.WEEK_OF_YEAR));
    }
    
    
    @Test
    public void testIsStartOfMonth() {
        // want to be in the middle of a year
        int month = 5;
        todayGermany.set(Calendar.MONTH, month);
        CalendarUtils.startOfMonth(todayGermany);
        Date start = todayGermany.getTime();
        assertTrue(CalendarUtils.isStartOfMonth(todayGermany));
        // sanity: calendar must not be changed
        assertEquals(start, todayGermany.getTime());
        todayGermany.add(Calendar.MILLISECOND, 1);
        assertFalse(CalendarUtils.isStartOfMonth(todayGermany));
    }
    
    @Test
    public void testIsEndOfMonth() {
        // want to be in the middle of a year
        int month = 5;
        todayGermany.set(Calendar.MONTH, month);
        CalendarUtils.endOfMonth(todayGermany);
        Date start = todayGermany.getTime();
        assertTrue(CalendarUtils.isEndOfMonth(todayGermany));
        assertEquals(start, todayGermany.getTime());
        todayGermany.add(Calendar.MILLISECOND, -1);
        assertFalse(CalendarUtils.isEndOfMonth(todayGermany));
        // sanity: calendar must not be changed
    }
    
    @Test
    public void testEndOfMonth() {
        // want to be in the middle of a year
        int month = midJune.get(Calendar.MONTH);
        CalendarUtils.endOfMonth(midJune);
        assertEquals(month, midJune.get(Calendar.MONTH));
        midJune.add(Calendar.MILLISECOND, 1);
        assertEquals(month + 1, midJune.get(Calendar.MONTH));
    }

    @Test
    public void testStartOfMonth() {
        // want to be in the middle of a year
        int month = midJune.get(Calendar.MONTH);
        CalendarUtils.startOfMonth(midJune);
        assertEquals(month, midJune.get(Calendar.MONTH));
        midJune.add(Calendar.MILLISECOND, -1);
        assertEquals(month - 1, midJune.get(Calendar.MONTH));
    }
    
    @Test
    public void testEndOfDay() {
        // want to be in the middle of a month
        int day = midJune.get(Calendar.DAY_OF_MONTH);
        CalendarUtils.endOfDay(midJune);
        assertEquals(day, midJune.get(Calendar.DATE));
        midJune.add(Calendar.MILLISECOND, 1);
        assertEquals(day + 1, midJune.get(Calendar.DATE));
    }

    @Test
    public void testEndOfDayWithReturn() {
        Date date = midJune.getTime();
        Date start = CalendarUtils.endOfDay(midJune, date);
        assertTrue(CalendarUtils.isEndOfDay(midJune));
        assertEquals(start, midJune.getTime());
    }
    
    @Test
    public void testStartOfDay() {
        // want to be in the middle of a month
        int day = midJune.get(Calendar.DAY_OF_MONTH);
        CalendarUtils.startOfDay(midJune);
        assertEquals(day, midJune.get(Calendar.DATE));
        midJune.add(Calendar.MILLISECOND, -1);
        assertEquals(day - 1, midJune.get(Calendar.DATE));
    }

    @Test
    public void testStartOfDayWithReturn() {
        Date date = midJune.getTime();
        Date start = CalendarUtils.startOfDay(midJune, date);
        assertTrue(CalendarUtils.isStartOfDay(midJune));
        assertEquals(start, midJune.getTime());
    }
    
    @Test
    public void testStartOfDayUnique() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        CalendarUtils.startOfMonth(calendar);
        // sanity
        assertTrue(CalendarUtils.isStartOfDay(calendar));
        assertNotStartOfDayInTimeZones(calendar, "GMT+");
        assertNotStartOfDayInTimeZones(calendar, "GMT-");
    }
    
    private void assertNotStartOfDayInTimeZones(Calendar calendar, String id) {
        for (int i = 1; i < 13; i++) {
            calendar.setTimeZone(TimeZone.getTimeZone(id + i));
            assertFalse(CalendarUtils.isStartOfDay(calendar));
        }
    }

    @Test
    public void testStartOfMonthUnique() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        CalendarUtils.startOfMonth(calendar);
        // sanity
        assertTrue(CalendarUtils.isStartOfMonth(calendar));
        assertNotStartOfMonthInTimeZones(calendar, "GMT+");
        assertNotStartOfMonthInTimeZones(calendar, "GMT-");
    }

    private void assertNotStartOfMonthInTimeZones(Calendar calendar, String id) {
        for (int i = 1; i < 13; i++) {
            calendar.setTimeZone(TimeZone.getTimeZone(id + i));
            assertFalse(CalendarUtils.isStartOfMonth(calendar));
        }
    }
    /**
     * sanity ...
     */
    @Test
    public void testNextMonthCal() {
        todayGermany.set(Calendar.MONTH, Calendar.JANUARY);
        Date date = todayGermany.getTime();
        for (int i = Calendar.JANUARY; i <= Calendar.DECEMBER; i++) {
            int month = todayGermany.get(Calendar.MONTH);
            CalendarUtils.startOfMonth(todayGermany);
            assertEquals(month, todayGermany.get(Calendar.MONTH));
            CalendarUtils.endOfMonth(todayGermany);
            assertEquals(month, todayGermany.get(Calendar.MONTH));
            // restore original and add
            todayGermany.setTime(date);
            todayGermany.add(Calendar.MONTH, 1);
            date = todayGermany.getTime();
            if (i < Calendar.DECEMBER) {
                assertEquals(month + 1, todayGermany.get(Calendar.MONTH));
            } else {
                assertEquals(Calendar.JANUARY, todayGermany.get(Calendar.MONTH));
            }
        }
    }
    
    @Test
    public void testNextMonth() {
        todayGermany.set(Calendar.MONTH, Calendar.JANUARY);
        todayGermany.set(Calendar.DATE, 31);
        for (int i = Calendar.JANUARY; i <= Calendar.DECEMBER; i++) {
            int month = todayGermany.get(Calendar.MONTH);
            long nextMonth = DateUtils.getNextMonth(todayGermany.getTimeInMillis());
            todayGermany.setTimeInMillis(nextMonth);
            if (i < Calendar.DECEMBER) {
                assertEquals(month + 1, todayGermany.get(Calendar.MONTH));
            } else {
                assertEquals(Calendar.JANUARY, todayGermany.get(Calendar.MONTH));
            }
        }
    }

    //----------------- semantic startOf/endOf must flush the calendar
    @Test
    public void testFlushedStartOfWeek() {
        CalendarUtils.startOfWeek(midJune);
        assertFlushed(midJune);
    }
    
    @Test
    public void testFlushedStartOfDay() {
        CalendarUtils.startOfDay(midJune);
        assertFlushed(midJune);
    }
    
    @Test
    public void testFlushedStartOfMonth() {
        CalendarUtils.startOfMonth(midJune);
        assertFlushed(midJune);
    }
    
    @Test
    public void testFlushedStartOfYear() {
        CalendarUtils.startOfYear(midJune);
        assertFlushed(midJune);
    }
    
    @Test
    public void testFlushedStartOfDecade() {
        CalendarUtils.startOfDecade(midJune);
        assertFlushed(midJune);
    }
    
    @Test
    public void testFlushedEndOfWeek() {
        CalendarUtils.endOfWeek(midJune);
        assertFlushed(midJune);
    }
    
    @Test
    public void testFlushedEndOfDay() {
       CalendarUtils.endOfDay(midJune); 
       assertFlushed(midJune);
    }
    
    @Test
    public void testFlushedEndOfMonth() {
        CalendarUtils.endOfMonth(midJune);
        assertFlushed(midJune);
    }
    
    @Test
    public void testFlushedInitially() {
        assertFlushed(todayGermany);
        assertFlushed(todayUS);
        assertFlushed(midJune);
    }
    /**
     * @param todayGerman2
     */
    private void assertFlushed(Calendar calendar) {
        assertTrue("must be flushed but was: " + calendar, CalendarUtils.isFlushed(calendar));
        
    }

}
