package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import org.junit.Test;

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
}
