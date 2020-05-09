package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.BerthApplication;

import java.util.List;

@Repository
public interface BerthApplicationRepository extends JpaRepository<BerthApplication, Long>, JpaSpecificationExecutor<BerthApplication> {

    List<BerthApplication> findAllByApplicant(Account account);
}
