package com.coursework.app.service;

import com.coursework.app.entity.Hall;
import com.coursework.app.exception.AddHallException;
import com.coursework.app.exception.NoHallByIdException;
import com.coursework.app.repository.HallRepository;

import java.sql.*;
import java.util.List;

public class HallService {
    private final HallRepository hallRepository = new HallRepository();

    public List<Hall> getHalls() throws SQLException {
        return hallRepository.getHalls();
    }

    public Hall getHallById(int id) throws SQLException, NoHallByIdException {
        Hall hall = hallRepository.getHallById(id);
        if (hall == null) {
            throw new NoHallByIdException("Зала с ID " + id + " не найдено");
        }
        return hall;
    }

    public Hall addHall(Hall hall) throws SQLException, AddHallException {
        Hall hall_ = hallRepository.addHall(hall);
        if (hall_ == null) {
            throw new AddHallException("Не удалось добавить зал в БД");
        }
        return hall_;
    }

    public void deactivateById(int id) throws SQLException {
        hallRepository.changeActiveStatus(id, false);
    }

    public List<Hall> getHallsBySalePointId(int salePointId) throws SQLException {
        return hallRepository.getHallsBySalePointId(salePointId);
    }
}
