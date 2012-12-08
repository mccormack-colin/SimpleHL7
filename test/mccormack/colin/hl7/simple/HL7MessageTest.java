package mccormack.colin.hl7.simple;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HL7MessageTest {
	private List<String> segments = null;

	@Before
	public void setUp() throws Exception {
		segments = new ArrayList<String>();
		segments.add("MSH|^~\\&|SENDAPP|SENDFAC|RECAPP|RECFAC|201212081824||ADT^A01|123456|T|2.3|");
		segments.add("EVN|A01|201212081824");
		segments.add("PID|||454721||LAST^FIRST^^^^||19840610|M|||123 FAKE ST^^SOMETOWN^XX^98765||(216)123-4567|||M||400003403|");
		segments.add("PV1||O|UNIT^ROOM^BED||||777^ATTLAST^ATTFIRST|888^REFLAST^REFFIRST|999CONLAST^CONSFIRST|||||||||O|400003403|||||||||||||||||||||||||201212090800|");
	}

	@After
	public void tearDown() throws Exception {
		segments = null;
	}

	@Test
	public void testMessage() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWhichSegment() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetSegmentCount() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetSegment() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetField() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetField() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetComponent() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetComponent() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testAddSegment() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testAddSegmentToEnd() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testDeleteSegment() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetMessage() {
		fail("Not yet implemented"); // TODO
	}

}
