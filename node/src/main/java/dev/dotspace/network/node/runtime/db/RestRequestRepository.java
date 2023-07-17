package dev.dotspace.network.node.runtime.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestRequestRepository extends JpaRepository<RestRequestEntity, Long> {
}
