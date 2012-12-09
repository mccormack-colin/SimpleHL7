package mccormack.colin.hl7.simple;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HL7MessageTest {
	private static ArrayList<String> segments = null;
	
	private static final String MSH = "MSH";
	private static final String EVN = "EVN";
	private static final String PID = "PID";
	private static final String PV1 = "PV1";
	private static final String AL1 = "AL1";
	private static final String IN1 = "IN1";

	
	private static final String COMPONENT_MSH_9_0 = "ADT";
	private static final String COMPONENT_PV1_8_1 = "REFLAST";
	private static final String COMPONENT_AL1_3_1 = "PENECILLIN";
	
	private static final String FIELD_MSH_3 = "SENDINGAPP";
	private static final String FIELD_MSH_9 = COMPONENT_MSH_9_0 + "^A01";
	private static final String FIELD_PID_3 = "454721";
	private static final String FIELD_PV1_2 = "O";
	private static final String FIELD_PV1_8 = "888^" + COMPONENT_PV1_8_1 + "^REFFIRST";
	
	private static final String SEGMENT_MSH = MSH + "|^~\\&|" + FIELD_MSH_3 + "|SENDINGFAC|RECEIVINGAPP|RECEIVINGFAC|201212081824||" + FIELD_MSH_9 + "|123456|T|2.3|";
	private static final String SEGMENT_EVN = EVN + "|A01|201212081824";
	private static final String SEGMENT_PID = PID + "|||" + FIELD_PID_3 + "||LAST^FIRST^^^^||19840610|M|||123 FAKE ST^^SOMETOWN^XX^98765||(216)123-4567|||M||400003403|";
	private static final String SEGMENT_PV1 = PV1 + "||" + FIELD_PV1_2 + "|UNIT^ROOM^BED||||777^ATTLAST^ATTFIRST|" + FIELD_PV1_8 + "|999CONLAST^CONSFIRST|||||||||O|400003403|||||||||||||||||||||||||201212090800|";
	private static final String SEGMENT_AL1_1 = AL1 + "|1|FA|4223^SEAFOOD|MI|DIFFICULTY BREATHING|20120801|";
	private static final String SEGMENT_AL1_2 = AL1 + "|2|SV|7753^" + COMPONENT_AL1_3_1 + "|MI|RASH|20120801|";

	@Before
	public void setUp() throws Exception {
		segments = new ArrayList<String>();
		segments.add(SEGMENT_MSH);
		segments.add(SEGMENT_EVN);
		segments.add(SEGMENT_PID);
		segments.add(SEGMENT_PV1);
		segments.add(SEGMENT_AL1_1);
		segments.add(SEGMENT_AL1_2);
	}

	@After
	public void tearDown() throws Exception {
		segments = null;
	}

	@Test
	public void testWhichSegment() {
		HL7Message msg = new HL7Message(segments);
		assertEquals(0, msg.whichSegment(MSH, 1));
		assertEquals(1, msg.whichSegment(EVN, 1));
		assertEquals(2, msg.whichSegment(PID, 1));
		assertEquals(3, msg.whichSegment(PV1, 1));
		assertEquals(4, msg.whichSegment(AL1, 1));
		assertEquals(5, msg.whichSegment(AL1, 2));
		assertEquals(-1, msg.whichSegment(AL1, 3));
		assertEquals(-1, msg.whichSegment(IN1, 1));
	}

	@Test
	public void testGetSegmentCount() {
		HL7Message msg = new HL7Message(segments);
		assertEquals(1, msg.getSegmentCount(MSH));
		assertEquals(1, msg.getSegmentCount(EVN));
		assertEquals(1, msg.getSegmentCount(PID));
		assertEquals(1, msg.getSegmentCount(PV1));
		assertEquals(2, msg.getSegmentCount(AL1));
		assertEquals(0, msg.getSegmentCount(IN1));
	}

	@Test
	public void testGetSegment() {
		HL7Message msg = new HL7Message(segments);
		assertEquals(SEGMENT_MSH, msg.getSegment(MSH, 1));
		assertEquals(SEGMENT_EVN, msg.getSegment(EVN, 1));
		assertEquals(SEGMENT_PID, msg.getSegment(PID, 1));
		assertEquals(SEGMENT_PV1, msg.getSegment(PV1, 1));
		assertEquals(SEGMENT_AL1_1, msg.getSegment(AL1, 1));
		assertEquals(SEGMENT_AL1_2, msg.getSegment(AL1, 2));
		assertEquals(null, msg.getSegment(IN1, 1));	
	}

	@Test
	public void testGetField() {
		HL7Message msg = new HL7Message(segments);
		assertEquals(FIELD_MSH_3, msg.getField(MSH, 1, 3));
		assertEquals(FIELD_MSH_9, msg.getField(MSH, 1, 9));
		assertEquals(FIELD_PID_3, msg.getField(PID, 1, 3));
		assertEquals(FIELD_PV1_2, msg.getField(PV1, 1, 2));
		assertEquals(FIELD_PV1_8, msg.getField(PV1, 1, 8));
		assertEquals(null, msg.getField(IN1, 1, 3));
	}

	@Test
	public void testSetField() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetComponent() {
		HL7Message msg = new HL7Message(segments);
		assertEquals(COMPONENT_MSH_9_0, msg.getComponent(MSH, 1, 9, 0));
		assertEquals(COMPONENT_PV1_8_1, msg.getComponent(PV1, 1, 8, 1));
		assertEquals(COMPONENT_AL1_3_1, msg.getComponent(AL1, 2, 3, 1));		
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
