package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

public class MeetingTest {

    @Test
    public void testCtor_withTimes_setsAllFields() {
        Meeting m = new Meeting(5, 10, 9, 11);
        assertEquals(5, m.getMonth());
        assertEquals(10, m.getDay());
        assertEquals(9, m.getStartTime());
        assertEquals(11, m.getEndTime());
        // description may be null or default — we won’t assert it here
    }

    @Test
    public void testCtor_allDayByDescription_sets0to23() {
        Meeting m = new Meeting(6, 26, "Midsommar");
        // Calendar constructor itself uses this pattern for “Day does not exist”
        // so we assume description-based ctor is all-day 0..23
        assertEquals(6, m.getMonth());
        assertEquals(26, m.getDay());
        assertEquals(0, m.getStartTime());
        assertEquals(23, m.getEndTime());
        assertEquals("Midsommar", m.getDescription());
    }


    @Test
    public void testAddToCalendar_withInvalidTimes_failsAtCalendarLevel() {
        // Meeting itself may not validate; Calendar is responsible for validation.
        Calendar cal = new Calendar();
        Meeting invalid = new Meeting(5, 15, -1, 10); // invalid start

        try {
            cal.addMeeting(invalid);
            fail("Expected TimeConflictException when adding meeting with invalid hours");
        } catch (TimeConflictException e) {
            // expected — Calendar.checkTimes(...) should trigger
            assertTrue(e.getMessage().toLowerCase().contains("illegal hour"));
        }
    }

    // ===== Structural White-Box Tests (Lab 08) =====
    @Test
    public void testToStringIncludesDetails() {
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(new Person("Alice"));
        Room r = new Room("2A01");
        Meeting m = new Meeting(1, 5, 9, 10, attendees, r, "Weekly Review");
        String info = m.toString();
        assertTrue(info.contains("Weekly Review"));
        assertTrue(info.contains("Alice"));
        assertTrue(info.contains("2A01"));
    }

    @Test
    public void testSettersAndGetters() {
        Meeting m = new Meeting();
        m.setMonth(6);
        m.setDay(20);
        m.setStartTime(9);
        m.setEndTime(11);
        m.setDescription("Demo");
        Room r = new Room("R");
        m.setRoom(r);
        assertEquals(6, m.getMonth());
        assertEquals("Demo", m.getDescription());
        assertEquals("R", m.getRoom().getID());
    }

    // ===== Structural White-Box Tests (Refinement) =====
    @Test
    public void testMeetingConstructorsAndCompare() {
        Meeting m1 = new Meeting(5, 5, 10, 11);
        Meeting m2 = new Meeting(5, 5, 10, 11);
        assertEquals(m1.getStartTime(), m2.getStartTime());
        assertEquals(m1.getEndTime(), m2.getEndTime());
    }

    @Test
    public void testAddAndRemoveAttendees() {
        Room r = new Room("M1");
        Meeting m = new Meeting(6, 10, 9, 10, new ArrayList<>(), r, "Sync");
        Person p = new Person("Ann");
        m.addAttendee(p);
        assertTrue(m.getAttendees().contains(p));
        m.removeAttendee(p);
        assertFalse(m.getAttendees().contains(p));
    }



}
