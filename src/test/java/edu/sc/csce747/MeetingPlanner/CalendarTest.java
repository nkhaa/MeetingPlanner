package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class CalendarTest {
    @Test
    public void testAddMeeting_holiday() {
        // Create Midsommar holiday
        Calendar calendar = new Calendar();
        // Add to calendar object.
        try {
            Meeting midsommar = new Meeting(6, 26, "Midsommar");
            calendar.addMeeting(midsommar);
            // Verify that it was added.
            Boolean added = calendar.isBusy(6, 26, 0, 23);
            assertTrue("Midsommar should be marked as busy on the calendar", added);
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testAddMeeting_invalidDay_throws() {
        Calendar cal = new Calendar();
        Meeting bad = new Meeting(2, 35, "Impossible Feb 35");
        try {
            cal.addMeeting(bad);
            fail("Expected TimeConflictException for invalid day");
        } catch (TimeConflictException e) {
            assertTrue(e.getMessage().toLowerCase().contains("day"));
        }
    }

    @Test
    public void testAddMeeting_invalidMonth_throws() {
        Calendar cal = new Calendar();
        Meeting bad = new Meeting(13, 10, "Invalid month 13");
        try {
            cal.addMeeting(bad);
            fail("Expected TimeConflictException for invalid month");
        } catch (TimeConflictException e) {
            assertTrue(e.getMessage().toLowerCase().contains("month"));
        }
    }

    @Test
    public void testAddOverlappingMeetings_sameDay_overlapThrows() {
        Calendar cal = new Calendar();
        // First meeting 10:00-12:00
        Meeting m1 = new Meeting(5, 12, 10, 12);
        m1.setDescription("Morning");
        // Second overlaps 11:00-13:00
        Meeting m2 = new Meeting(5, 12, 11, 13);
        m2.setDescription("Overlap");
        try {
            cal.addMeeting(m1);
        } catch (TimeConflictException e) {
            fail("First meeting should be valid: " + e.getMessage());
        }
        try {
            cal.addMeeting(m2);
            fail("Expected TimeConflictException for overlapping meeting");
        } catch (TimeConflictException e) {
            // expected
        }
    }

    @Test
    public void testIsBusy_rangeCoversMeeting_returnsTrue() {
        Calendar cal = new Calendar();
        Meeting m = new Meeting(7, 1, 9, 10);
        m.setDescription("Daily standup");
        try {
            cal.addMeeting(m);
            boolean busy = cal.isBusy(7, 1, 8, 11);
            assertTrue("Calendar should be busy when queried range covers meeting", busy);
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }
}
