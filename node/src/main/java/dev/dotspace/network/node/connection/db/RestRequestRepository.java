package dev.dotspace.network.node.connection.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface RestRequestRepository extends JpaRepository<RestRequestEntity, Long> {
}
