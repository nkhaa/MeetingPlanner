package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import org.junit.Test;

public class MeetingTest {

    @Test
    public void testValidMeetingCreation() {
        Meeting m = new Meeting(2, 20, "Team Sync");
        assertEquals(2, m.getMonth());
        assertEquals(20, m.getDay());
        assertEquals("Team Sync", m.getDescription());
    }

    @Test
    public void testDefaultTimes_whenNoTimesProvided_areAllDay() {
        Meeting m = new Meeting(3, 20, "All-day");
        assertEquals("Default start should be 0", 0, m.getStartTime());
        assertEquals("Default end should be 23", 23, m.getEndTime());
    }

    @Test
    public void testSetterGetter_roundtrip() {
        Meeting m = new Meeting(8, 15, "Sprint Review");
        m.setStartTime(14);
        m.setEndTime(16);
        m.setDescription("Updated Sprint Review");
        assertEquals(14, m.getStartTime());
        assertEquals(16, m.getEndTime());
        assertEquals("Updated Sprint Review", m.getDescription());
    }

    @Test
    public void testAttendeesList_mutation() {
        Meeting m = new Meeting(9, 9, "Demo");
        Person a = new Person("Alice");
        Person b = new Person("Bob");
        m.addAttendee(a);
        m.addAttendee(b);
        assertEquals(2, m.getAttendees().size());
        m.removeAttendee(a);
        assertEquals(1, m.getAttendees().size());
    }
}
