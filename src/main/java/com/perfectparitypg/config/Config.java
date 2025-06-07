package com.perfectparitypg.config;

public class Config {

    public boolean LOAD_1_21_4_OVERLAY_DIRECTORIES = false;
    public YAMLParser YAML_PARSER;

    public Config (String name) {
        setUpConfig(name);
    }

    public void setUpConfig (String name) {
        YAML_PARSER = new YAMLParser(name);
        if (!YAML_PARSER.WRITTEN) return;
        YAML_PARSER.addComment("Utility settings\n");
        YAML_PARSER.addComment("Some resource packs require a matching pack format for overlays, normally if they support multiple versions");
        YAML_PARSER.addBooleanWithComment("use_1_21_4_resource_pack_format", "default: false", false);
    }

    public void readConfig () {
        LOAD_1_21_4_OVERLAY_DIRECTORIES = YAML_PARSER.readValue("load_1_21_4_overlay_directories").equalsIgnoreCase("true");
    }
}
