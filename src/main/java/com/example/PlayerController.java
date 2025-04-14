package com.example;

import org.codehaus.plexus.util.StringUtils;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    private static final Map<String, List<String>> TEAM_ROSTERS = new HashMap<>();

    static {
        TEAM_ROSTERS.put("nuggets", List.of("Nikola Jokic", "Jamal Murray", "Aaron Gordon", "Michael Porter Jr.", "Kentavious Caldwell-Pope"));
        TEAM_ROSTERS.put("lakers", List.of("LeBron James", "D'Angelo Russell", "Austin Reaves", "Rui Hachimura", "Luka Doncic"));
        TEAM_ROSTERS.put("warriors", List.of("Stephen Curry", "Klay Thompson", "Draymond Green", "Jimmy Butler", "Kevon Looney"));
        TEAM_ROSTERS.put("thunders", List.of("Shai Gilgeous-Alexander", "Josh Giddey", "Jalen Williams", "Chet Holmgren", "Lu Dort"));
        TEAM_ROSTERS.put("knicks", List.of("Julius Randle", "Jalen Brunson", "RJ Barrett", "Mitchell Robinson", "Immanuel Quickley"));
        TEAM_ROSTERS.put("suns", List.of("Kevin Durant", "Devin Booker", "Bradley Beal", "Deandre Ayton", "Eric Gordon"));
        TEAM_ROSTERS.put("cavs", List.of("Donovan Mitchell", "Darius Garland", "Evan Mobley", "Jarrett Allen", "Caris LeVert"));
    }

    @GetMapping("/api/players")
    public Map<String, Object> getTeamRoster(@RequestParam String team) {
        logger.info("Fetching roster for team: {}", team);

        List<String> roster = TEAM_ROSTERS.get(team.toLowerCase());
        if (roster == null) {
            logger.error("Team not found: {}", team);
            return Map.of("error", "Team not found");
        }

        // Use Plexus Utils to join player names into a single string
        String joinedPlayers = StringUtils.join(roster.iterator(), ", ");
        logger.info("Roster: {}", joinedPlayers);

        // Use XStream to serialize the roster into XML format
        XStream xStream = new XStream();
        xStream.alias("team", Map.class);
        String xml = xStream.toXML(Map.of("team", team, "players", roster));

        // Simulate a PostgreSQL database connection
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mydb", "myuser", "mypassword")) {
            logger.info("Connected to the PostgreSQL database successfully.");
        } catch (SQLException e) {
            logger.error("Failed to connect to the PostgreSQL database.", e);
        }

        return Map.of(
            "team", team,
            "players", roster,
            "joinedPlayers", joinedPlayers,
            "xml", xml
        );
    }
}