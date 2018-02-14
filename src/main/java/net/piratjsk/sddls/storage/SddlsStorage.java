package net.piratjsk.sddls.storage;

import net.piratjsk.sddls.Saddle;

import java.util.Map;
import java.util.UUID;

public interface SddlsStorage {

    Map<UUID,Saddle> loadProtectedMounts();
    void saveProtectedMounts(Map<UUID,Saddle> protectedMounts);

    Map<String,UUID> loadUUIDMap();
    void saveUUIDMap(Map<String,UUID> uuidMap);

}
