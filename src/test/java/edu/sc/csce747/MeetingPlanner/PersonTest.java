package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import org.junit.Test;

public class PersonTest {

    @Test
    public void testInitiallyNotBusy() {
        Person p = new Person("John");
        try {
            assertFalse(p.isBusy(4, 10, 9, 10));
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }

    @Test
    public void testAddMeeting_makesBusy() {
        Person p = new Person("John");
        Meeting m = new Meeting(4, 10, 9, 10);
        try {
            p.addMeeting(m);
            assertTrue(p.isBusy(4, 10, 9, 10));
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }

    @Test
    public void testAddOverlappingMeeting_throws() {
        Person p = new Person("John");
        Meeting m1 = new Meeting(4, 10, 9, 10);
        Meeting m2 = new Meeting(4, 10, 9, 11);
        try {
            p.addMeeting(m1);
        } catch (TimeConflictException e) {
            fail("First add should not throw");
        }
        try {
            p.addMeeting(m2);
            fail("Expected TimeConflictException for overlapping meetings");
        } catch (TimeConflictException e) {

        }
    }
}
