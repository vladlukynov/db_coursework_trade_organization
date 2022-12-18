package com.coursework.app.service;

import com.coursework.app.entity.Section;
import com.coursework.app.repository.SectionRepository;

import java.sql.SQLException;
import java.util.List;

public class SectionService {
    private final SectionRepository sectionRepository= new SectionRepository();

    public List<Section> getSections() throws SQLException {
        return sectionRepository.getSections();
    }

    public List<Section> getSectionsBySalePointId(int salePointId) throws SQLException {
        return sectionRepository.getSectionsBySalePointId(salePointId);
    }
}
