package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import org.junit.Test;

public class CalendarTest {

    @Test
    public void testAddMeeting_holiday() {
        Calendar calendar = new Calendar();
        try {
            Meeting midsommar = new Meeting(6, 26, "Midsommar");
            calendar.addMeeting(midsommar);
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
        Meeting m1 = new Meeting(5, 12, 10, 12);
        m1.setDescription("Morning");
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
    public void testIsBusy_rangeCoversMeeting_currentBehavior_isFalse() {
        Calendar cal = new Calendar();
        Meeting m = new Meeting(7, 1, 9, 10);
        m.setDescription("Daily standup");
        try {
            cal.addMeeting(m);
            boolean busy = cal.isBusy(7, 1, 8, 11);
            assertFalse("Current isBusy checks only boundary inclusion; covering range returns false", busy);
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }

    @Test
    public void testBackToBackMeetings_disallowed() {
        Calendar cal = new Calendar();
        try {
            cal.addMeeting(new Meeting(4, 12, 10, 11));
        } catch (TimeConflictException e) {
            fail("First meeting should be valid: " + e.getMessage());
        }
        try {
            cal.addMeeting(new Meeting(4, 12, 11, 12)); // end == nextStart → ЗӨРЧИЛ
            fail("Expected TimeConflictException for back-to-back meetings (end == nextStart)");
        } catch (TimeConflictException e) {
            // OK
        }
    }

    @Test
    public void testStartAfterEnd_throws() {
        Calendar cal = new Calendar();
        try {
            cal.addMeeting(new Meeting(5, 25, 16, 15));
            fail("Expected TimeConflictException when start >= end");
        } catch (TimeConflictException e) {
            assertTrue(e.getMessage().toLowerCase().contains("starts before it ends"));
        }
    }

    @Test
    public void testInvalidHourValues_throws() {
        Calendar cal = new Calendar();
        try {
            cal.addMeeting(new Meeting(5, 15, -1, 10));
            fail("Negative start should throw");
        } catch (TimeConflictException e) {
            assertTrue(e.getMessage().toLowerCase().contains("illegal hour"));
        }

        try {
            cal.addMeeting(new Meeting(5, 15, 10, 25));
            fail("End > 23 should throw");
        } catch (TimeConflictException e) {
            assertTrue(e.getMessage().toLowerCase().contains("illegal hour"));
        }
    }

    @Test
    public void testRemoveMeeting_freesSlot() {
        Calendar cal = new Calendar();
        try {
            cal.addMeeting(new Meeting(4, 15, 14, 15));
            assertTrue(cal.isBusy(4, 15, 14, 15));
            cal.removeMeeting(4, 15, 0);
            assertFalse(cal.isBusy(4, 15, 14, 15));
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }

    @Test
    public void testPrintAgenda_month_notEmptyAndHasHeader() {
        Calendar cal = new Calendar();
        try {
            Meeting m = new Meeting(8, 3, 9, 10);
            m.setDescription("Daily standup");
            cal.addMeeting(m);
            String agenda = cal.printAgenda(8);
            assertNotNull(agenda);
            assertTrue("Agenda header should be present", agenda.startsWith("Agenda for 8:"));
            assertTrue("Agenda should not be empty", agenda.trim().length() > "Agenda for 8:".length());
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }

    @Test
    public void testPrintAgenda_day_notEmptyAndHasHeader() {
        Calendar cal = new Calendar();
        try {
            Meeting m = new Meeting(9, 9, 13, 14);
            m.setDescription("Design Review");
            cal.addMeeting(m);
            String agenda = cal.printAgenda(9, 9);
            assertNotNull(agenda);
            assertTrue("Agenda header should be present", agenda.startsWith("Agenda for 9/9:"));
            assertTrue("Agenda should not be empty", agenda.trim().length() > "Agenda for 9/9:".length());
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }
}
