package Util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** 
 * Parses XML file and text file, passes data to CellGroup through Game
 * 
 * Borrowed a lot of code from Robert Duvall's example_xml project to use for the XML parser
 *  Also used some code on StackOverflow 
 *  
 *  @author Robert Duvall
 *  @author Owen Smith
 *  @author Matthew Dickson
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** Class Util: Manages XML and text file parsing. Contains fields for XML files to parse through and 
 * Assumptions: Scanners and xml parsers encounter no errors in their parsing. If errors like incorrect data in the configuration xml file or a null 
 * file as a result of not choosing one, it has exception throwers that display pop-ups and prompt the user to try a different option.
 * Dependencies: Depends on the file the Game class passes to be parsed.
 * Example of how to use it: create a Util instance. Use the parser to parse xml files for simulation configurations, the readTextFile to parse for 
 * titles on various components for the UI, and other public methods to get necessary data.
 * Credit to Robert Duvall's example_xml project for many methods of XML parsing
 * 
 * @author Robert Duvall
 * @author Matthew Dickson
 * @author Owen Smith
 * 
 *
 */
public class Util {
    public static final int GLOBAL_DATA_FIELDS = 8;
	// name of root attribute that notes the type of file expecting to parse
    public final String TYPE_ATTRIBUTE = "animation";
    // keep only one documentBuilder because it is expensive to make and can reset it before parsing
    public final DocumentBuilder DOCUMENT_BUILDER = getDocumentBuilder();
    // it is generally accepted behavior that the chooser remembers where user left it last
    public static final String DATA_TYPE = "Simulation";
    public static final String UITEXT_FILE = "ButtonSliderLabels.txt";
    public static final String SCREENTEXTS_FILE = "Statuses.xml";
	public static final List<String> DATA_FIELDS = Arrays.asList(new String[] {
			"gridShape",
			"stroke",
			"neigh",
			"torus",
			"simType",
			"gridWidth",
			"gridHeight",
			"numStates",
			"Cell",
	        "cellDist",
	        "cellConfig",
	    });
	public static final int LAST_ELEMENT = DATA_FIELDS.size() - 1;
	public static final List<String> INNER_DATA_FIELDS = Arrays.asList(new String[] {
			"state",
			"x",
			"y",
	});
	public static final List<String> BUTTONSLIDER_LABELS = Arrays.asList(new String[] {
			"Pause",
			"Resume",
			"Load_new_file",
			"Step",
			"Save",
			"Speed",
	});
	public static final int CELLSTATE_DEX = 9;

	// kind of data files to look for
	public static final String DATA_FILE_EXTENSION = "*.xml";
    public static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Simulation animation=\"Simulation\">\n";
    public static final String PLACEHOLDER = "\t<cellDist type=\"%d\">0</cellDist>\n";
    public static final String FOOTER = "</Simulation>";
    
    private Element root;
    
    /**
     * Reads an XML file and returns data
     * 
     * @param dataFile	XML file to be parsed
     * @return	ArrayList of all the data from the XML file
     * @throws IOException	if the file the user chooses is invalid
     */
    public List<String> parseText(File dataFile) throws IOException{
    	if (dataFile == null){
    		throw new IOException("Data file is null!");
    	}
    	root = getRootElement(dataFile);
    	if(root == null) System.out.println("parseText root is not null");
    	if(!isValidFile(root, DATA_TYPE)) {
    		throw new XMLException("XML file does not represent a %s", DATA_TYPE);
    	}
    	// read data associated with the fields given by the object
    	ArrayList<String> results = new ArrayList<String>();
    	List<String> attributes;
    	if(dataFile.getName().equals(UITEXT_FILE)) attributes = BUTTONSLIDER_LABELS;
    	else attributes = DATA_FIELDS;
    	for(String field:attributes) {
    		try{
    			readField(results, field);
    		}catch(IOException e){
    			UI.exceptionThrower(e);
    		}
    	}
    	//for(String s : results) System.out.println(s);
    	return results;
    }

    private void readField(ArrayList<String> results, String field) throws IOException {
		if("cell".equals(field.toLowerCase())){
			handleSpecificCells(root, results);
		}
		else if ("gridshape".equals(field.toLowerCase())){
			results.add(getGridShape(getTextValue(root, field).get(0)));
		}
		else if("neigh".equals(field.toLowerCase())){
			results.add(getNeighConfig(getTextValue(root, field).get(0)));
		}
		else{
			results.addAll(getTextValue(root, field));
		}
	}
    
