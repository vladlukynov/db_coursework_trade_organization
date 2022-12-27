package com.coursework.app.service;

import com.coursework.app.entity.Section;
import com.coursework.app.exception.AddSectionException;
import com.coursework.app.exception.NoSectionByIdException;
import com.coursework.app.repository.SectionRepository;

import java.sql.SQLException;
import java.util.List;

public class SectionService {
    private final SectionRepository sectionRepository = new SectionRepository();

    public List<Section> getSections() throws SQLException {
        return sectionRepository.getSections();
    }

    public Section getSectionById(int id) throws SQLException, NoSectionByIdException {
        Section section = sectionRepository.getSectionById(id);
        if (section == null) {
            throw new NoSectionByIdException("Секции с ID " + id + " не найдено");
        }
        return section;
    }

    public Section addSection(Section section) throws SQLException, AddSectionException {
        Section section_ = sectionRepository.addSection(section);
        if (section_ == null) {
            throw new AddSectionException("Не удалось добавить секцию в БД");
        }
        return section_;
    }

    public void deactivateSection(int id) throws SQLException {
        sectionRepository.changeActiveStatus(id, false);
    }

    public List<Section> getSectionsBySalePointId(int salePointId) throws SQLException {
        return sectionRepository.getSectionsBySalePointId(salePointId);
    }

    public List<Section> getSectionsByHallId(int id) throws SQLException {
        return sectionRepository.getSectionsByHallId(id);
    }
}
