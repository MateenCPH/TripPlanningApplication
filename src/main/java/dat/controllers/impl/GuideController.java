package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.daos.impl.GuideDAO;
import dat.dtos.GuideDTO;
import dat.entities.Guide;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Map;

public class GuideController {
    private final GuideDAO dao;

    public GuideController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = GuideDAO.getInstance(emf);
    }

}