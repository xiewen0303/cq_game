package com.junyou.bus.lunpan.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.lunpan.entity.RefabuLunpan;
import com.junyou.bus.lunpan.server.LunPanService;

@Service
public class LunPanExportService {

    @Autowired
    private LunPanService lunPanService;

    public List<RefabuLunpan> initRefabuLunpan(Long userRoleId) {
        return lunPanService.initRefabuLunpan(userRoleId);
    }

    public Object[] getRefbLunpanInfo(Long userRoleId, Integer subId) {
        return lunPanService.getRefbInfo(userRoleId, subId);
    }

    public void rechargeYb(Long userRoleId, Long addVal) {
        lunPanService.rechargeYb(userRoleId, addVal);
    }

}
