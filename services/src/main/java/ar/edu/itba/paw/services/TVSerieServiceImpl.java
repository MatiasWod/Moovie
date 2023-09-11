package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.TV.TVSerie;
import ar.edu.itba.paw.persistence.TVSerieDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class TVSerieServiceImpl implements TVSerieService{
    @Autowired
    private TVSerieDao tvSerieDao;
    @Override
    public Optional<TVSerie> getById(int tvId) {
        return tvSerieDao.getById(tvId);
    }

    @Override
    public List<TVSerie> getTvList() {
        return tvSerieDao.getTvList();
    }

    @Override
    public Optional<Integer> getTvCount() {
        return tvSerieDao.getTvCount();
    }
}
