package app.services;

import app.dao.NGODao;
import app.dao.VolunteerDao;
import app.models.UserRole;

public class AuthService {
    private final NGODao ngoDao = new NGODao();
    private final VolunteerDao volDao = new VolunteerDao();

    public boolean register(String userid, String name, String email, String password, UserRole role) throws Exception {
        if (role == UserRole.NGO) return ngoDao.create(userid, name, email, password);
        return volDao.create(userid, name, email, password);
    }

    public boolean login(String userid, String password, UserRole role) throws Exception {
        if (role == UserRole.NGO) return ngoDao.validate(userid, password);
        return volDao.validate(userid, password);
    }
}