package moe.sndy.carbon.data.profiles;

import moe.sndy.carbon.Carbon;
import moe.sndy.carbon.instances.Adapter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.UUID;

public class MetaAdapter extends Adapter {

    public MetaAdapter(UUID uuid) {
        File profile = new File("Carbon/data/player/" + uuid.toString() + "/global.dat");
        if (profile.exists()) {
            try {
                FileInputStream fileStream = new FileInputStream(profile);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                MetaAdapter adapter = (MetaAdapter) objectStream.readObject();
                attributes = adapter.attributes;
                objectStream.close();
                adapt();
            } catch (Exception e) {
                Carbon.logger().warning("failed to load global profile data from UUID '" + uuid + "'");
                Yaml yaml = new Yaml();
                try {
                    attributes = yaml.load(new FileInputStream(new File("Carbon/resources/GlobalAttributes.yml")));
                } catch (IOException e1) {
                    Carbon.logger().warning("failed to load file 'GlobalAttributes.yaml'");
                }
            }
        } else {
            Yaml yaml = new Yaml();
            try {
                attributes = yaml.load(new FileInputStream(new File("Carbon/resources/GlobalAttributes.yml")));
            } catch (IOException e) {
                Carbon.logger().warning("failed to load file 'GlobalAttributes.yaml'");
            }
            setAttribute(new Date(), "extra", "join-date");
        }
    }

    @Override
    public void adapt() {
        long version = getVersion();
        // Future scalability
    }

    // TODO: Make getters and setters for certain attributes in the Map

}
