// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.manager;

import me.mohalk.banzem.features.modules.Module;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import java.util.Collection;
import com.google.gson.Gson;
import java.nio.file.Path;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.file.OpenOption;
import com.google.gson.GsonBuilder;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Objects;
import java.io.File;
import java.util.List;
import java.util.Iterator;
import me.mohalk.banzem.features.modules.render.XRay;
import java.util.UUID;
import java.util.Map;
import com.google.gson.JsonObject;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.setting.EnumConverter;
import me.mohalk.banzem.features.setting.Bind;
import com.google.gson.JsonElement;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.Feature;
import java.util.ArrayList;
import me.mohalk.banzem.util.Util;

public class ConfigManager implements Util
{
    public ArrayList<Feature> features;
    public String config;
    public boolean loadingConfig;
    public boolean savingConfig;
    
    public ConfigManager() {
        this.features = new ArrayList<Feature>();
        this.config = "banzem/config/";
    }
    
    public static void setValueFromJson(final Feature feature, final Setting setting, final JsonElement element) {
        final String type = setting.getType();
        switch (type) {
            case "Boolean": {
                setting.setValue(element.getAsBoolean());
                break;
            }
            case "Double": {
                setting.setValue(element.getAsDouble());
                break;
            }
            case "Float": {
                setting.setValue(element.getAsFloat());
                break;
            }
            case "Integer": {
                setting.setValue(element.getAsInt());
                break;
            }
            case "String": {
                final String str = element.getAsString();
                setting.setValue(str.replace("_", " "));
                break;
            }
            case "Bind": {
                setting.setValue(new Bind.BindConverter().doBackward(element));
                break;
            }
            case "Enum": {
                try {
                    final EnumConverter converter = new EnumConverter(((Enum)setting.getValue()).getClass());
                    final Enum value = converter.doBackward(element);
                    setting.setValue((value == null) ? setting.getDefaultValue() : value);
                }
                catch (Exception ex) {}
                break;
            }
            default: {
                Banzem.LOGGER.error("Unknown Setting type for: " + feature.getName() + " : " + setting.getName());
                break;
            }
        }
    }
    
    private static void loadFile(final JsonObject input, final Feature feature) {
        for (final Map.Entry entry : input.entrySet()) {
            final String settingName = (String) entry.getKey();
            final JsonElement element = (JsonElement) entry.getValue();
            if (feature instanceof FriendManager) {
                try {
                    Banzem.friendManager.addFriend(new FriendManager.Friend(element.getAsString(), UUID.fromString(settingName)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                boolean settingFound = false;
                for (final Setting setting : feature.getSettings()) {
                    if (!settingName.equals(setting.getName())) {
                        continue;
                    }
                    try {
                        setValueFromJson(feature, setting, element);
                    }
                    catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    settingFound = true;
                }
                if (settingFound) {
                    continue;
                }
            }
            if (feature instanceof XRay) {
                feature.register(new Setting(settingName, true, v -> ((XRay)feature).showBlocks.getValue()));
            }
        }
    }
    
    public void loadConfig(final String name) {
        this.loadingConfig = true;
        final List<File> files = Arrays.stream(Objects.requireNonNull(new File("banzem").listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        this.config = (files.contains(new File("banzem/" + name + "/")) ? ("banzem/" + name + "/") : "banzem/config/");
        Banzem.friendManager.onLoad();
        for (final Feature feature : this.features) {
            try {
                this.loadSettings(feature);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.saveCurrentConfig();
        this.loadingConfig = false;
    }
    
    public void saveConfig(final String name) {
        this.savingConfig = true;
        this.config = "banzem/" + name + "/";
        final File path = new File(this.config);
        if (!path.exists()) {
            path.mkdir();
        }
        Banzem.friendManager.saveFriends();
        for (final Feature feature : this.features) {
            try {
                this.saveSettings(feature);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.saveCurrentConfig();
        this.savingConfig = false;
    }
    
    public void saveCurrentConfig() {
        final File currentConfig = new File("banzem/currentconfig.txt");
        try {
            if (currentConfig.exists()) {
                final FileWriter writer = new FileWriter(currentConfig);
                final String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("banzem", ""));
                writer.close();
            }
            else {
                currentConfig.createNewFile();
                final FileWriter writer = new FileWriter(currentConfig);
                final String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("banzem", ""));
                writer.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String loadCurrentConfig() {
        final File currentConfig = new File("banzem/currentconfig.txt");
        String name = "config";
        try {
            if (currentConfig.exists()) {
                final Scanner reader = new Scanner(currentConfig);
                while (reader.hasNextLine()) {
                    name = reader.nextLine();
                }
                reader.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
    
    public void resetConfig(final boolean saveConfig, final String name) {
        for (final Feature feature : this.features) {
            feature.reset();
        }
        if (saveConfig) {
            this.saveConfig(name);
        }
    }
    
    public void saveSettings(final Feature feature) throws IOException {
        final JsonObject object = new JsonObject();
        final File directory = new File(this.config + this.getDirectory(feature));
        if (!directory.exists()) {
            directory.mkdir();
        }
        final String featureName;
        final Path outputFile;
        if (!Files.exists(outputFile = Paths.get(featureName = this.config + this.getDirectory(feature) + feature.getName() + ".json", new String[0]), new LinkOption[0])) {
            Files.createFile(outputFile, (FileAttribute<?>[])new FileAttribute[0]);
        }
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String json = gson.toJson((JsonElement)this.writeSettings(feature));
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile, new OpenOption[0])));
        writer.write(json);
        writer.close();
    }
    
    public void init() {
        this.features.addAll(Banzem.moduleManager.modules);
        this.features.add(Banzem.friendManager);
        final String name = this.loadCurrentConfig();
        this.loadConfig(name);
        Banzem.LOGGER.info("Config loaded.");
    }
    
    private void loadSettings(final Feature feature) throws IOException {
        final String featureName = this.config + this.getDirectory(feature) + feature.getName() + ".json";
        final Path featurePath = Paths.get(featureName, new String[0]);
        if (!Files.exists(featurePath, new LinkOption[0])) {
            return;
        }
        this.loadPath(featurePath, feature);
    }
    
    private void loadPath(final Path path, final Feature feature) throws IOException {
        final InputStream stream = Files.newInputStream(path, new OpenOption[0]);
        try {
            loadFile(new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject(), feature);
        }
        catch (IllegalStateException e) {
            Banzem.LOGGER.error("Bad Config File for: " + feature.getName() + ". Resetting...");
            loadFile(new JsonObject(), feature);
        }
        stream.close();
    }
    
    public JsonObject writeSettings(final Feature feature) {
        final JsonObject object = new JsonObject();
        final JsonParser jp = new JsonParser();
        for (final Setting setting : feature.getSettings()) {
            if (setting.isEnumSetting()) {
                final EnumConverter converter = new EnumConverter((Class<? extends Enum>) setting.getValue().getClass());
                object.add(setting.getName(), converter.doForward((Enum) setting.getValue()));
            }
            else {
                if (setting.isStringSetting()) {
                    final String str = (String) setting.getValue();
                    setting.setValue(str.replace(" ", "_"));
                }
                try {
                    object.add(setting.getName(), jp.parse(setting.getValueAsString()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }
    
    public String getDirectory(final Feature feature) {
        String directory = "";
        if (feature instanceof Module) {
            directory = directory + ((Module)feature).getCategory().getName() + "/";
        }
        return directory;
    }
}
