package cn.gavin.snmp.core.exception;

/**
 * Created by gluo on 11/14/2016.
 */
public class IPAddressFormatError extends IllegalArgumentException {
    public IPAddressFormatError(String msg){
        super(msg);
    }
}
