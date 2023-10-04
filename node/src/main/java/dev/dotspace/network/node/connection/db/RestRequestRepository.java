package dev.dotspace.network.node.connection.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestRequestRepository extends JpaRepository<RestRequestEntity, Long> {
}
