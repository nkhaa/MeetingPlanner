package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import org.junit.Test;

public class RoomTest {

    @Test
    public void testInitiallyNotBusy() {
        Room r = new Room("2A01");
        try {
            assertFalse(r.isBusy(6, 6, 13, 14));
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }

    @Test
    public void testAddMeeting_makesBusy() {
        Room r = new Room("2A01");
        Meeting m = new Meeting(6, 6, 13, 14);
        try {
            r.addMeeting(m);
            assertTrue(r.isBusy(6, 6, 13, 14));
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }

    @Test
    public void testAddOverlappingMeeting_throws() {
        Room r = new Room("2A01");
        Meeting m1 = new Meeting(6, 6, 13, 14);
        Meeting m2 = new Meeting(6, 6, 13, 15);
        try {
            r.addMeeting(m1);
        } catch (TimeConflictException e) {
            fail("First add should not throw");
        }
        try {
            r.addMeeting(m2);
            fail("Expected TimeConflictException for overlapping meetings");
        } catch (TimeConflictException e) {
            // expected
        }
    }
}
