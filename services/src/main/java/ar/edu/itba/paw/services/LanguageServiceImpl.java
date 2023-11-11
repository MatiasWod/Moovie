package ar.edu.itba.paw.services;

import ar.edu.itba.paw.persistence.LanguageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService{

    @Autowired
    LanguageDao languageDao;

    @Override
    public List<String> getAllLanguages() {
        return languageDao.getAllLanguages();
    }

    @Override
    public List<String> getAllLanguages(int type) {
        return languageDao.getAllLanguages(type);
    }
}