    private String getGridShape(String s) throws IOException{
		switch(s.toLowerCase()){
		case "square": return "" + 0;
		case "hex": return "" + 1;
		case "tri": return "" + 2;
		case "0":
		case "1":
		case "2":
			return s;
		default: throw new IOException("Unrecognized shape! Use square, hex, or tri!");
		}
	}

	private void handleSpecificCells(Element root, ArrayList<String> results) throws IOException {
		int cellWidth = Integer.parseInt(results.get(GLOBAL_DATA_FIELDS - 2));
		ArrayList<String> state = getTextValue(root, INNER_DATA_FIELDS.get(0));
		ArrayList<String> x = getTextValue(root, INNER_DATA_FIELDS.get(1));
		ArrayList<String> y = getTextValue(root, INNER_DATA_FIELDS.get(2));
		int i;
		for(i = 0; i < x.size(); i++){ 
			handleAddCellsToRet(results, cellWidth, state, x, y, i);
		}
		results.add(GLOBAL_DATA_FIELDS, "" + i);
	}


	private void handleAddCellsToRet(ArrayList<String> results, int cellWidth, ArrayList<String> state,
			ArrayList<String> x, ArrayList<String> y, int i) {
		int iX = Integer.parseInt(x.get(i));
		int iY = Integer.parseInt(y.get(i));
		String sIndex = "" + (cellWidth * iY + iX % cellWidth);
		results.add(state.get(i));
		results.add(sIndex);
	}
    
    /** getRootElement--gets the root of the XML file to be able to get elements within it
     *  Assumptions: file is of type XML and is not null 
     * @param xmlFile -- the XML file that is being passed in
     * @return -- the root of the XML file
     */
    private Element getRootElement (File xmlFile) {
        try {
            DOCUMENT_BUILDER.reset();
            Document xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
            return xmlDocument.getDocumentElement();
        }
        catch (SAXException | IOException e) {
            throw new XMLException(e);
        }
    }
    
    /** getTextValue method--gets the required text within each field of the XML file, which has important information for the simulation
     * Assumptions: tagName is a valid String tagName in the file and Element e is the root of the file
     * @param e -- the root of the XML file
     * @param tagName -- the name of the tag in the XML file
     * @return -- returns an arraylist of the contents of the XML file in a specific, already known order
     * @throws IOException -- if the root is misformatted or null, throws an exception 
     */
    private ArrayList<String> getTextValue(Element e, String tagName) throws IOException {
    	ArrayList<String> ret = new ArrayList<String>();
    	NodeList nodeList = e.getElementsByTagName(tagName);
    	if (nodeList != null) {
    		for (int i = 0; i < nodeList.getLength(); i++) {
    			Element node = (Element) nodeList.item(i);
    			String text = node.getTextContent();
    			ret.add(text);
    		}
    		return ret;
    	}
    	else {
            throw new IOException("No header in XML file; file might be blank!");
    	}
    }
    
    /** getCellStateNames method--gets the names of the cell states specified in the XML file to be included on the graph and in the drop-down menus
     *  Assumptions: That the XML file has the tag names specified by the constants in this method
     * @return -- an arraylist of those cell state names
     * @throws IOException
     */
    public ArrayList<String> getCellStateNames() throws IOException {
    	ArrayList<String> ret = new ArrayList<String>();
    	if(root == null) System.out.println("getCellStateNames root is null");
    	NodeList nodeList = root.getElementsByTagName(DATA_FIELDS.get(CELLSTATE_DEX));
    	if (nodeList != null) {
    		for (int i = 0; i < nodeList.getLength(); i++) {
    			Element node = (Element) nodeList.item(i);
    			String text = node.getAttribute("type");
    			ret.add(text);
    		}
    		return ret;
    	}
    	else {
            throw new IOException("No matching tags found! Does the file have a proper header"
            		+ " and correct data fields?");
    	}
    }
	
