package mccormack.colin.hl7.simple;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is designed to making common HL7 message manipulation easy.
 * 
 * @author mccormack-colin
 *
 */
public class Message {

	private ArrayList<String> segments;

	private static final String FIELD_SEPARATOR = "|";
	private static final String COMPONENT_SEPARATOR = "^";
	private static final String REPITION_SEPARATOR = "~";
	private static final String ESCAPE_CHAR = "\\";
	private static final String SUBCOMPONENT_SEPARATOR = "&";

	/**
	 * Creates an instance of the <code>Message</code> from an ArrayList.
	 * <p>
	 * @param segments <code>ArrayList</code> of segments that form a HL7 message
	 */
	public Message(ArrayList<String> segments) {
		this.segments = segments;
	}

	private boolean isSegment(String segment, String segmentName) {
		if (segment != null && segment.startsWith(segmentName + FIELD_SEPARATOR)) {
			return true;
		}

		return false;
	}
		
	private String getSub(String data, String sep, int num) {
		String sub = null;
		
		String[] subs = data.split(ESCAPE_CHAR + sep);
		if (subs.length > num && subs[num].length() > 0) {
			sub = subs[num];
		}
		
		return sub;
	}

	/**
	 * Determines what position in the message a particular segment is
	 * @param segmentName the segment of interest
	 * @param instance which occurrence in the message, 1..n
	 * @return <code>int</code> of position 0..n, -1 if the segment instance does not exist
	 */
	public int whichSegment(String segmentName, int instance) {
		int count = -1;
		int which = -1;
		int current = 1;

		for (Iterator<String> iter = this.segments.iterator(); iter.hasNext();) {
			String segment = (String) iter.next();
			count++;
			if (isSegment(segment, segmentName)) {
				if (current == instance) {
					which = count;
					break;
				}
				current++;
			}
		}

		return which;
	}

