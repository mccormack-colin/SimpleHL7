package mccormack.colin.hl7.simple;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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
	
	private static final char SEGMENT_SEPARATOR = '\r';

	
	private static final String COMPONENT_MSH_9_0 = "ADT";
	private static final String COMPONENT_PID_5_1 = "FIRST";
	private static final String COMPONENT_PID_5_1_NEW = "NEWFIRST";
	private static final String COMPONENT_PV1_8_1 = "REFLAST";
	private static final String COMPONENT_AL1_3_1 = "PENECILLIN";
	
	private static final String FIELD_MSH_3 = "SENDINGAPP";
	private static final String FIELD_MSH_9 = COMPONENT_MSH_9_0 + "^A01";
	private static final String FIELD_PID_3 = "454721";
	private static final String FIELD_PV1_2 = "O";
	private static final String FIELD_PV1_8 = "888^" + COMPONENT_PV1_8_1 + "^REFFIRST";
	private static final String FIELD_MSH_5 = "RECEIVINGAPP";
	private static final String FIELD_MSH_5_NEW = "NEWRECEIVINGAPP";
	
	private static final String SEGMENT_MSH = MSH + "|^~\\&|" + FIELD_MSH_3 + "|SENDINGFAC|" + FIELD_MSH_5 + "|RECEIVINGFAC|201212081824||" + FIELD_MSH_9 + "|123456|T|2.3|";
	private static final String SEGMENT_EVN = EVN + "|A01|201212081824";
	private static final String SEGMENT_PID = PID + "|||" + FIELD_PID_3 + "||LAST^" + COMPONENT_PID_5_1 + "^^^^||19840610|M|||123 FAKE ST^^SOMETOWN^XX^98765||(216)123-4567|||M||400003403|";
	private static final String SEGMENT_PV1 = PV1 + "||" + FIELD_PV1_2 + "|UNIT^ROOM^BED||||777^ATTLAST^ATTFIRST|" + FIELD_PV1_8 + "|999CONLAST^CONSFIRST|||||||||O|400003403|||||||||||||||||||||||||201212090800|";
	private static final String SEGMENT_AL1_1 = AL1 + "|1|FA|4223^SEAFOOD|MI|DIFFICULTY BREATHING|20120801|";
	private static final String SEGMENT_AL1_2 = AL1 + "|2|SV|7753^" + COMPONENT_AL1_3_1 + "|MI|RASH|20120801|";
	private static final String SEGMENT_NTE = "NTE|1||some nurse comment here";

	@Before
	public void setUp() throws Exception {
		segments = new ArrayList<String>();
		segments.add(SEGMENT_MSH);
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
		assertEquals(1, msg.whichSegment(PID, 1));
		assertEquals(2, msg.whichSegment(PV1, 1));
		assertEquals(3, msg.whichSegment(AL1, 1));
		assertEquals(4, msg.whichSegment(AL1, 2));
		assertEquals(-1, msg.whichSegment(AL1, 3));
		assertEquals(-1, msg.whichSegment(IN1, 1));
	}

	@Test
	public void testGetSegmentCount() {
		HL7Message msg = new HL7Message(segments);
		assertEquals(1, msg.getSegmentCount(MSH));
		assertEquals(1, msg.getSegmentCount(PID));
		assertEquals(1, msg.getSegmentCount(PV1));
		assertEquals(2, msg.getSegmentCount(AL1));
		assertEquals(0, msg.getSegmentCount(IN1));
	}

	@Test
	public void testGetSegment() {
		HL7Message msg = new HL7Message(segments);
		assertEquals(SEGMENT_MSH, msg.getSegment(MSH, 1));
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
		HL7Message msg = new HL7Message(segments);
		assertEquals(FIELD_MSH_5, msg.getField(MSH, 1, 5));
		msg.setField(MSH, 1, 5, FIELD_MSH_5_NEW);
		assertEquals(FIELD_MSH_5_NEW, msg.getField(MSH, 1, 5));
		assertThat(FIELD_MSH_5, not(equalTo(msg.getField(MSH, 1, 5))));
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
		HL7Message msg = new HL7Message(segments);
		assertEquals(COMPONENT_PID_5_1, msg.getComponent(PID, 1, 5, 1));
		msg.setComponent(PID, 1, 5, 1, COMPONENT_PID_5_1_NEW);
		assertEquals(COMPONENT_PID_5_1_NEW, msg.getComponent(PID, 1, 5, 1));
	}

	@Test
	public void testAddSegment() {
		StringBuffer msgStr = new StringBuffer("");
		msgStr.append(SEGMENT_MSH).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PID).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PV1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_2);
		
		HL7Message msg = new HL7Message(segments);
		
		assertEquals(msgStr.toString(), msg.getMessage());
		
		msg.addSegment(SEGMENT_EVN, 1);
		
		msgStr = new StringBuffer("");
		msgStr.append(SEGMENT_MSH).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_EVN).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PID).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PV1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_2);
		
		assertEquals(msgStr.toString(), msg.getMessage());
	}

	@Test
	public void testAddSegmentToEnd() {
		StringBuffer msgStr = new StringBuffer("");
		msgStr.append(SEGMENT_MSH).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PID).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PV1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_2);
		
		HL7Message msg = new HL7Message(segments);
		
		assertEquals(msgStr.toString(), msg.getMessage());
		
		msg.addSegmentToEnd(SEGMENT_NTE);
		
		msgStr = new StringBuffer("");
		msgStr.append(SEGMENT_MSH).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PID).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PV1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_2).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_NTE);
		
		assertEquals(msgStr.toString(), msg.getMessage());
	}

	@Test
	public void testDeleteSegment() {
		StringBuffer msgStr = new StringBuffer("");
		msgStr.append(SEGMENT_MSH).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PID).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PV1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_2);
		
		HL7Message msg = new HL7Message(segments);
		
		assertEquals(msgStr.toString(), msg.getMessage());
		
		msg.deleteSegment(AL1, 1);
		
		msgStr = new StringBuffer("");
		msgStr.append(SEGMENT_MSH).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PID).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PV1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_2);
		
		assertEquals(msgStr.toString(), msg.getMessage());
	}

	@Test
	public void testGetMessage() {
		StringBuffer msgStr = new StringBuffer("");
		msgStr.append(SEGMENT_MSH).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PID).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_PV1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_1).append(SEGMENT_SEPARATOR);
		msgStr.append(SEGMENT_AL1_2);
		
		HL7Message msg = new HL7Message(segments);
		
		assertEquals(msgStr.toString(), msg.getMessage());
	}

}
