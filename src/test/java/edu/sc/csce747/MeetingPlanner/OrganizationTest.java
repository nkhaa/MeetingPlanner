package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import org.junit.Test;

public class OrganizationTest {

    @Test
    public void testDefaultEmployeesAndRoomsExist() throws Exception {
        Organization org = new Organization();
        // Based on Organization default constructor in your source:
        assertNotNull(org.getEmployee("Greg Gay"));
        assertNotNull(org.getEmployee("Manton Matthews"));
        assertNotNull(org.getEmployee("John Rose"));
        assertNotNull(org.getEmployee("Ryan Austin"));
        assertNotNull(org.getEmployee("Csilla Farkas"));
        assertNotNull(org.getRoom("2A01"));
        assertNotNull(org.getRoom("2A02"));
        assertNotNull(org.getRoom("2A03"));
        assertNotNull(org.getRoom("2A04"));
        assertNotNull(org.getRoom("2A05"));
    }

    @Test
    public void testGetNonexistentEmployee_throws() {
        Organization org = new Organization();
        try {
            org.getEmployee("Non Existent");
            fail("Should throw for missing employee");
        } catch (Exception e) {
            assertTrue(e.getMessage().toLowerCase().contains("does not exist"));
        }
    }

    @Test
    public void testGetNonexistentRoom_throws() {
        Organization org = new Organization();
        try {
            org.getRoom("Nope");
            fail("Should throw for missing room");
        } catch (Exception e) {
            assertTrue(e.getMessage().toLowerCase().contains("does not exist"));
        }
    }

    @Test
    public void testGetRoom_validAndInvalid() throws Exception {
        Organization o = new Organization();
        assertNotNull(o.getRoom("2A01"));
        assertThrows(Exception.class, () -> o.getRoom("X999"));
    }

    @Test
    public void testGetEmployee_validAndInvalid() throws Exception {
        Organization o = new Organization();
        assertNotNull(o.getEmployee("Greg Gay"));
        assertThrows(Exception.class, () -> o.getEmployee("Unknown Person"));
    }

    // ===== Structural White-Box Tests (Refinement) =====
    @Test
    public void testGetRoom_caseSensitivity() throws Exception {
        Organization o = new Organization();
        assertThrows(Exception.class, () -> o.getRoom("2a01")); // lowercase variant
    }


}
