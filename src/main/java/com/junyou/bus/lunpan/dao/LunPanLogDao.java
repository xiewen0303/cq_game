package com.junyou.bus.lunpan.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.lunpan.entity.LunPanLog;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.data.filedb.Filedb;

@Component
public class LunPanLogDao {

    // 获得服务器Id
    private String getServerId() {
        return ChuanQiConfigUtil.getServerId();
    }

    @SuppressWarnings("unchecked")
    public void insertDb(LunPanLog lunpanLog) {

        String serverId = getServerId();

        synchronized (GameConstants.LP_LOG_LOCK) {

            try {

                File file = Filedb.getFile(GameConstants.LP_COMPONENET_NAME + lunpanLog.getUserRoleId(), serverId);
                if (null == file) {

                    ObjectOutputStream out = null;
                    try {
                        file = Filedb.mkFile(GameConstants.LP_COMPONENET_NAME + lunpanLog.getUserRoleId(), serverId);
                        out = new ObjectOutputStream(new FileOutputStream(file));
                        out.writeObject(new ArrayList<String>());
                    } catch (Exception e) {
                        ChuanQiLog.error("", e);
                    } finally {
                        if (null != out) {
                            out.close();
                        }
                    }
                }

                ObjectOutputStream out = null;
                try {

                    ObjectInputStream in = null;
                    List<LunPanLog> inData = null;
                    try {
                        in = new ObjectInputStream(new FileInputStream(file));
                        inData = (List<LunPanLog>) in.readObject();
                        inData.add(inData.size(), lunpanLog);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        if (null != in) {
                            in.close();
                        }
                    }

                    // 数据上限验证
                    if (inData.size() > GameConstants.LP_INFO_MAX_COUNT) {
                        inData.remove(0);
                    }
                    out = new ObjectOutputStream(new FileOutputStream(file));
                    out.writeObject(inData);

                } catch (Exception e) {
                    ChuanQiLog.error("", e);
                } finally {
                    if (null != out) {
                        out.close();
                    }
                }

            } catch (Exception e) {
                ChuanQiLog.error("", e);
            }
        }

    }

    @SuppressWarnings("unchecked")
    public List<LunPanLog> getLunpanLogByIdDb(Long userRoleId) {
        String serverId = getServerId();

        File file = Filedb.getFile(GameConstants.LP_COMPONENET_NAME + userRoleId, serverId);
        if (null != file) {
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream(file));
                List<LunPanLog> inData = (List<LunPanLog>) in.readObject();
                return inData;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }

        return null;
    }

}