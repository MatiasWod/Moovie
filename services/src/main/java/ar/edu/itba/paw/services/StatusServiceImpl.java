package ar.edu.itba.paw.services;

import ar.edu.itba.paw.persistence.StatusDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService{

    @Autowired
    private StatusDao statusDao;

    @Override
    public List<String> getAllStatus() {
        return statusDao.getAllStatus();
    }

    @Override
    public List<String> getAllStatus(int type) {
        return statusDao.getAllStatus(type);
    }
}
