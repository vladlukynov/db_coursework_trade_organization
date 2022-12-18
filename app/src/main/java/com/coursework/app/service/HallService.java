package com.coursework.app.service;

import com.coursework.app.entity.Hall;
import com.coursework.app.repository.HallRepository;

import java.sql.SQLException;
import java.util.List;

public class HallService {
    private final HallRepository hallRepository = new HallRepository();

    public List<Hall> getHalls() throws SQLException {
        return hallRepository.getHalls();
    }

    public void addHall(Hall hall) throws SQLException {
        hallRepository.addHall(hall);
    }

    public void changeHallStatus(int hallId, boolean status) throws SQLException {
        hallRepository.changeHallStatus(hallId, status);
    }
}
