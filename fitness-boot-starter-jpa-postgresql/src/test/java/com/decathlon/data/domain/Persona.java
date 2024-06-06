/* AssentSoftware (C)2023 */
package com.decathlon.data.domain;
// Generated 10 jul. 2021 0:09:56 by Hibernate Tools 3.2.2.GA and Assent Architecture

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

import lombok.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Persona generated by hbm2java */
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(
        name = "persona",
        schema = "public",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"cedulaPersona"}),
            @UniqueConstraint(columnNames = {"codigoPersona"}),
            @UniqueConstraint(columnNames = {"numeroMsas"})
        })
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Getter
@Setter
public class Persona implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "persona_seq_gen",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "persona_id_seq"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            })
    @GeneratedValue(generator = "persona_seq_gen")
    @Id
    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    public Long id;

    @Version @Column @EqualsAndHashCode.Include @ToString.Include public Long version;

    @Column(nullable = false, length = 16)
    public String codigoPersona;

    @Column(nullable = false, length = 64)
    public String cedulaPersona;

    @Column(nullable = false, length = 64)
    public String nombrePersona;

    @Column(length = 64)
    public String apellidoPersona;

    @Column(length = 64)
    public String rifPersona;

    @Column public String numeroMsas;

    @Column(length = 13)
    public LocalDate fechaNacimientoPersona;

    @Column(length = 13)
    public LocalDate fechaDefuncionPersona;

    @Column(length = 13)
    public LocalDate fechaVencimientoRif;

    @Column(length = 32)
    public String telefonoMovilPersona;

    @Column(nullable = false, length = 32)
    public String telefonoFijoPersona;

    @Column(length = 64)
    public String eMailPersona;

    @Column(nullable = false)
    public String direccionPersona;

    @Column(length = 64)
    public String nombreContactoUno;

    @Column(length = 32)
    public String telefonoContactoUno;

    @Column(length = 32)
    public String telefonoMovilContactoUno;

    @Column(length = 64)
    public String emailContactoUno;

    @Column public String parentescoContactoUno;

    @Column(length = 64)
    public String nombreContactoDos;

    @Column(length = 32)
    public String telefonoContactoDos;

    @Column(length = 32)
    public String telefonoMovilContactoDos;

    @Column(length = 64)
    public String emailContactoDos;

    @Column public String parentescoContactoDos;

    @Column(precision = 8)
    public Float edad;

    @Column public String url1;

    @Column public String url1FileName;

    @Column(precision = 17)
    public Double url1FileSize;

    @Column public String comentariosAdicionales;

    @Column public String nombreDoctorReferencia;

    @Column(length = 32)
    public String telefonoContactoDoctorReferencia;

    @Column(length = 32)
    public String telefonoMovilDoctorReferencia;

    @Column(length = 64)
    public String emailDoctorReferencia;

    @Column(length = 128)
    public String lugarNacimientoOtro;

    @Column public String direccionPersonaCalleAvenida;

    @Column public String direccionPersonaEdificio;

    @Column public String direccionPersonaConsultorio;

    @CreatedBy @Column public String createdBy;

    @CreatedDate @Column public LocalDateTime createdDate;

    @LastModifiedBy @Column public String lastModifiedBy;

    @LastModifiedDate @Column public LocalDateTime lastModifiedDate;
}
