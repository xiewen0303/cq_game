package com.junyou.bus.shenmo.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.shenmo.entity.RoleKuafuArena4v4;
import com.junyou.bus.shenmo.service.KuafuArena4v4SourceService;
import com.junyou.utils.KuafuConfigPropUtil;

@Service
public class KuafuArena4v4SourceExportService {
    @Autowired
    private KuafuArena4v4SourceService kuafuArena4v4SourceService;

    public List<RoleKuafuArena4v4> initRoleKuafuArena4v4(Long userRoleId) {
        return kuafuArena4v4SourceService.initRoleKuafuArena4v4(userRoleId);
    }

    public void offlineHandle(Long userRoleId) {
        kuafuArena4v4SourceService.offlineHandle(userRoleId);
    }

    public void startKuafuArenaCleanJob() {
        if (!KuafuConfigPropUtil.isKuafuServer()) {
            kuafuArena4v4SourceService.startKuafuArenaCleanJob();
        }
    }

    public void clean() {
        kuafuArena4v4SourceService.cleanJob();
    }

    public void clean(Long userRoleId) {
        kuafuArena4v4SourceService.cleanUserJifen(userRoleId);
    }
}
