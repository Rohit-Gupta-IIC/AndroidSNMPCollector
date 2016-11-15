package cn.gavin.snmp.android.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gavin.snmp.MainController;
import cn.gavin.snmp.core.monitor.Group;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Group> ITEMS = new ArrayList<Group>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Group> ITEM_MAP = new HashMap<String, Group>();

    public static List<Group> getITEMS(){
        return MainController.getGroupManager().getAllGroup();
    }
}
