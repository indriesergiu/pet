package com.xmlservices.logic.api.commands.store.db;

import com.xmlservices.logic.api.commands.store.model.Attribute;
import com.xmlservices.logic.api.commands.store.model.Element;
import com.xmlservices.logic.api.commands.store.model.File;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author Sergiu Indrie
 */
public class MySQLStore implements Store {

    private static final Logger log = Logger.getLogger(MySQLStore.class);

    // DB connection static data
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL_PREFIX = "jdbc:mysql://";

    // Queries
    private static final String CREATE_FILE = "INSERT INTO file(id, name) VALUES (null, ?);";
    private static final String CREATE_ELEMENT = "INSERT INTO element(id, file_id, nr, type, prefix, localname, data, encoding, version) VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String CREATE_ATTRIBUTE = "INSERT INTO attribute(id, element_id, nr, name, value) VALUES (null, ?, ?, ?, ?);";
    private static final String GET_ELEMENT_NR = "SELECT * FROM element WHERE file_id=? AND nr=?;";
    private static final String GET_ATTRIBUTE_NR = "SELECT * FROM attribute WHERE element_id=? AND nr=?;";

    // DB connection data extracted from the config file
    private String user;
    private String pass;
    private String url;

    private Connection connection;

    public MySQLStore() {
        try {
            loadConfig();
            Class.forName(MYSQL_DRIVER).newInstance();
            connection = DriverManager.getConnection(JDBC_URL_PREFIX + url, user, pass);
        } catch (Exception e) {
            log.error("Error initializing DB connection", e);
            throw new IllegalStateException("Error initializing DB connection", e);
        }
    }

    private void loadConfig() throws IOException {
        log.debug("Starting configuration loading.");
        InputStream resourceAsStream = getClass().getResourceAsStream("/application.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        resourceAsStream.close();
        log.debug("Finished configuration loading.");

        user = properties.getProperty("store.user");
        pass = properties.getProperty("store.pass");
        url = properties.getProperty("store.url");

        if (user == null || pass == null || url == null) {
            throw new IllegalArgumentException("Invalid DB connection data");
        }
    }

    public void close() {
        log.info("Closing the DB connection..");
        try {
            connection.close();
        } catch (SQLException e) {
            log.warn("Connection with DB was not successfully closed.", e);
        }
    }

    @Override
    public void startTransaction() throws StoreException {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            String message = "An error occurred while setting the transaction start.";
            log.error(message, e);
            throw new StoreException(message, e);
        }
    }

    @Override
    public void endTransaction() throws StoreException {
        try {
            connection.commit();
        } catch (SQLException e) {
            String message = "An error occurred while committing the transaction.";
            log.error(message, e);
            // rollback the transaction
            try {
                connection.rollback();
            } catch (SQLException e1) {
                String rollbackErrorMessage = "An error occurred while rolling back the transaction.";
                log.error(rollbackErrorMessage, e1);
                throw new StoreException(rollbackErrorMessage, e1);
            }
            throw new StoreException(message, e);
        }
    }

    @Override
    public void cleanUp() throws StoreException {
        log.info("Cleaning up any remaining DB state. Rolling back changes..");
        try {
            connection.rollback();
        } catch (SQLException e1) {
            String rollbackErrorMessage = "An error occurred while rolling back existing changes.";
            log.error(rollbackErrorMessage, e1);
            throw new StoreException(rollbackErrorMessage, e1);
        }
    }

    @Override
    public File addFile(File file) throws StoreException {
        log.info("Adding file=" + file);
        try {
            PreparedStatement statement = connection.prepareStatement(CREATE_FILE, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            statement.setString(index, file.getName());
            log.debug("Performing executeUpdate with query=" + statement.toString());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                file.setId(generatedKeys.getInt(1));
            }
            statement.close();
            return file;
        } catch (SQLException e) {
            String message = MessageFormat.format("Could not create the file: {0}", file);
            log.error(message, e);
            throw new StoreException(message, e);
        }
    }

