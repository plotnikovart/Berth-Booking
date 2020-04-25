package app.database.repository;

import app.database.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, UUID> {
}
