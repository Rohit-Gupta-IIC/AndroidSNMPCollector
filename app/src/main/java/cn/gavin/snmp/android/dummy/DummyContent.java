package cn.gavin.snmp.android.dummy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gavin.snmp.MainController;
import cn.gavin.snmp.core.monitor.Group;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Group> ITEMS = Collections.emptyList();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Group> ITEM_MAP = Collections.emptyMap();

    public static List<Group> getITEMS(){
        List<Group> allGroup = MainController.getGroupManager().getAllGroup();
        ITEM_MAP = new HashMap<>(allGroup.size());
        for(Group g : allGroup){
            ITEM_MAP.put(g.getUuid(), g);
        }
        return allGroup;
    }
}
