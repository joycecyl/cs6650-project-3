package cs6650.kvstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigReader {
    private static final String numServerMatchStr = "M(:|=)\\s*(?<numServer>\\d+)";
    private static final String serverInstMatchStr = "server(?<serverId>\\d+)(:|=)\\s*(?<serverPort>\\d+)";
    
    private static final Pattern configMatcher = Pattern.compile(
            String.format("((%s)|(%s))\\s*",
                numServerMatchStr,
                serverInstMatchStr
            ));

    protected File configFile;
    protected String config;
    
    public Integer numServer;
    public HashMap<Integer, Integer> serverPorts;

    
	public ConfigReader(File configFile) throws FileNotFoundException {
		if (!configFile.exists()) {
			throw new FileNotFoundException(configFile.getPath());
		}
		
		try {
			config = new String(Files.readAllBytes(configFile.toPath()), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		parseConfigFile();
	}
	
	public ConfigReader(String configString) {
		configFile = null;
		config = configString;
		parseConfigFile();
	}

	protected void parseConfigFile() {
        serverPorts = new HashMap<Integer, Integer>();

        for(String line : config.split("\\r?\\n")) {
            Matcher result = configMatcher.matcher(line);
            if(!result.matches()) {
                System.err.println("ConfigReader: Invalid line:\n" + line);
            }
            else if (result.group("numServer") != null) {
                numServer = Integer.parseInt(result.group("numServer"));
            } else if (result.group("serverId") != null) {
                serverPorts.put(Integer.parseInt(result.group("serverId")),
                                  Integer.parseInt(result.group("serverPort")));
            }  else{
                System.err.println("ConfigReader: Invalid line:\n" + line);
            }
        }

        if (numServer == null) {
            throw new RuntimeException("Config file is missing one or more required lines!");
        }

        for(int i = 0; i < numServer; i++) {
            if (!serverPorts.containsKey(i))
                throw new RuntimeException("Must set port for metadata" + i);
        }
    }

    public int getNumServers() {
        return numServer;
    }

    public int getServerPort(int serverId) {
        return serverPorts.get(serverId);
    }
}