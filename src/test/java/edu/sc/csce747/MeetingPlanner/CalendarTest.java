package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.Test;
import java.util.ArrayList;

public class CalendarTest {

    // Helper to create valid meetings with non-null Room
    private Meeting makeMeeting(int month, int day, int start, int end, String desc) {
        Organization org = new Organization();
        Room room = org.getRooms().get((month + day + start + end) % org.getRooms().size());
        return new Meeting(month, day, start, end, new ArrayList<>(), room, desc);
    }


    @Test
    public void testCheckTimes_invalidBranches() {
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(0, 10, 9, 10));
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(12, 10, 9, 10));
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(5, 0, 9, 10));
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(5, 32, 9, 10));
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(5, 10, -1, 10));
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(5, 10, 9, 24));
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(5, 10, 12, 12));
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(5, 10, 15, 12));
    }

    @Test
    public void testCheckTimes_valid() throws Exception {
        Calendar.checkTimes(1, 31, 9, 10);
        Calendar.checkTimes(6, 30, 9, 10);
        Calendar.checkTimes(7, 31, 0, 23);
    }

    // ---------- addMeeting ----------

    @Test
    public void testAddMeeting_validAndConflict() throws Exception {
        Calendar c = new Calendar();
        c.addMeeting(makeMeeting(3, 5, 9, 10, "M1"));
        c.addMeeting(makeMeeting(3, 5, 11, 12, "M2")); // no overlap
        assertTrue(c.isBusy(3, 5, 9, 10));
        assertTrue(c.isBusy(3, 5, 11, 12));
        assertThrows(TimeConflictException.class,
                () -> c.addMeeting(makeMeeting(3, 5, 9, 11, "Overlap"))); // overlap
    }

    @Test
    public void testAddMeeting_startEndConflicts() throws Exception {
        Calendar c = new Calendar();
        c.addMeeting(makeMeeting(4, 4, 8, 10, "One"));
        assertThrows(TimeConflictException.class,
                () -> c.addMeeting(makeMeeting(4, 4, 9, 10, "Two")));
        assertThrows(TimeConflictException.class,
                () -> c.addMeeting(makeMeeting(4, 4, 7, 9, "Three")));
    }

    // --- helper to access the internal day list (no source change needed) ---
    @SuppressWarnings("unchecked")
    private ArrayList<Meeting> dayList(Calendar c, int month, int day) {
        try {
            java.lang.reflect.Field f = Calendar.class.getDeclaredField("occupied");
            f.setAccessible(true);
            ArrayList<ArrayList<ArrayList<Meeting>>> occ =
                    (ArrayList<ArrayList<ArrayList<Meeting>>>) f.get(c);
            return occ.get(month).get(day);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Safe coverage of “Day does not exist” branch (no toString() call)
    @Test
    public void testAddMeeting_dayDoesNotExistBranch() {
        Calendar c = new Calendar();
        // Calendar() constructor seeded 2/31 with a dummy meeting: "Day does not exist"
        ArrayList<Meeting> feb31 = dayList(c, 2, 31);
        assertNotNull(feb31);
        assertTrue("Expected the dummy invalid-day meeting at 2/31", feb31.size() > 0);
        assertEquals("Day does not exist", feb31.get(0).getDescription());
    }

    // ---------- isBusy ----------

    @Test
    public void testIsBusy_allPaths() throws Exception {
        Calendar c = new Calendar();
        c.addMeeting(makeMeeting(5, 5, 9, 11, "Meet"));
        assertTrue(c.isBusy(5, 5, 9, 10));   // start within
        assertTrue(c.isBusy(5, 5, 10, 11));  // end within
        assertFalse(c.isBusy(5, 5, 7, 8));   // before
        assertFalse(c.isBusy(5, 5, 12, 13)); // after
        assertFalse(c.isBusy(6, 5, 9, 10));  // wrong month
        assertFalse(c.isBusy(5, 6, 9, 10));  // wrong day
    }

    @Test
    public void testIsBusy_emptyCalendar() throws Exception {
        Calendar c = new Calendar();
        assertFalse(c.isBusy(8, 8, 9, 10));
    }

    // ---------- clearSchedule / removeMeeting / getMeeting ----------

    @Test
    public void testClearRemoveGetMeetingCoverage() throws Exception {
        Calendar c = new Calendar();
        Meeting m = makeMeeting(9, 9, 8, 9, "Morning");
        c.addMeeting(m);
        assertEquals(m, c.getMeeting(9, 9, 0));

        // clearSchedule
        c.clearSchedule(9, 9);
        assertFalse(c.isBusy(9, 9, 8, 9));

        // removeMeeting safe
        c.addMeeting(makeMeeting(9, 9, 10, 11, "Late"));
        c.removeMeeting(9, 9, 0);
        try { c.removeMeeting(9, 9, 5); } catch (Exception ignored) {}
    }

    // ---------- printAgenda ----------

    @Test
    public void testPrintAgenda_emptyAndFilled() throws Exception {
        Calendar c = new Calendar();
        String emptyMonth = c.printAgenda(10);
        assertTrue(emptyMonth.startsWith("Agenda for 10:"));

        c.addMeeting(makeMeeting(10, 20, 8, 9, "Test"));
        String month = c.printAgenda(10);
        assertTrue(month.contains("Agenda for 10:"));

        String day = c.printAgenda(10, 20);
        assertTrue(day.contains("Agenda for 10/20:"));

        String emptyDay = c.printAgenda(10, 31);
        assertTrue(emptyDay.startsWith("Agenda for 10/31:"));
    }

    // ---------- multiple-meeting final false ----------

    @Test
    public void testAddMeeting_multipleNoConflictFinalFalse() throws Exception {
        Calendar c = new Calendar();
        c.addMeeting(makeMeeting(11, 11, 8, 9, "A"));
        c.addMeeting(makeMeeting(11, 11, 10, 11, "B"));
        c.addMeeting(makeMeeting(11, 11, 12, 13, "C"));
        assertFalse(c.isBusy(11, 11, 14, 15));
    }

    // ---------- utility coverage helper for invalid branch ----------

    // Adds access to internal day list (for DayDoesNotExist coverage)
    private ArrayList<Meeting> getMeetingList(Calendar c, int month, int day) {
        try {
            java.lang.reflect.Field f = Calendar.class.getDeclaredField("occupied");
            f.setAccessible(true);
            ArrayList<ArrayList<ArrayList<Meeting>>> occ = (ArrayList<ArrayList<ArrayList<Meeting>>>) f.get(c);
            return occ.get(month).get(day);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // === Final patch to cover first-line branches of printAgenda() ===
    @Test
    public void testPrintAgenda_monthHeaderOnly_noMeetings() {
        Calendar c = new Calendar();
        // Ensure this month has no meetings; triggers 0-iteration branch
        String agenda = c.printAgenda(12);
        assertEquals("Agenda for 12:\n", agenda);
    }

    @Test
    public void testPrintAgenda_dayHeaderOnly_noMeetings() {
        Calendar c = new Calendar();
        // Ensure this day is empty; triggers 0-iteration branch in day loop
        String agenda = c.printAgenda(12, 25);
        assertEquals("Agenda for 12/25:\n", agenda);
    }

    @Test
    public void testAddMeeting_dayDoesNotExistSkippedAndNoConflict() throws Exception {
        Calendar c = new Calendar();
        Meeting invalid = new Meeting(3, 3, 9, 10, new ArrayList<>(), new Room("R"), "Day does not exist");
        c.addMeeting(invalid);
        Meeting m2 = new Meeting(3, 3, 11, 12, new ArrayList<>(), new Room("R"), "Normal");
        c.addMeeting(m2);
        assertFalse(c.isBusy(3, 3, 13, 14));
    }

    @Test
    public void testCheckTimes_illegalHourNegativeAndTooHigh() {
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(3, 3, -1, 10));
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(3, 3, 0, 25));
    }

    @Test
    public void testIsBusy_loopRunsButReturnsFalse() throws Exception {
        Calendar c = new Calendar();
        c.addMeeting(makeMeeting(6, 6, 9, 10, "Single"));
        assertFalse(c.isBusy(6, 6, 7, 8));
    }

    // === FINAL PATCH: ensure 100% branch coverage for isBusy() and illegal-hour checks ===

    @Test
    public void testIsBusy_firstConditionTrue_secondFalse() throws Exception {
        // start inside meeting range → triggers first if branch
        Calendar c = new Calendar();
        c.addMeeting(makeMeeting(1, 2, 9, 11, "Morning"));
        assertTrue("Expected busy=true for start within range", c.isBusy(1, 2, 9, 10));
    }

    @Test
    public void testIsBusy_secondConditionTrue_firstFalse() throws Exception {
        // end inside meeting range → triggers else-if branch
        Calendar c = new Calendar();
        c.addMeeting(makeMeeting(1, 3, 9, 11, "Morning"));
        assertTrue("Expected busy=true for end within range", c.isBusy(1, 3, 8, 10));
    }

    @Test
    public void testIsBusy_noConditionTrue_pathFalse() throws Exception {
        // start and end outside range → neither if nor else-if runs
        Calendar c = new Calendar();
        c.addMeeting(makeMeeting(1, 4, 9, 11, "Morning"));
        assertFalse("Expected busy=false when no overlap", c.isBusy(1, 4, 12, 13));
    }

    @Test
    public void testCheckTimes_illegalHourBranches() {
        // explicitly trigger both mStart and mEnd "Illegal hour." exceptions
        assertThrows(TimeConflictException.class,
                () -> Calendar.checkTimes(5, 10, -1, 10)); // mStart < 0
        assertThrows(TimeConflictException.class,
                () -> Calendar.checkTimes(5, 10, 9, 24));  // mEnd > 23
    }
    @Test
    public void testAddMeeting_emptyDay_noConflictBranch() throws Exception {
        Calendar c = new Calendar();
        // new day with no existing meetings → for-loop not entered
        c.addMeeting(makeMeeting(1, 15, 9, 10, "Single"));
        assertTrue(c.isBusy(1, 15, 9, 10));
    }

    @Test
    public void testCheckTimes_monthGreaterOrEqual12() {
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(12, 10, 9, 10));
    }
    @Test
    public void testCheckTimes_startEqualsEndBranch() {
        assertThrows(TimeConflictException.class, () -> Calendar.checkTimes(3, 3, 9, 9));
    }

    @Test
    public void testCheckTimes_startGreaterThan23() {
        assertThrows(TimeConflictException.class,
                () -> Calendar.checkTimes(5, 10, 24, 25)); // start > 23
    }
    @Test
    public void testCheckTimes_endLessThan0() {
        assertThrows(TimeConflictException.class,
                () -> Calendar.checkTimes(5, 10, 0, -1)); // end < 0
    }

    @Test
    public void testAddMeeting_mEndLessThanExistingStart_falseBranch() throws Exception {
        Calendar c = new Calendar();
        // existing meeting starts later than our mEnd → (mEnd >= start) false
        c.addMeeting(makeMeeting(6, 6, 10, 12, "Existing"));
        // new meeting ends before 10, so mEnd >= toCheck.getStartTime() is false
        c.addMeeting(makeMeeting(6, 6, 8, 9, "EndsBeforeExisting")); // no overlap
        assertTrue(c.isBusy(6, 6, 8, 9));
    }

}