    /** getDocumentBuilder -- Helper method to do the boilerplate code needed to make a documentBuilder for the XML parser. Assists in getting root.
     * Assumptions: The parser configuration is correct. If it is not, throws a specific XML exception.
     * @return -- a DocumentBuilder for the XML file
     */
    private DocumentBuilder getDocumentBuilder () {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new XMLException("Invalid parser configuration", e);
        }
    }
	
	/**
	 * isValidFile -- Returns if this is a valid XML file for the specified object type
	 * @param root--the root of the XML file
	 * @param type--the kind of XML file being parsed. For this case, it will just be an "animation" XML file. It can't be any others
	 * @return -- a boolean indicating if the XML file is valid
	 */
    private boolean isValidFile (Element root, String type) {
        return getAttribute(root, TYPE_ATTRIBUTE).equals(type);
    }
    
    /**
     *  Get value of Element's attribute
     * @param e -- the root of the file
     * @param attributeName -- the name of the tage that the attribute belongs to
     * @return--returns a String containing the name
     */
    private String getAttribute (Element e, String attributeName) {
        return e.getAttribute(attributeName);
    }
    
    /**
     * 
     * Save the current simulation configuration to an XML file
     * 
     * @param simData List of all the data needed to re-create the state
     * @param file File that this will save to
     * @throws IOException Error that could occur if the file is null. Should never
     * 			be thrown since the checking for if file is null occurs when it gets
     * 			selected. 
     */
    public void saveFile(List<String> simData, File file) throws IOException{
	    try{
    		FileWriter fileWrite = new FileWriter(file);
	    	String toWrite = getWritableString(simData);
	    	writeToFile(fileWrite, toWrite);
	    }catch(IOException e){
	    	throw new IOException("Could not write to chosen file! Do you have the right permissions?");
	    }
    }
    
	private void writeToFile(FileWriter fileWrite, String toWrite) throws IOException {
		fileWrite.write(toWrite);
		fileWrite.flush();
		fileWrite.close();
	}

	private String getWritableString(List<String> simData) {
		StringBuilder toWrite = new StringBuilder();
		toWrite.append(HEADER);
		int numCells = Integer.parseInt(simData.remove(GLOBAL_DATA_FIELDS));
		createOutputStrBldr(simData, toWrite, numCells);
		toWrite.append(FOOTER);
		return toWrite.toString();
	}


    private String getNeighConfig(String s) throws XMLException{
    	switch(s.toLowerCase()){
    	case "adj": return "" + 0;
    	case "dia": return "" + 1;
    	case "all": return "" + 2;
    	case "0":
		case "1":
		case "2":
			return s;
    	default: throw new XMLException("Illegal neighbor conifiguration!");
    	}
    }
    
	private void createOutputStrBldr(List<String> simData, StringBuilder toWrite, int numCells) {
		int width = Integer.parseInt(simData.get(GLOBAL_DATA_FIELDS - 2));
		boolean write = true;
		for(int i = 0; i < simData.size(); i++){
    		if(i < GLOBAL_DATA_FIELDS)
		    	toWrite.append("\t<" + DATA_FIELDS.get(i) + ">" + simData.get(i) + "</" + DATA_FIELDS.get(i) + ">\n");
    		else if(i < numCells + GLOBAL_DATA_FIELDS){
		    	createIndividualCell(simData, i, toWrite, width);
    		}
    		else{
    			if(write){
    				for(int j = 0; j < Integer.parseInt(simData.get(GLOBAL_DATA_FIELDS - 1)); j++){
    				toWrite.append(String.format(PLACEHOLDER, j));
    				}
    				write = false;
    			}
    			toWrite.append("\t<" + DATA_FIELDS.get(LAST_ELEMENT) + ">" + simData.get(i) + "</" + DATA_FIELDS.get(LAST_ELEMENT) + ">\n");
    		}
		}
	}
	
	private void createIndividualCell(List<String> simData, int i, StringBuilder toWrite, int width) {
		int index = i - GLOBAL_DATA_FIELDS;
		toWrite.append("\t<Cell>\n");
		toWrite.append("\t\t<" + INNER_DATA_FIELDS.get(0) + ">" + simData.get(i) +"</" + INNER_DATA_FIELDS.get(0) + ">\n");
		toWrite.append("\t\t<" + INNER_DATA_FIELDS.get(1) + ">" + index % width +"</" + INNER_DATA_FIELDS.get(1) + ">\n");
		toWrite.append("\t\t<" + INNER_DATA_FIELDS.get(2) + ">" + index/width + "</" + INNER_DATA_FIELDS.get(2) + ">\n");
		toWrite.append("\t</Cell>\n");
	}

	/**
	 * Read a .txt file with information for the sim like button labels or other text
	 * 
	 * 
	 * @param file file where the data is located
	 * @return the contents of the file as a string
	 * @throws FileNotFoundException thrown if the file is not found
	 */
	public List<String> readTextFile(File file) throws FileNotFoundException{
		List<String> text = new ArrayList<String>();
		Scanner scan;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw(e);
		}
		while (scan.hasNextLine())
			text.add(scan.nextLine());
		scan.close();
		return text;
	}
}
