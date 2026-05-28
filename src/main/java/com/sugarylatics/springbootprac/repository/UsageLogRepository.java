package com.sugarylatics.springbootprac.repository;

import com.sugarylatics.springbootprac.model.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UsageLogRepository extends JpaRepository<UsageLog, UUID> {

}