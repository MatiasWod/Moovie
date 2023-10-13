package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.BannedMessageNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.BannedMessage.BannedMessage;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.BannedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannedServiceImpl implements BannedService{
    @Autowired
    BannedDao bannedDao;
    @Autowired
    UserService userService;

    @Override
    public BannedMessage getBannedMessage() {
        return bannedDao.getBannedMessage(10).orElseThrow(() -> new BannedMessageNotFoundException("Banned message for user not found"));
        /*
        try{
            User u = userService.getInfoOfMyUser();
            if(u.getRole()!= userService.ROLE_BANNED){
                throw new BannedMessageNotFoundException("Banned message for user not found, user not banned");
            }
            return bannedDao.getBannedMessage(u.getUserId()).orElseThrow(() -> new BannedMessageNotFoundException("Banned message for user not found"));

        } catch (UserNotLoggedException e){
            throw new BannedMessageNotFoundException("Banned message for user not found, user not logged");
        }*/
    }
}
