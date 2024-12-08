package edu.vutfit.ThirstQuest.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
@Profile("prod")
public class DataSourceConfig {

    @Value("${DATABASE_URL}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() throws Exception {
        // Příklad: postgresql://postgres:heslo@postgres.railway.internal:5432/railway
        // Změníme prefix 'postgresql://' na 'http://' aby to URI parser zvládl přečíst
        URI uri = new URI(databaseUrl.replace("postgresql://", "http://"));

        String userInfo = uri.getUserInfo(); // "postgres:heslo"
        String username = userInfo.split(":")[0];
        String password = userInfo.split(":")[1];

        // Sestavení JDBC URL
        // jdbc:postgresql://HOST:PORT/DATABASE
        String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("org.postgresql.Driver");

        // Volitelně můžete nastavit další properties:
        // ds.setMaximumPoolSize(5);
        // ds.setConnectionTimeout(30000);

        return ds;
    }
}

