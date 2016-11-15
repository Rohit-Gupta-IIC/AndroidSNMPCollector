package cn.gavin.snmp.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.gavin.snmp.core.model.DeviceImp;

/**
 * Created by luoyuan on 2016/11/14.
 */
public class DeviceAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    List<DeviceImp> devices;
    private Set<DeviceImp> checks;
    private boolean checkAble;

    public DeviceAdapter(List<DeviceImp> devices, boolean checkAble) {
        this.devices = devices;
        if(checkAble) {
            checks = new HashSet<>(devices.size());
        }else{
            checks = new HashSet<>(devices);
        }
        this.checkAble = checkAble;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public DeviceImp getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckBox isSelected;
        TextView deviceName;
        ImageView deviceStatus;
        if (convertView == null) {
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            isSelected = new CheckBox(parent.getContext());
            deviceName = new TextView(parent.getContext());
            deviceStatus = new ImageView(parent.getContext());
            linearLayout.addView(isSelected);
            linearLayout.addView(deviceName);
            linearLayout.addView(deviceStatus);
            convertView = linearLayout;
        } else {
            LinearLayout view = (LinearLayout) convertView;
            isSelected = (CheckBox) view.getChildAt(0);
            deviceName = (TextView) view.getChildAt(1);
            deviceStatus = (ImageView) view.getChildAt(2);
        }
        if(!checkAble){
            isSelected.setVisibility(View.GONE);
        }
        DeviceImp item = getItem(position);
        isSelected.setTag(R.string.item, item);
        deviceName.setText(item.getName());
        if (item.getSysId().getLatestData() == null || item.getSysId().getLatestData().isEmpty()) {
            deviceStatus.setImageResource(R.drawable.wrong);
        } else {
            deviceStatus.setImageResource(R.drawable.correct);
        }
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object o = buttonView.getTag(R.string.item);
        if (o != null) {
            DeviceImp item = (DeviceImp) o;
            if (isChecked) {
                checks.add(item);
            } else {
                checks.remove(item);
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return devices.isEmpty();
    }

    public Set<DeviceImp> getChecks() {
        return checks;
    }
}
