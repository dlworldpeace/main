package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.AddressBook;
import seedu.address.storage.XmlAdaptedCarpark;
import seedu.address.storage.XmlAdaptedTag;
import seedu.address.storage.XmlSerializableAddressBook;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.CarparkBuilder;
import seedu.address.testutil.TestUtil;

public class XmlUtilTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlUtilTest");
    private static final Path EMPTY_FILE = TEST_DATA_FOLDER.resolve("empty.xml");
    private static final Path MISSING_FILE = TEST_DATA_FOLDER.resolve("missing.xml");
    private static final Path VALID_FILE = TEST_DATA_FOLDER.resolve("validCarparkFinder.xml");
    private static final Path MISSING_CARPARK_FIELD_FILE = TEST_DATA_FOLDER.resolve("missingCarparkField.xml");
    private static final Path INVALID_CARPARK_FIELD_FILE = TEST_DATA_FOLDER.resolve("invalidCarparkField.xml");
    private static final Path VALID_CARPARK_FILE = TEST_DATA_FOLDER.resolve("validCarpark.xml");
    private static final Path TEMP_FILE = TestUtil.getFilePathInSandboxFolder("tempCarparkFinder.xml");

    private static final String INVALID_COORDINATE = "9482asf424";

    private static final String VALID_ADDRESS = "BLK 508-517,520-533 HOUGANG AVENUE 10";
    private static final String VALID_CARPARK_NUMBER = "HG38";
    private static final String VALID_CARPARK_TYPE = "SURFACE CAR PARK";
    private static final String VALID_COORDINATE = "34274.4064, 39391.9731";
    private static final String VALID_FREE_PARKING = "SUN & PH FR 7AM-10.30PM";
    private static final String VALID_LOTS_AVAILABLE = "809";
    private static final String VALID_NIGHT_PARKING = "YES";
    private static final String VALID_SHORT_TERM = "WHOLE DAY";
    private static final String VALID_TOTAL_LOTS = "1279";
    private static final String VALID_TYPE_OF_PARKING = "ELECTRONIC PARKING";

    private static final List<XmlAdaptedTag> VALID_TAGS = Collections.singletonList(new XmlAdaptedTag("Home"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, AddressBook.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, AddressBook.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, AddressBook.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        AddressBook dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableAddressBook.class).toModelType();
        assertEquals(9, dataFromFile.getCarparkList().size());
    }

    @Test
    public void xmlAdaptedCarparkFromFile_fileWithMissingCarparkField_validResult() throws Exception {
        XmlAdaptedCarpark actualCarpark = XmlUtil.getDataFromFile(
                MISSING_CARPARK_FIELD_FILE, XmlAdaptedCarparkWithRootElement.class);
        XmlAdaptedCarpark expectedCarpark = new XmlAdaptedCarpark(VALID_ADDRESS, null,
                VALID_CARPARK_TYPE, VALID_COORDINATE, VALID_FREE_PARKING, VALID_LOTS_AVAILABLE,
                VALID_NIGHT_PARKING, VALID_SHORT_TERM, VALID_TOTAL_LOTS, VALID_TYPE_OF_PARKING,
                VALID_TAGS);
        assertEquals(expectedCarpark, actualCarpark);
    }

    @Test
    public void xmlAdaptedCarparkFromFile_fileWithInvalidCarparkField_validResult() throws Exception {
        XmlAdaptedCarpark actualCarpark = XmlUtil.getDataFromFile(
                INVALID_CARPARK_FIELD_FILE, XmlAdaptedCarparkWithRootElement.class);
        XmlAdaptedCarpark expectedCarpark = new XmlAdaptedCarpark(VALID_ADDRESS, VALID_CARPARK_NUMBER,
                VALID_CARPARK_TYPE, INVALID_COORDINATE, VALID_FREE_PARKING, VALID_LOTS_AVAILABLE,
                VALID_NIGHT_PARKING, VALID_SHORT_TERM, VALID_TOTAL_LOTS, VALID_TYPE_OF_PARKING,
                VALID_TAGS);
        assertEquals(expectedCarpark, actualCarpark);
    }

    @Test
    public void xmlAdaptedCarparkFromFile_fileWithValidCarpark_validResult() throws Exception {
        XmlAdaptedCarpark actualCarpark = XmlUtil.getDataFromFile(
                VALID_CARPARK_FILE, XmlAdaptedCarparkWithRootElement.class);
        XmlAdaptedCarpark expectedCarpark = new XmlAdaptedCarpark(VALID_ADDRESS, VALID_CARPARK_NUMBER,
                VALID_CARPARK_TYPE, VALID_COORDINATE, VALID_FREE_PARKING, VALID_LOTS_AVAILABLE,
                VALID_NIGHT_PARKING, VALID_SHORT_TERM, VALID_TOTAL_LOTS, VALID_TYPE_OF_PARKING,
                VALID_TAGS);
        assertEquals(expectedCarpark, actualCarpark);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new AddressBook());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new AddressBook());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        FileUtil.createFile(TEMP_FILE);
        XmlSerializableAddressBook dataToWrite = new XmlSerializableAddressBook(new AddressBook());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableAddressBook.class);
        assertEquals(dataToWrite, dataFromFile);

        AddressBookBuilder builder = new AddressBookBuilder(new AddressBook());
        dataToWrite = new XmlSerializableAddressBook(
                builder.withCarpark(new CarparkBuilder().build()).build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableAddressBook.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to {@code XmlAdaptedCarpark}
     * objects.
     */
    @XmlRootElement(name = "carparks")
    private static class XmlAdaptedCarparkWithRootElement extends XmlAdaptedCarpark {}
}
