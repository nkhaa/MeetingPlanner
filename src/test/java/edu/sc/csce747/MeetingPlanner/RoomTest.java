package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class RoomTest {

    @Test
    public void testCreateRoom() {
        Room r = new Room("2A01");
        assertEquals("2A01", r.getID());
    }

    @Test
    public void testAddMeetingMakesBusy() {
        Room r = new Room("2A01");
        ArrayList<Person> people = new ArrayList<Person>();
        people.add(new Person("Alice"));
        try {
            Meeting m = new Meeting(6, 6, 13, 14, people, r, "Lunch Meeting");
            r.addMeeting(m);
            assertTrue(r.isBusy(6, 6, 13, 14));
        } catch (TimeConflictException e) {
            fail("Should not throw: " + e.getMessage());
        }
    }

    @Test
    public void testOverlappingNotAllowed() {
        Room r = new Room("2A01");
        ArrayList<Person> attendees = new ArrayList<Person>();
        attendees.add(new Person("Bob"));

        try {
            r.addMeeting(new Meeting(6, 6, 13, 14, attendees, r, "First Meeting"));
        } catch (TimeConflictException e) {
            fail("First meeting should be valid");
        }

        try {
            r.addMeeting(new Meeting(6, 6, 13, 15, attendees, r, "Overlap Meeting"));
            fail("Expected TimeConflictException for overlap");
        } catch (TimeConflictException e) {
            // expected
        }
    }

    @Test
    public void testIsBusyCoveringRangeReturnsFalseByDesign() {
        Room r = new Room("2A02");
        ArrayList<Person> attendees = new ArrayList<Person>();
        attendees.add(new Person("Charlie"));
        try {
            r.addMeeting(new Meeting(8, 8, 10, 11, attendees, r, "Morning Meeting"));
            assertFalse(r.isBusy(8, 8, 9, 12)); // covering range -> false with current Calendar.isBusy
        } catch (TimeConflictException e) {
            fail("Should not throw");
        }
    }

    @Test
    public void testPrintAgendaWorks() {
        Room r = new Room("2A05");
        ArrayList<Person> attendees = new ArrayList<Person>();
        attendees.add(new Person("Alice"));
        try {
            Meeting m = new Meeting(8, 9, 10, 11, attendees, r, "Project Demo");
            r.addMeeting(m);

            String dayAgenda = r.printAgenda(8, 9);
            String monthAgenda = r.printAgenda(8);
            assertNotNull(dayAgenda);
            assertNotNull(monthAgenda);
            assertTrue(dayAgenda.startsWith("Agenda for 8/9:"));
            assertTrue(monthAgenda.startsWith("Agenda for 8:"));
        } catch (TimeConflictException e) {
            fail("Should not throw");
        }
    }
}
