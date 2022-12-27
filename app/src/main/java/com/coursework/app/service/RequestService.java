package com.coursework.app.service;

import com.coursework.app.entity.Request;
import com.coursework.app.entity.RequestProduct;
import com.coursework.app.exception.AddRequestException;
import com.coursework.app.repository.RequestRepository;

import java.sql.*;
import java.util.List;

public class RequestService {
    private final RequestRepository requestRepository = new RequestRepository();

    public List<Request> getRequestsBySalePointId(int id) throws SQLException {
        return requestRepository.getRequestsBySalePointId(id);
    }

    public Request addRequest(Request request) throws SQLException, AddRequestException {
        Request request_ = requestRepository.addRequest(request);
        if (request_ == null) {
            throw new AddRequestException("Ошибка при добавлении заявки в БД");
        }
        return request_;
    }

    public RequestProduct addRequestProduct(RequestProduct product) throws SQLException {
        return requestRepository.addRequestProduct(product);
    }
}
