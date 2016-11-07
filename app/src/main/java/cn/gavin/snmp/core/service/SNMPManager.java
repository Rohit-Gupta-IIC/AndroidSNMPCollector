package cn.gavin.snmp.core.service;

import cn.gavin.snmp.core.model.DataSet;
import cn.gavin.snmp.core.model.Device;
import cn.gavin.snmp.core.snmputil.trap.SNMPTrapListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This will manage all the device add into in.
 * <br>
 * You can create a new device and add it into this manager by calling {@link #addDevice(Device)}<br>
 * And then, this  manager will do the collection by the default interval.<br>
 * This manager will handle a ScheduledExecutorService, so remember calling the {@link #destroy()} before stop you snmp process.
 */
public class SNMPManager implements Runnable {
    private static final SNMPManager _instance = new SNMPManager();
    private final List<Device> deviceList = new ArrayList<Device>();
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> future;
    private Map<String, Future<?>> runningDevices = new HashMap<String, Future<?>>();
    private Set<Integer> listenPorts = new HashSet<Integer>();
    private List<SNMPTrapListener> trapListeners = new ArrayList<SNMPTrapListener>();
    private final Set<String> ips = new HashSet<String>();
    public static synchronized SNMPManager getInstance() {
        if(_instance == null){
        }
        return _instance;
    }

    private SNMPManager() {
        //TODO
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            scheduledExecutorService = Executors.newScheduledThreadPool(51);
        }
        this.future = scheduledExecutorService.scheduleAtFixedRate(this, 1, 60*1000, TimeUnit.MINUTES);
    }

    private void managerDataSet() {
        //TODO Manager DataSet instances
        Set<DataSet> dataSets = DataSet.getAllDataSetInstances();
        for (DataSet dataSet : dataSets) {
            dataSet.rollUp();
        }
    }

    public List<Device> getDevices() {
        return deviceList;
    }

    public int addDevice(Device device) {
        synchronized (ips){
            if(ips.contains(device.getIp())){
                return deviceList.size();
            }else{
                ips.add(device.getIp());
            }
        }
        deviceList.add(device);
        return deviceList.size();
    }

    public int removeDevice(String ip) {
        synchronized (deviceList){
            Device device = null;
            for (Device dev : deviceList) {
                if (dev.getIp().equals(ip)) {
                    device = dev;
                    break;
                }
            }
            if (device != null) {
                Future running = runningDevices.get(device.getIp());
                if (running != null && !running.isCancelled() && !running.isDone()) running.cancel(true);
                runningDevices.remove(device.getIp());
                deviceList.remove(device);
            }
        }
        return deviceList.size();
    }

    @Override
    public void run() {
        Set<Integer> trapPorts = new HashSet<Integer>(deviceList.size());
        for (Device device : deviceList) {
            runningDevices.put(device.getIp(), scheduledExecutorService.submit(device));
            trapPorts.add(device.getSnmpParameter().getTrapPort());
        }
        for(int port : trapPorts){
            if(!listenPorts.contains(port)){
                try {
                    SNMPTrapListener listener = new SNMPTrapListener(port);
                    listenPorts.add(port);
                    trapListeners.add(listener);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void destroy() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }
        for(SNMPTrapListener listener : trapListeners){
            try {
                listener.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        listenPorts.clear();
        trapListeners.clear();
    }

    public synchronized void setScheduledPoolSize(int poolSize){
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdownNow();
        }
        scheduledExecutorService = Executors.newScheduledThreadPool(poolSize);
    }
}

