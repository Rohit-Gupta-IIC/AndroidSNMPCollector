package cn.gavin.snmp;

import android.content.Context;

import cn.gavin.snmp.core.service.DeviceManager;
import cn.gavin.snmp.core.service.GroupManager;
import cn.gavin.snmp.core.service.OIDManager;

/**
 * Created by gluo on 11/9/2016.
 */
public class MainController {
    private static MainController mainController = new MainController();
    private Context context;
    private DeviceManager deviceManager;
    private GroupManager groupManager;
    private OIDManager oidManager;

    public synchronized static void init(Context context) {
        if (mainController == null) {
            mainController = new MainController();
            mainController.context = context;
            mainController.deviceManager = new cn.gavin.snmp.db.DeviceManager(context);
            mainController.groupManager = new cn.gavin.snmp.db.GroupManager(context);
            mainController.oidManager = new cn.gavin.snmp.db.OIDManager(context);
        }
    }

    public synchronized static DeviceManager getDeviceManager() {
        return mainController.deviceManager;
    }

    public synchronized static GroupManager getGroupManager() {
        return mainController.groupManager;
    }

    public synchronized static OIDManager getOIDManger(){
        return mainController.oidManager;
    }
}