	/**
	 * Finds how many occurrences of the same segment type exist
	 * @param segmentName the segment of interest
	 * @return <code>int</code> of 0..n occurrences
	 */
	public int getSegmentCount(String segmentName) {
		int count = 0;

		for (Iterator<String> iter = this.segments.iterator(); iter.hasNext();) {
			String segment = (String) iter.next();
			if (isSegment(segment, segmentName)) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Gets the requested segment from the message
	 * @param segmentName segment of interest
	 * @param instance which occurrence in the message, 1..n
	 * @return a <code>String</code> representation of an HL7 segment, <code>null</code> if not found
	 */
	public String getSegment(String segmentName, int instance) {
		String seg = null;

		int which = whichSegment(segmentName, instance);
		if (which > -1) {
			seg = this.segments.get(which);
		}

		return seg;
	}

	/**
	 * Gets the requested field from the specified segment
	 * <p>
	 * This class will handle fields in the MSH segment properly,
	 * no need to adjust for MSH.1
	 * @param segmentName segment of interest
	 * @param instance which occurrence in the message, 1..n
	 * @param fieldNum which field in the segment, 1..n
	 * @return a <code>String</code> representation of a field, <code>null</code> if not found
	 */
	public String getField(String segmentName, int instance, int fieldNum) {
		if ("MSH".equals(segmentName)) {
		    if (fieldNum == 1) {
			return FIELD_SEPARATOR;
		    }
		    fieldNum-=1;
		}
		String field = null;

		String seg = getSegment(segmentName, instance);
		if (seg != null) {
			field = getSub(seg, FIELD_SEPARATOR, fieldNum);
		}

		return field;
	}

	/**
	 * Replaces data in the field with new data
	 * <p>
	 * This method will also blank out field that needs to be removed 
	 * by passing an empty <code>String</code>
	 * or <code>null</code> in for <code>newValue</code>
	 * <p>
	 * This method will create the segment if it doesn't exist placing 
	 * the new segment at the end of the message assuming <code>newValue</code>
	 * is not <code>null</code> or empty
	 * @param segmentName segment of interest
	 * @param instance which occurrence in the message, 1..n
	 * @param fieldNum which field in the segment, 1..n
	 * @param newValue value to replace current value with
	 */
	public void setField(String segmentName, int instance, int fieldNum, String newValue) {
		if ("MSH".equals(segmentName)) {
			fieldNum-=1;
		}
		int which = whichSegment(segmentName, instance);
		if (which > -1) {
			String seg = getSegment(segmentName, instance);
			if (seg != null) {
				StringBuffer workSeg = new StringBuffer("");

				String[] fields = seg.split(ESCAPE_CHAR + FIELD_SEPARATOR);
				for (int i = 0; i < Math.max(fields.length, fieldNum + 1); i++) {
					if (i > 0) {
						workSeg.append(FIELD_SEPARATOR);
					}
					
					if (i == fieldNum) {
						workSeg.append(newValue != null ? newValue : "");
					} else {
					    workSeg.append(fields.length > i ? fields[i] : "");
					}
				}

				this.segments.set(which, workSeg.toString());
			}
		} else if (newValue != null && newValue.length() > 0){
			StringBuffer newSeg = new StringBuffer(segmentName);
			for (int i = 0; i < fieldNum; i++) {
				newSeg.append("|");
			}
			newSeg.append(newValue);
			addSegmentToEnd(newSeg.toString());
		}
	}

	/**
	 * Gets the requested component from the specified field
	 * @param segmentName segment of interest
	 * @param instance which occurrence in the message, 1..n
	 * @param fieldNum which field in the segment, 1..n
	 * @param componentNum which compoent in the field, 0..n
	 * @return a <code>String</code> representation of a component, <code>null</code> if not found
	 */
	public String getComponent(String segmentName, int instance, int fieldNum, int componentNum) {
		String comp = null;

		String field = getField(segmentName, instance, fieldNum);
		if (field != null) {
			comp = getSub(field, COMPONENT_SEPARATOR, componentNum);
		}

		return comp;
	}

	/**
	 * Replaces data in the component with new data
	 * <p>
	 * This method will also blank out a component that needs to be removed 
	 * by passing an empty <code>String</code>
	 * or <code>null</code> in for <code>newValue</code>
	 * <p>
	 * This method will create the segment if it doesn't exist placing 
	 * the new segment at the end of the message assuming <code>newValue</code>
	 * is not <code>null</code> or empty
	 * @param segmentName segment of interest
	 * @param instance which occurrence in the message, 1..n
	 * @param fieldNum which field in the segment, 1..n
	 * @param componentNum which component in the field, 0..n
	 * @param newValue value to replace current value with
	 */
	public void setComponent(String segmentName, int instance, int fieldNum, int componentNum, String newValue) {
		String newField = null;

		String field = getField(segmentName, instance, fieldNum);
		StringBuffer workField = new StringBuffer("");

		if (field == null) {
			field = "";
		}
		String[] comps = field.split(ESCAPE_CHAR + COMPONENT_SEPARATOR);
		for (int i = 0; i < Math.max(comps.length, componentNum + 1); i++) {
			if (i > 0) {
				workField.append(COMPONENT_SEPARATOR);
			}
			
			if (i == componentNum) {
				workField.append(newValue != null ? newValue : "");
			} else {
			    workField.append(comps.length > i ? comps[i] : "");
			}
		}
		newField = workField.toString();
		setField(segmentName, instance, fieldNum, newField);
	}
	
	/**
	 * Adds a segment to the message
	 * @param newSeg <code>String</code> representation of the new HL7 segment
	 * @param position position to place the new segment, 0..n
	 */
	public void addSegment(String newSeg, int position) {
		if (newSeg != null) {
			this.segments.add(position, newSeg);
		}
	}
	
	/**
	 * Adds a segment to the end of the message
	 * @param newSeg <code>String</code> representation of the new HL7 segment
	 */
	public void addSegmentToEnd(String newSeg) {
		addSegment(newSeg, this.segments.size());
	}
	
	/**
	 * Removes a segment from the message
	 * @param segmentName segment of interest
	 * @param instance which occurrence in the message, 1..n
	 * @return <code>boolean</code> value indicating deletion of segment, if the segment
	 * doesn't exits false will be returned
	 */
	public boolean deleteSegment(String segmentName, int instance) {
		int which = whichSegment(segmentName, instance);
		if (which != -1) {
			this.segments.remove(which);
			return true;
		}
		
		return false;
	}

	private ArrayList getSegments() {
		return this.segments;
	}
	
	/**
	 * Rebuilds the message with proper new line characters
	 * @return <code>String</code> representation of the HL7 message
	 * @see com.patientkeeper.monaco.CdsMessage#setFlatwire
	 */
	public String getMessage() {
		StringBuffer outFlatwire = new StringBuffer();
		for (Iterator iter = this.getSegments().iterator(); iter.hasNext();) {
			String segment = (String) iter.next();
			outFlatwire.append(segment);
			if (iter.hasNext()) {
				outFlatwire.append("\r");
			}
		}
		return outFlatwire.toString();
	}
}