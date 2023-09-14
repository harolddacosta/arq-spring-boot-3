/* Decathlon (C)2023 */
package com.decathlon.data.repositories;

import com.decathlon.data.domain.Persona;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Persona, Long> {}
