package com.perfectparitypg.config;

import com.perfectparitypg.PerfectParityPG;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class YAMLParser {
    public final String FILE_NAME;
    public Path FILE_PATH;
    public boolean WRITTEN;

    public YAMLParser (String file_name) {
        FILE_NAME = file_name;
        createFile();
    }

    public void createFile() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME + ".yaml");
        FILE_PATH = configPath;
        if (!Files.exists(configPath)) {
            try {
                Files.createFile(configPath);
                WRITTEN = true;
                return;
            } catch (IOException e) {
                PerfectParityPG.LOGGER.warn("PerfectParityPG: Config file creation failed. Report to mod author");
            }
        }
        WRITTEN = false;
    }

    public void addComment(String comment) {
        try {
            Files.writeString(FILE_PATH, "# " + comment + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            PerfectParityPG.LOGGER.warn("PerfectParityPG: failed to add comment to config");
        }
    }

    public void addBoolean(String var, boolean bool, boolean withComment) {
        try {
            Files.writeString(FILE_PATH, var + ": " + (bool ? "true" : "false") +
                    (withComment ? "" : "\n"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            PerfectParityPG.LOGGER.warn("PerfectParityPG: failed to add boolean to config");
        }
    }

    public void addBooleanWithComment(String var,String comment ,boolean bool) {
        try {
            addBoolean(var, bool, true);
            Files.writeString(FILE_PATH, " ", StandardOpenOption.APPEND);
            addComment(comment);
        } catch (IOException e) {
            PerfectParityPG.LOGGER.warn("PerfectParityPG: failed to add boolean with comment to config");
        }
    }

    public String readValue(String val) {

        if (FILE_PATH == null) {
            PerfectParityPG.LOGGER.error("PerfectParityPG: Config path is null!");
            return "NOT FOUND";
        }

        try {
            for (String line : Files.readAllLines(FILE_PATH)) {
                if (line.split(":")[0].equals(val)) {
                    return line.split(":")[1].split("#")[0].strip();
                }
            }
        } catch (IOException e) {
            PerfectParityPG.LOGGER.error("PerfectParityPG: FAILED TO READ CONFIG. DELETE CONFIG TO REGENERATE");
        }

        return "NOT FOUND";
    }
}
