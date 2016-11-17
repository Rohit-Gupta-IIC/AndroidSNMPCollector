package cn.gavin.snmp.core.service;

import android.content.Context;

import cn.gavin.snmp.core.model.Device;
import cn.gavin.snmp.core.model.DeviceImp;
import cn.gavin.snmp.core.monitor.Group;

import java.util.List;
import java.util.UUID;

/**
 * Created by gluo on 11/7/2016.
 */
public abstract class GroupManager extends Manager {
    public GroupManager(Context context) {
        super(context);
    }

    public abstract List<String> getAllGroupNames();
    public abstract Group getGroup(String name);
    public abstract List<String> getGroupNamesByDevice(Device device);
    public abstract void save(Group group);
    Group createGroup(String name){
        Group group = new Group();
        group.setName(name);
        group.setUuid(null);
        save(group);
        return group;
    }
    public abstract Group retrieveGroupData(Group group);
    public abstract void delete(Group group);

    public abstract List<Group> getAllGroup();

    public abstract List<Group> getAllGroupByDevice(DeviceImp device);
}