    @Override
    public Element addElement(Element element) throws StoreException {
        log.info("Adding element=" + element);
        try {
            PreparedStatement statement = connection.prepareStatement(CREATE_ELEMENT, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            statement.setInt(index++, element.getFileId());
            statement.setInt(index++, element.getNr());
            statement.setString(index++, element.getType());
            statement.setString(index++, element.getPrefix());
            statement.setString(index++, element.getLocalname());
            statement.setString(index++, element.getData());
            statement.setString(index++, element.getEncoding());
            statement.setString(index++, element.getVersion());
            log.debug("Performing executeUpdate with query=" + statement.toString());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                element.setId(generatedKeys.getInt(1));
            }
            statement.close();
            return element;
        } catch (SQLException e) {
            String message = MessageFormat.format("Could not create the element: {0}", element);
            log.error(message, e);
            throw new StoreException(message, e);
        }
    }

    @Override
    public Attribute addAttribute(Attribute attribute) throws StoreException {
        log.info("Adding attribute=" + attribute);
        try {
            PreparedStatement statement = connection.prepareStatement(CREATE_ATTRIBUTE, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            statement.setInt(index++, attribute.getElementId());
            statement.setInt(index++, attribute.getNr());
            statement.setString(index++, attribute.getName());
            statement.setString(index++, attribute.getValue());
            log.debug("Performing executeUpdate with query=" + statement.toString());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                attribute.setId(generatedKeys.getInt(1));
            }
            return attribute;
        } catch (SQLException e) {
            String message = MessageFormat.format("Could not create the attribute: {0}", attribute);
            log.error(message, e);
            throw new StoreException(message, e);
        }
    }

    @Override
    public Element getElement(int fileId, int nr) throws StoreException {
        log.info(MessageFormat.format("Obtaining element with fileId={0} and nr={1}", fileId, nr));
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ELEMENT_NR);
            int index = 1;
            statement.setInt(index++, fileId);
            statement.setInt(index++, nr);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null) {
                Element element = getElementFromResultSet(resultSet);
                log.debug("Obtained element=" + element);
                statement.close();
                return element;
            }

            throw new IllegalStateException("Returned result set is null");
        } catch (SQLException e) {
            String message = MessageFormat.format("Could not obtain the element with fileId={0} and nr={1}", fileId, nr);
            log.error(message, e);
            throw new StoreException(message, e);
        }
    }

    @Override
    public Attribute getAttribute(int elementId, int nr) throws StoreException {
        log.info(MessageFormat.format("Obtaining attribute with elementId={0} and nr={1}", elementId, nr));
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ATTRIBUTE_NR);
            int index = 1;
            statement.setInt(index++, elementId);
            statement.setInt(index++, nr);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null) {
                Attribute attribute = getAttributeFromResultSet(resultSet);
                log.debug("Obtained attribute=" + attribute);
                statement.close();
                return attribute;
            }

            throw new IllegalStateException("Returned result set is null");
        } catch (SQLException e) {
            String message = MessageFormat.format("Could not obtain the attribute with elementId={0} and nr={1}", elementId, nr);
            log.error(message, e);
            throw new StoreException(message, e);
        }
    }

    private Element getElementFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Element element = new Element();
            element.setData(resultSet.getString("data"));
            element.setEncoding(resultSet.getString("encoding"));
            element.setFileId(resultSet.getInt("file_id"));
            element.setId(resultSet.getInt("id"));
            element.setLocalname(resultSet.getString("localname"));
            element.setNr(resultSet.getInt("nr"));
            element.setPrefix(resultSet.getString("prefix"));
            element.setType(resultSet.getString("type"));
            element.setVersion(resultSet.getString("version"));
            return element;
        }
        throw new IllegalArgumentException("Received empty result set");
    }

    private Attribute getAttributeFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new Attribute(resultSet.getInt("id"), resultSet.getInt("element_id"), resultSet.getInt("nr"), resultSet.getString("name"), resultSet.getString("value"));
        }
        throw new IllegalArgumentException("Received empty result set");
    }

    @Override
    public Iterator<Element> getElementIterator(File file) {
        return new ElementIterator(this, file);
    }

    @Override
    public Iterator<Attribute> getAttributeIterator(Element element) {
        return new AttributeIterator(this, element);
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, StoreException {
        BasicConfigurator.configure();
        MySQLStore store = new MySQLStore();
        store.addFile(new File(null, "sdf"));
    }
}
