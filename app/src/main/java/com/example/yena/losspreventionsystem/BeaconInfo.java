package com.example.yena.losspreventionsystem;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;

import java.util.UUID;

/**
 * Created by CAU on 2016-05-10.
 */
public class BeaconInfo {

    private UUID proximityUUID;
    private int major;
    private int minor;
    private int getAdvertisingInterval = 1000;

    public BeaconInfo(UUID proximityUUID, int major, int minor) {
        this.proximityUUID = proximityUUID;
        this.major = major;
        this.minor = minor;
    }

    public BeaconInfo(String UUIDString, int major, int minor) {
        this(UUID.fromString(UUIDString), major, minor);
    }
    public double getDistance(int rssi, int txPower) {
        return Math.pow(10d, ((double) txPower - rssi) / (10 * 2));
    }

    public UUID getProximityUUID() {
        return proximityUUID;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }
    public int getAdvertisingInterval(){
        return getAdvertisingInterval;
    }

    public Region toBeaconRegion() {
        return new Region(toString(), getProximityUUID(), getMajor(), getMinor());
    }

    public String toString() {
        return getProximityUUID().toString() + ":" + getMajor() + ":" + getMinor();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (getClass() != o.getClass()) {
            return super.equals(o);
        }

        Beacon other = (Beacon) o;

        return getProximityUUID().equals(other.getProximityUUID())
                && getMajor() == other.getMajor()
                && getMinor() == other.getMinor();
    }
}
