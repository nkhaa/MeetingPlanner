package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PersonTest {
    private static Meeting createMeeting(Person attendee,
                                         int month, int day,
                                         int start, int end,
                                         String roomId, String desc) {
        List<Person> attendees = new ArrayList<>();
        if (attendee != null) attendees.add(attendee);
        Room room = new Room(roomId);
        return new Meeting(month, day, start, end, new ArrayList<>(attendees), room, desc);
    }

    @Test
    public void testCreatePerson() {
        Person p = new Person("Alice");
        assertEquals("Alice", p.getName());
    }

    @Test
    public void testAddMeetingMakesBusy() {
        Person p = new Person("John");
        try {
            Meeting m = createMeeting(p, 4, 10, 9, 10, "2A01", "Morning Sync");
            p.addMeeting(m);
            assertTrue(p.isBusy(4, 10, 9, 10));
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }

    @Test
    public void testDoubleBookingNotAllowed() {
        Person p = new Person("John");
        try {
            p.addMeeting(createMeeting(p, 4, 10, 9, 10, "2A01", "First Meeting"));
        } catch (TimeConflictException e) {
            fail("First meeting should be valid");
        }
        try {
            p.addMeeting(createMeeting(p, 4, 10, 9, 11, "2A01", "Overlap Meeting"));
            fail("Expected TimeConflictException for overlapping meetings");
        } catch (TimeConflictException e) {
            // expected
        }
    }

    @Test
    public void testBackToBackNotAllowed() {
        Person p = new Person("Jane");
        try {
            p.addMeeting(createMeeting(p, 6, 6, 10, 11, "2A02", "First Meeting"));
        } catch (TimeConflictException e) {
            fail("First meeting should be valid");
        }
        try {
            p.addMeeting(createMeeting(p, 6, 6, 11, 12, "2A02", "Second Meeting"));
            fail("Expected TimeConflictException for back-to-back meetings");
        } catch (TimeConflictException e) {
            // expected
        }
    }

    @Test
    public void testIsBusyCoveringRangeFalseByDesign() {
        Person p = new Person("Eve");
        try {
            p.addMeeting(createMeeting(p, 7, 2, 9, 10, "2A03", "Stand-up"));
            assertFalse(p.isBusy(7, 2, 8, 11)); // current Calendar logic
        } catch (TimeConflictException e) {
            fail("Should not throw");
        }
    }
    @Test
    public void testAddMeeting_conflictPropagation() throws Exception {
        Person p = new Person("Bob");
        Room room = new Room("R1");
        Meeting m1 = new Meeting(4, 5, 10, 11, new ArrayList<>(), room, "Conflict1");
        Meeting m2 = new Meeting(4, 5, 10, 12, new ArrayList<>(), room, "Conflict2");
        p.addMeeting(m1);
        assertThrows(TimeConflictException.class, () -> p.addMeeting(m2));
    }



    @Test
    public void testInvalidTimeThrows() {
        Person p = new Person("Gina");
        try {
            p.addMeeting(createMeeting(p, 5, 15, -1, 10, "2A05", "Invalid Hour"));
            fail("Negative start should throw");
        } catch (TimeConflictException e) {
            assertTrue(e.getMessage().toLowerCase().contains("illegal hour"));
        }
    }



    @Test
    public void testIsBusy_exactEndBoundary() throws Exception {
        Person p = new Person("Liam");
        Meeting m = new Meeting(3, 3, 9, 10);
        p.addMeeting(m);
        assertTrue(p.isBusy(3, 3, 9, 10));  // on boundary
    }


}
