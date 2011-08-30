package dk.bestbrains.friendly;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseRepository implements Disposable {
    protected final ConnectionBuilder connectionBuilder;
    private Connection connection;

    public BaseRepository(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    public Connection getConnection() throws SQLException {
        if(connection == null)
            connection = connectionBuilder.getConnection();

        return connection;
    }

    @Override
    public void dispose() {
        try {
            if(connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
